# Engine Public API

## Overview
The Kiss Engine exposes a UI-agnostic interface for starting detection, stopping detection, and receiving kiss events.

UI layers must not access audio, ML, or detection internals directly.

---

## Commands

### start()
Starts audio capture and detection.

### stop()
Stops audio capture and releases resources.

### resetCount()
Resets the internal kiss counter to zero.

---

## State

### currentCount
- Integer
- Total confirmed kiss events since last reset

### isRunning
- Boolean
- Indicates whether detection is active

---

## Events

### onKissDetected
Emitted when a kiss event is confirmed.

Payload:
- timestamp
- updated count

### onEngineStateChanged
Emitted when engine state changes.

States:
- IDLE
- RUNNING
- STOPPED
- ERROR

### onError
Emitted when a fatal or recoverable error occurs.

Payload:
- error code
- message
