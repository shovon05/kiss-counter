# Kiss Counter — Core Definition

## Purpose
Kiss Counter is a mobile application that continuously listens to live microphone input on-device and detects kiss-like (“kisi”) sound events in real time.

Each detected kiss event increments a counter that is visible to the user.

## Scope
The core responsibility of Kiss Counter is limited to:

- Accessing live microphone audio
- Continuously monitoring audio input
- Detecting kiss-like acoustic events
- Registering each detected event
- Maintaining a running count

## Non-Goals (Core Layer)
The following are intentionally excluded from the core definition:

- UI design or layout
- Visual styling or animations
- Platform-specific implementation details
- Model training methodology
- Cloud processing or data storage
- Social, sharing, or gamification features

## Design Principle
The detection and counting logic must remain independent of:
- UI implementation
- Visual design
- Platform-specific frameworks

This ensures that the core functionality can be reused, extended, or re-skinned without changing its fundamental behavior.
