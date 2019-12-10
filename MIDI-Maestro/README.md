# MIDI-Maestro

## Hardware/Software Requirements
- [MetaWear Sensor](https://mbientlab.com/pricing/): Firmware>=1.3.6
- Android device running API level 26-29 (Android 8.0.0 - Android 10)

## Installation
1. Clone the repository with ```git clone https://github.com/caseyschmitz/MIDI-Maestro.git```
2. Build the project with Gradle
    1. *Recommended* - Build the project in Android Studio with Gradle Wrapper >=3.5.3
    2. Build the project with the Grade command-line tool from the project's root directory
    ```
    cd MIDI-Maestro/MIDI-Maestro
    gradle build
    ```
3. Install the application on the Android device by 'running' it in Android Studio

## Getting Started
- Run the application on the Android device
- Enter the MAC address of your MetaWear sensor in the text field and click 'Connect to MW"
    - An AlertDialog popup will notify you if the connection is unsuccessful. Double-check that the MetaWear device is not connected to another device and that the MAC address you've entered is correct and try again.
- A MIDI file (mime=audio/midi) can be selected from the file system for processing by choosing 'Load File'.
    - Processing will begin upon pressing 'Play MIDI'
    - Processing will pause in-place upon pressing 'Pause MIDI'
    - Processing will stop and reset at the beginning of the MIDI file upon pressing 'Stop MIDI'
- Once a MIDI file has been selected and the MetaWear sensor has begun streaming data, processing of the MIDI file can happen in real-time as the MetaWear sensor streams events from its accelerometer (#TODO).
*NOTE* - More logging will be available in Logcat
