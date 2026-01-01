## Dataset Objective

Train a lightweight on-device classifier to detect
kiss-like acoustic transients from short audio frames.

## Classes

### Positive
- Kiss-like mouth-generated transients
- Duration ~100–400 ms

### Negative
- Speech (talking, whispering, laughing)
- Mouth noises (clicks, chewing)
- Sharp transients (claps, snaps)
- Background noise and silence

## Audio Format
- WAV, 16-bit PCM
- Mono, 16 kHz
- Max duration: 1 second

## Target Size
- Kiss: 200–300 samples
- Non-kiss: 600–900 samples

## Notes
- No audio will be stored by the app
- Dataset used only for model training
