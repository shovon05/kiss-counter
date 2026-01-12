# Core Engine Interface

## Purpose
The core engine is responsible for detecting kiss events from live audio input and maintaining a running count.

It is UI-agnostic and platform-independent by design.

## Responsibilities
- Receive continuous audio input
- Detect kiss events based on acoustic patterns
- Apply debounce logic to avoid double-counting
- Maintain an internal kiss count
- Expose current count and detection state

## Inputs
- Continuous audio samples (source unspecified)
- Engine lifecycle commands (start, stop, reset)

## Outputs
- Current kiss count (integer)
- Optional event notifications when a kiss is detected
- Engine running state (active / inactive)

## Core Operations
- Start listening
- Stop listening
- Reset counter
- Query current count
- Subscribe to kiss detection events (optional)

## Guarantees
- Each kiss event increments the count by exactly one
- No UI logic exists inside the engine
- Detection logic can evolve without breaking the interface

## Non-Responsibilities
- Permission handling
- UI rendering
- Data persistence
- Background scheduling
- Platform-specific behavior

## Design Note
This interface is intentionally minimal to allow multiple frontends
(Android, desktop, testing tools) to reuse the same core logic.
