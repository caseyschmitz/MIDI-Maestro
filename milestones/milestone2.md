# MIDI-Maestro

## User Story Realization

### User Story 1
*As a Music Maker, I want to use a wearable tracker to create and modify audio tracks.*
#### Tasks Completed
As this is the core user story, realizing it involves setting up some of the base components of the application that will allow it to:

    1.1. Establish a connection to a wearable device over BLE
    1.2. Stream raw sensor data from a wearable device
    1.3. Pass raw sensor data to a processing pipeline (User Story 2)
    
I have successfully created the base functionality of connecting to a wearable device over BLE and streaming raw sensor data, though not without challenges. Hardware issues that began with illegible vendor documentation of MAC address (e.g. 'B' vs '8') eventually grew to battery-related problems that resulted in an inability to interact with and code for the wearable sensor. These challenges aside, the application is able to connect to a pre-configured, hard-coded MAC address and initiate a data stream from the device's accelerometer.

#### Tasks Remaining
Refining the code that accomplishes the core tasks of establishing a connection and streaming sensor data from a wearable device need to be generalized to allow for more flexibility regarding which devices are connected. This application would benefit from some kind of 'DeviceManager' that saves various configuration associated with previously connected wearable devices and provides more insight into the current connection status.
Success in realizing this user story with respect to the third criterion will require some design activities that ensure sensor data can be passed reliably and efficiently to a processing pipeline. Understanding the interactions between Activities and Services in the Android framework will be essential, and I plan to spend a considerable amount of time in designing the components of the application such that they realize this requirement.

### User Story 2
*As a Music Maker, I want to map gestures to custom instruments and audio effects so I can create and modify audio tracks.*

#### Tasks Completed

### User Story 3
*As a Music Maker, I want to use an Android device as a controller so I can create and modify audio tracks in a Digital Audio Workstation.*

#### Tasks Completed
