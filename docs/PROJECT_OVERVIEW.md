# Kiss Counter — Project Overview & Technical Rationale

## Motivation & Personal Context

This project began as a personal exploration of applied machine learning,
signal processing, and on-device intelligence.

While studying machine learning concepts academically, I wanted to move
beyond abstract datasets and try applying ML to something intuitive,
human, and real-world. Sound felt like a natural medium — it is continuous,
noisy, and deeply contextual, which makes it an interesting challenge.

A simple but meaningful idea emerged:  
**we all kiss the people we love — but how often does it really happen?**

Rather than treating this as a novelty, I approached it as an engineering
problem:
- Can a device detect subtle acoustic patterns?
- Can this be done fully on-device?
- Can privacy be preserved while still using ML?
- Can Android handle continuous audio ML responsibly?

The result is *Kiss Counter*: a locally-running Android application that
detects kiss-like acoustic events using on-device machine learning.

This project represents both:
- a learning exercise in applied ML and Android systems
- an experiment in turning a human, intuitive idea into a technical system

---

## What This Project Does

Kiss Counter is an Android application that listens to live microphone input
and detects **kiss-like sounds** in real time.

When a kiss-like acoustic event is detected:
- the event is classified locally using a trained ML model
- a counter is incremented
- the count is stored persistently on the device

The application can continue running even when the screen is off using a
foreground service, in accordance with Android’s background execution rules.

Importantly:
- **No audio is recorded**
- **No audio is saved**
- **No data leaves the device**

All processing happens locally and transiently.

---

## High-Level System Architecture

At a conceptual level, the system follows this pipeline:


Each stage is deliberately lightweight to ensure:
- low latency
- low battery impact
- compliance with Android platform rules

---

## How Detection Works (Conceptual)

### 1. Audio Capture

The app continuously reads short audio windows from the microphone.
Audio is processed in-memory only and discarded immediately after analysis.

Key characteristics:
- Mono audio
- Fixed sample rate (16 kHz)
- Small time windows (~0.5 seconds)

This window size is chosen to capture the transient nature of a kiss sound.

---

### 2. What the Model Is Actually Looking For

A “kiss” is not detected as a semantic concept.
Instead, the model learns to recognize **acoustic patterns** commonly
associated with kiss-like sounds.

Typical characteristics include:
- very short duration
- soft but sharp transient energy
- specific frequency distributions
- lack of sustained vocal components

The model is trained to distinguish these patterns from:
- speech
- mouth noises
- claps or taps
- background noise

In other words, the model does not “know” what a kiss is — it recognizes
statistical similarities in sound patterns.

---

### 3. Feature Representation

Raw audio waveforms are not used directly for inference.
Instead, the signal is transformed into a compact feature representation
that captures the spectral and temporal structure of the sound.

This reduces:
- sensitivity to volume
- microphone-specific variations
- unnecessary noise

The resulting feature tensor is small, stable, and suitable for mobile ML.

---

### 4. On-Device Machine Learning

The trained model is a lightweight neural network exported to
**TensorFlow Lite** format.

Key design goals:
- fast inference
- small memory footprint
- offline operation
- predictable performance

The model runs fully on-device, producing a probability score indicating
how likely the current audio window represents a kiss-like event.

A configurable threshold is applied to decide whether to count the event.

---

### 5. False-Positive Control

To prevent over-counting:
- a confidence threshold is applied
- a cooldown (debounce) window is enforced
- consecutive detections within a short time are ignored

This ensures that:
- one kiss does not trigger multiple counts
- random noise does not inflate the counter

---

## Privacy & Ethics

Privacy was a core design constraint from the start.

This application:
- does **not** store audio
- does **not** upload data
- does **not** require internet access
- processes all audio transiently in memory

The system observes sound only long enough to extract features and make a
classification decision. No raw data persists beyond that moment.

---

## Learning Outcomes

Through this project, I gained hands-on experience with:

- applying machine learning to real-world sensory data
- designing ML pipelines for mobile constraints
- on-device inference with TensorFlow Lite
- Android audio APIs and background execution rules
- balancing accuracy, performance, and battery usage
- privacy-aware ML system design

Kiss Counter serves both as a functional prototype and as a demonstration
of applied ML engineering on mobile platforms.

The `kiss_model.tflite` file is required and must be present in the
`assets/` directory.

---

## Dependencies

This project uses TensorFlow Lite for on-device ML.

Required Gradle dependencies include:

- TensorFlow Lite runtime
- TensorFlow Lite audio task library

Ensure Gradle sync completes successfully after opening the project.

---

## Running the App

1. Open the `android-app` directory in Android Studio
2. Allow Gradle to sync
3. Connect an Android device or start an emulator
4. Click **Run**

On first launch:
- Android will request microphone permission
- Permission must be granted for the app to function

---

## Using the Application

- Press **Start Counting** to begin detection
- The app will listen for kiss-like sounds
- Detected kisses increment the on-screen counter
- The app can continue running in the background
- Press **Stop Counting** to stop detection

The counter value is saved locally and persists across app restarts.

---

## Background Execution Notes

The app uses an Android **foreground service** to perform background
audio processing.

This means:
- a persistent notification is displayed while running
- Android does not terminate the app unexpectedly
- microphone access remains active while counting is enabled

This follows Android’s recommended practices.

---

## Troubleshooting

### App does not detect sounds
- Ensure microphone permission is granted
- Check that the device microphone is functional
- Try adjusting the detection threshold in code

### App crashes on startup
- Verify `kiss_model.tflite` exists in `assets/`
- Ensure Gradle dependencies are synced
- Check Logcat for error messages

---

## Notes for Developers

This project is intended as:
- a learning-focused prototype
- a demonstration of applied on-device ML

Further improvements may include:
- refined feature extraction
- model retraining with larger datasets
- battery optimization
- UI enhancements
