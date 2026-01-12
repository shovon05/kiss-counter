# Audio Constraints & Assumptions

## Input Source
- Live microphone input only
- Mono channel
- No recorded audio files in production

## Audio Format
- Sample rate: 16,000 Hz
- Encoding: 16-bit PCM
- Endianness: Little endian

## Processing Window
- Fixed-size time windows (~1 second)
- Sequential processing
- Overlapping windows allowed

## Silence Handling
- Silence and low-energy noise are ignored
- Silence is determined by RMS energy threshold
- Threshold is configurable within safe bounds

## Non-Goals
- No speaker identification
- No speech or language detection
- No audio storage
- No guaranteed performance in extreme noise
