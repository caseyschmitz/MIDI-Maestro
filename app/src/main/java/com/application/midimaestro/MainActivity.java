package com.application.midimaestro;
/***************************************************************************************
 * Portions of this codebase were sampled from open-source documentation provided
 * by the following projects:
 *
 * Title: MetaWear Android API
 * Author: MbientLab
 * Date: 2017
 * Version: 3.7
 * Availability: https://mbientlab.com/androiddocs/latest/index.html
 *
 * Title: MetaWear-Tutorial-Android Free Fall Detector
 * Author: mbientlab
 * Date: 3 April 2018
 * Availability: https://github.com/mbientlab/MetaWear-Tutorial-Android/tree/master/freefall
 *
 * Title: Create a real time line graph in Android with GraphView
 * Author: s.saurel
 * Date: 23 April 2015
 * Availability: https://www.ssaurel.com/blog/create-a-real-time-line-graph-in-android-with-graphview/
 *
 * Title: android-midisuite
 * Author: philburk
 * Date: 30 September 2019
 * Availability: https://github.com/philburk/android-midisuite
 *
***************************************************************************************/

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.leff.midi.MidiFile;
import com.leff.midi.MidiTrack;
import com.leff.midi.event.ChannelEvent;
import com.leff.midi.event.MidiEvent;
import com.leff.midi.examples.EventPrinter;
import com.leff.midi.util.MidiProcessor;
import com.mbientlab.metawear.MetaWearBoard;
import com.mbientlab.metawear.Route;
import com.mbientlab.metawear.Subscriber;
import com.mbientlab.metawear.android.BtleService;
import com.mbientlab.metawear.data.Acceleration;
import com.mbientlab.metawear.module.Accelerometer;
import com.mbientlab.metawear.module.Debug;
import com.mbientlab.metawear.module.Led;
import com.mbientlab.metawear.module.Logging;
import com.mobileer.miditools.synth.SynthEngine;

import org.billthefarmer.mididriver.MidiDriver;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import bolts.Continuation;

public class MainActivity extends AppCompatActivity implements ServiceConnection, MidiDriver.OnMidiStartListener {
    private static final String LOG_TAG = "midimaestro";

    //TODO build a component that allows for connections to MetaWear based on dynamic MAC
    private final String MW_MAC_ADDRESS = "E7:75:2B:2E:50:4B";

    private MetaWearBoard metaWearBoard;
    private Led led;
    private Accelerometer accelerometer;

    private MidiFile mMidi;
    private MidiProcessor mProcessor;
    private SynthEngine mEngine;
    private int[] midiConfig;
    private MidiDriver mDriver;

    private AlertDialog mAlertDialog;
    private Debug debug;
    private Logging logging;

    private LineGraphSeries<DataPoint> series;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize a graph that can be used to view sensor input
        GraphView graph = (GraphView) findViewById(R.id.graph);
        series = new LineGraphSeries<DataPoint>();
        graph.addSeries(series);
        Viewport viewport = graph.getViewport();
        viewport.setYAxisBoundsManual(true);
        viewport.setMinY(0);
        viewport.setMaxY(10);
        viewport.setScrollable(true);

