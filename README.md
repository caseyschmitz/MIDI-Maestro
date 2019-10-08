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
* Application ingests raw sensor data from a wearable tracker over *BTLE*.
* Application generates *MIDI messages* based on sensor data to create and modify audio tracks.

As a **Music Maker**, I want to **map gestures to custom instruments and audio effects** so I can **create and modify audio tracks**.
**Acceptance Criteria:**
* Application successfully recognizes multiple input gestures.
* Input gestures are able to be mapped to custom instruments and audio effects.

As a **Music Maker**, I want to **use an Android device as a controller** so I can **create and modify audio tracks in a *Digital Audio Workstation* (DAW)**.
**Acceptance Criteria:**
* Application connects to a computer as a MIDI controller.
* Application generates MIDI messages.
* Application sends MIDI messages to a DAW program.

## Misuser Stories
As a **Music Tamperer**, I want to **exploit MIDI-Maestro's BTLE connection capabilities** to **deny availability of the application to a Music Maker**.
* Application ensures that only authorized input devices can connect via BTLE.

As a **Music Tamperer**, I want to **exploit MIDI-Maestro's MIDI message transmissions** to **send malicious payloads to a Music Maker's computer**.
* Application sanitizes MIDI messages that are sent to a DAW program.

## High Level Design
![Design Diagram](https://github.com/caseyschmitz/MIDI-Maestro/blob/master/MIDI-Maestro_%20Design%20Diagram.png)

## Component List

## Security Analysis
| Component Name | Category of Vulnerability | Issue Description | Mitigation |
|----------------|---------------------------|-------------------|------------|
