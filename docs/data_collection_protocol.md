## Data Collection Protocol

This document defines the recording procedure
for creating a clean and consistent dataset.

## Recording Setup
- Single primary device preferred
- WAV, 16-bit PCM
- Mono, 16 kHz
- Duration: 0.3–1.0 seconds

## Environments
- Quiet indoor room
- Mild ambient noise allowed
- No music or media playback

## Classes

### Positive
- Natural kiss-like mouth transients
- No exaggerated or acted sounds

### Negative
- Speech
- Mouth noises
- Sharp transients
- Background noise and silence

## Naming Convention
<class>_<speakerID>_<index>.wav

Example:
kiss_s1_001.wav

## Labeling Rules
- One event per file
- Uncertain samples go to negative classes
