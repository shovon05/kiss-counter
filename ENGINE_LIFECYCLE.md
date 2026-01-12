# Engine Lifecycle & State Model

## Engine States

### IDLE
- Engine initialized
- Not listening to audio
- Kiss count preserved

### RUNNING
- Actively listening to audio input
- Performing detection
- Kiss count increments only in this state

### STOPPED
- Listening halted intentionally
- Engine still alive
- Kiss count preserved

### ERROR
- Engine encountered unrecoverable failure
- Requires reset before reuse

## Allowed Transitions
- IDLE → RUNNING
- RUNNING → STOPPED
- STOPPED → RUNNING
- RUNNING → ERROR
- ERROR → IDLE (reset)
- STOPPED → IDLE (reset)

## Reset Behavior
- Reset clears kiss count
- Reset returns engine to IDLE
- Reset is not allowed while RUNNING

## Design Principle
The engine behaves as a strict state machine.
Illegal transitions must be ignored or rejected safely.
