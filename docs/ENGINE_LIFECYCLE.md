# Engine Lifecycle & Threading

## Lifecycle States
- IDLE
- RUNNING
- STOPPED
- ERROR

State transitions are explicit and deterministic.

---

## Lifecycle Rules
- Engine starts only via start()
- Engine stops only via stop() or fatal error
- Engine never restarts automatically
- Cleanup always runs before state change

---

## Threading Model

### Audio Thread
- Handles microphone input
- Runs continuously while RUNNING
- Terminates immediately on stop()

### Processing Thread
- Handles feature extraction and detection
- Consumes buffered audio
- Terminates immediately on stop()

---

## UI Thread Safety
- Engine must never block the UI thread
- All callbacks must be dispatched safely

---

## Interruption Handling
- Any interruption triggers engine stop
- Errors emit onError before shutdown