        // Bind the service when the activity is created
        getApplicationContext().bindService(new Intent(this, BtleService.class), this, Context.BIND_AUTO_CREATE);
        findViewById(R.id.load_file).setOnClickListener(v -> showFileChooser());
        findViewById(R.id.play_midi).setOnClickListener(v -> {
            onMidiStart();
        });
        findViewById(R.id.pause_midi).setOnClickListener(v -> {
            mProcessor.stop();
            Log.i(LOG_TAG, "MidiProcessor paused.");
            findViewById(R.id.pause_midi).setEnabled(false);
            findViewById(R.id.play_midi).setEnabled(true);
        });
        findViewById(R.id.stop_midi).setOnClickListener(v -> {
            mProcessor.stop();
            mProcessor.reset();
            Log.i(LOG_TAG, "MidiProcessor stopped.");
            findViewById(R.id.stop_midi).setEnabled(false);
            findViewById(R.id.pause_midi).setEnabled(false);
            findViewById(R.id.play_midi).setEnabled(true);
        });
        findViewById(R.id.start_accel).setOnClickListener(v -> {
            accelerometer.acceleration().start();
            accelerometer.start();
        });
        findViewById(R.id.stop_accel).setOnClickListener(v -> {
            accelerometer.stop();
            accelerometer.acceleration().stop();
        });
        findViewById(R.id.reset_board).setOnClickListener(v -> debug.resetAsync());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Unbind the service when the activity is destroyed
        getApplicationContext().unbindService(this);
        //led.stop(true);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        // Create a connection to the MetaWear board upon service connection
        BtleService.LocalBinder serviceBinder = (BtleService.LocalBinder) service;
        BluetoothManager btManager= (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        BluetoothDevice btDevice= btManager.getAdapter().getRemoteDevice(MW_MAC_ADDRESS);

        metaWearBoard = serviceBinder.getMetaWearBoard(btDevice);

        metaWearBoard.connectAsync().onSuccessTask(task -> {
            // Configure the LED to blink upon successful async connection
            led = metaWearBoard.getModule(Led.class);
            led.editPattern(Led.Color.GREEN)
                    .riseTime((short) 0)
                    .pulseDuration((short) 1000)
                    .repeatCount((byte) 5)
                    .highTime((short) 500)
                    .highIntensity((byte) 16)
                    .lowIntensity((byte) 16)
                    .commit();

            // Configure the accelerometer upon successful async connection
            accelerometer = metaWearBoard.getModule(Accelerometer.class);
            accelerometer.configure()
                    .odr(50f)
                    .commit();

            // Log raw accelerometer received from the MetaWear board
            return accelerometer.acceleration().addRouteAsync(source ->
                    source.stream((Subscriber) (data, env) -> {
                        Log.i(LOG_TAG, data.value(Acceleration.class).toString());
                        //addEntry(data.value(Acceleration.class));
                    }));
        }).continueWith((Continuation<Route, Void>) conn_task -> {
            if (conn_task.isFaulted()) {
                // If connection fails, log and present an AlertDialog indicating failed connection
                Log.i(LOG_TAG, "Failed to connect.");
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.dialog_failed_connection)
                        .setPositiveButton(R.string.okay, (dialog, which) -> {
                        });
                mAlertDialog = builder.create();
                mAlertDialog.show();
                // Disable control buttons if the connection is unsuccessful
                findViewById(R.id.start_accel).setEnabled(false);
                findViewById(R.id.stop_accel).setEnabled(false);
                findViewById(R.id.reset_board).setEnabled(false);
            } else {
                // If connection is successful, log battery status and begin playing the LED
                Log.i(LOG_TAG, "Connected to " + metaWearBoard.getModelString());

                metaWearBoard.readBatteryLevelAsync().continueWith(batt_task -> {
                    Log.i(LOG_TAG, String.valueOf(batt_task.getResult()));
                    return null;
                });
                led.play();
                debug = metaWearBoard.getModule(Debug.class);
                logging = metaWearBoard.getModule(Logging.class);
            }
            return null;
        });
    }

    private <T> void addEntry(T value) {
        //TODO add data-points to the series that is being graphed
    }

    private void showFileChooser() {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, 01);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 01) {
            if (resultCode == RESULT_OK) {
                Uri fileUri = data.getData();
                //String mimeType = getContentResolver().getType(fileUri);

                String fPath = fileUri.getPath();

                Log.i(LOG_TAG, "File path: " + fPath);
                Log.i(LOG_TAG, "File string: " + fileUri.toString());

                InputStream midiInput = null;
                try {
                    midiInput = getContentResolver().openInputStream(fileUri);
                } catch (FileNotFoundException e) {
                    Log.e(LOG_TAG, "Error getting file: ");
                    e.printStackTrace();
                }

                if (midiInput != null) {
                    try {
                        mMidi = new MidiFile(midiInput);
                        findViewById(R.id.play_midi).setEnabled(true);
                        InitProcessor();
                    } catch (IOException e) {
                        Log.e(LOG_TAG, "Error getting Midi file: ");
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void InitProcessor() {
        mProcessor = new MidiProcessor(mMidi);
        //mEngine = new SynthEngine();

        mDriver = new MidiDriver();

        //AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        //String text = audioManager.getProperty(AudioManager.PROPERTY_OUTPUT_FRAMES_PER_BUFFER);
        //int framesPerBlock = Integer.parseInt(text);
        //mEngine.setFramesPerBlock(framesPerBlock);


        EventPrinter mEP = new EventPrinter("Event Listener");
        mProcessor.registerEventListener(mEP, MidiEvent.class);
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        metaWearBoard.onUnexpectedDisconnect(status -> Log.i(LOG_TAG, "Unexpectedly lost connection: " + status));
    }

    @Override
    public void onMidiStart() {
        mProcessor.start();
        Log.i(LOG_TAG, "MidiProcessor started.");
        findViewById(R.id.play_midi).setEnabled(false);
        findViewById(R.id.pause_midi).setEnabled(true);
        findViewById(R.id.stop_midi).setEnabled(true);
        //mEngine.start();
        //mDriver.start();

        //midiConfig = mDriver.config();

        // Print out the details.
        //Log.d(LOG_TAG, "maxVoices: " + midiConfig[0]);
        //Log.d(LOG_TAG, "numChannels: " + midiConfig[1]);
        //Log.d(LOG_TAG, "sampleRate: " + midiConfig[2]);
        //Log.d(LOG_TAG, "mixBufferSize: " + midiConfig[3]);
    }
}
