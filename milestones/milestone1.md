# MIDI-Maestro

## Executive Summary
Music has been a cornerstone to human culture since the days of our primitive ancestors. In that time, its purpose has varied greatly – from serving as a vessel that carries tribal knowledge through generations to the driver of political agendas - but has always focused on sharing a message in a memorable manner. As with other mediums, making these messages easy to recall is essential to their propagation through society and time. To accomplish this, music makers rely on the power of emotion to evoke a strong response to their compositions. Whether through appealing chord progressions or moving lyrics, a message's successful propagation relies on the musician's ability to make their audience *feel* through music. In its physical performance, music derives much of its emotional impact from the musician's ability to express themselves. Where vocal delivery and instrumentation are predominant tools for this expression, MIDI-Maestro offers musician's another channel by which they can manifest emotion in their music.

## Project Goals
* Compose music in real-time using raw sensor data collected from a wearable tracker.
* Apply various effects to audio tracks using raw sensor data collected from a wearable tracker.
* Process audio tracks generated from raw sensor data in a *Digital Audio Workstation* (DAW).

## User Stories
As a **Music Maker**, I want to **use a wearable tracker** to **create and modify audio tracks**.

**Acceptance Criteria:**
* Application ingests raw sensor data from a wearable tracker over *Bluetooth Low Energy* (BLE).
* Application generates [MIDI messages](https://www.midi.org/articles-old/about-midi-part-3-midi-messages) based on sensor data to create and modify audio tracks.

As a **Music Maker**, I want to **map gestures to custom instruments and audio effects** so I can **create and modify audio tracks**.

**Acceptance Criteria:**
* Application successfully recognizes multiple input gestures.
* Input gestures are able to be mapped to custom instruments and audio effects.

As a **Music Maker**, I want to **use an Android device as a controller** so I can **create and modify audio tracks in a Digital Audio Workstation**.

**Acceptance Criteria:**
* Application connects to a computing device as a MIDI controller.
* Application generates MIDI messages.
* Application sends MIDI messages to a DAW.

## Misuser Stories
As a **Music Tamperer**, I want to **exploit MIDI-Maestro's BLE connection capabilities** to **deny availability of the application to a Music Maker**.

**Mitigations:**
* Application ensures that only authorized input devices can connect via BLE.

As a **Music Tamperer**, I want to **exploit MIDI-Maestro's MIDI message receiving capabilities** to **remotely execute code on a Music Maker's DAW**.

**Mitigations:**
* Application sanitizes MIDI messages that are sent to a DAW.

## High Level Design
![Design Diagram](https://github.com/caseyschmitz/MIDI-Maestro/blob/master/images/MIDI-Maestro_DesignDiagram.png)

## Component List

### Input - Wearable Tracker
Raw sensor data generated by a user's wearable tracker will be sent to MIDI-Maestro via BLE.

### Input - MIDI Controller
MIDI-Maestro will be able to receive MIDI messages from MIDI controllers in addition to the sensor data received from a wearable tracker.

### Processing - Gesture Classifier
Gestures performed by the user will be accurately identified based on raw sensor data sent by the user's wearable tracker. This component will be responsible for negotiating the connection between wearable motion trackers and MIDI-Maestro via BLE.

### Processing - MIDI Engine
The MIDI Engine will convert gestures to MIDI messages.

### Processing - Effects Engine
Gestures mapped to audio effects will be handled in the Effects Engine, where the MIDI messages will be altered according to the gesture performed.

### Output - Audio Signal
MIDI-Maestro will output an audio signal generated by MIDI messages.

### Output - Digital Audio Workstation
MIDI-Maestro will be able to send MIDI messages to Digital Audio Workstation software running on another computing device.

![Component Diagram](https://github.com/caseyschmitz/MIDI-Maestro/blob/master/images/MIDI-Maestro_ComponentDiagram.png)

## Security Analysis
| Component Name | Category of Vulnerability | Issue Description | Mitigation |
|----------------|---------------------------|-------------------|------------|
| Gesture Classifier | Denial of Service | It may be possible to deny availability to this message-receiving component by flooding it with BLE connections. | This component should ensure that only authorized motion tracking devices can connect to the application. |
| Digital Audio Workstation | Remote Code Execution | It may be possible to send the application a specially crafted MIDI file that allows for remote execution of code on the user's DAW. | The application should verify whether MIDI messages received from external controllers are valid. |