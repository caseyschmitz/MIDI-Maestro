# MIDI-Maestro

## User Story Realization

### User Story 1
*As a Music Maker, I want to use a wearable tracker to create and modify audio tracks.*

As this is the core user story, realizing it involves setting up some of the base components of the application that will allow it to:

    1.1. Establish a connection to a wearable device over BLE
    1.2. Stream raw sensor data from a wearable device
    1.3. Pass raw sensor data to a processing pipeline (User Story 2)
#### Tasks Completed    
I have successfully created the base functionality of connecting to a wearable device over BLE and streaming raw sensor data, though not without challenges. Hardware issues that began with illegible vendor documentation of MAC address (e.g. 'B' vs '8') eventually grew to battery-related problems that resulted in an inability to interact with and code for the wearable sensor. These challenges aside, the application is able to connect to a pre-configured, hard-coded MAC address and initiate a data stream from the device's accelerometer.

#### Tasks Remaining
Refining the code that accomplishes the core tasks of establishing a connection and streaming sensor data from a wearable device need to be generalized to allow for more flexibility regarding which devices are connected. This application would benefit from some kind of 'DeviceManager' that saves various configuration associated with previously connected wearable devices and provides more insight into the current connection status.
Success in realizing this user story with respect to the third criterion will require some design activities that ensure sensor data can be passed reliably and efficiently to a processing pipeline. Understanding the interactions between Activities and Services in the Android framework will be essential, and I plan to spend a considerable amount of time in designing the components of the application such that they realize this requirement.

### User Story 2
*As a Music Maker, I want to map gestures to custom instruments and audio effects so I can create and modify audio tracks.*

Meeting the acceptance criteria for this user story requires the following be delivered:

    2.1 A processing pipeline that, in real- or near-real-time can accurately classify gestures based on sensor data received from the wearable device
    2.2 An ability to map pre-defined gestures to instruments and sound effects that will be used to alter/create MIDI messages

#### Tasks Completed
While no application functionality has been delivered to satisfy the above requirements, preliminary research has been conducted that will support a unique implementation of this user story without reinventing the wheel.

#### Tasks Remaining
The greatest challenge of realizing this user story, and of the overall success of the application, will be in classifying user input. While this is not a trivial task, several research projects have achieved significant results in classifying 2-D and 3-D vectors into a descrete set of pre-configured gestures. Of course, 3-D input is the desired case for this application, but concerns in implementing these frameworks relating to accuracy and resource consumption may lead to use of 2-D input.

Designing the mapping framework for gestures-to-instruments/effects will be altogether less challenging to implement, but will require careful consideration in how this can be configured in the user interface. Again, a better understanding of the interaction between Activities and Services will be necessary to build these components appropriately.

### User Story 3
*As a Music Maker, I want to use an Android device as a controller so I can create and modify audio tracks in a Digital Audio Workstation.*

This user story was designed to fit some 'stretch-goals' of this project and, naturally, should build easily onto components that have already been developed prior to its integration. Requirements of this user story are:

    3.1 An ability for the application to connect to an external computing device as a MIDI controller, and use some of the settings/configurations established to create MIDI messages
    3.2 An ability for the application to send MIDI messages to a Digital Audio Workstation program that can further process the messages

#### Tasks Completed
As these features will be delivered near the end of the development process, little work has been done thus far to realize these requirements. Initial research into Android's MIDI APIs suggests that creating MIDI messages in the application should be relatively simple, though interactions with the endpoint software could introduce challenges.

#### Tasks Remaining
Because these requirements are nearly wholly-dependent on components developed for other user stories, successfully realizing this user story relies heavily on the success of earlier satisfied requirements. Components developed for these requirements may simply act as 'add-ons' to already-existing output options (i.e. direct output through device audio) and some success can be expected in their delivery, though with likely in their minimally-viable form. This user story will not take priority over either of the preceding stories and, as such, its realization will come late in the development process.
