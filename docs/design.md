## System Overview

The application detects kiss-like acoustic events by analyzing
short audio frames from the device microphone.

Audio is processed in real time and never stored.

## Processing Pipeline

Microphone Input
→ Audio Buffer
→ Feature Extraction (MFCC)
→ ML Classifier
→ Kiss Counter

## Notes

- Target sample rate: 16 kHz
- Mono PCM audio
- On-device inference only
