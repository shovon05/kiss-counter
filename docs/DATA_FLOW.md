# Data Flow & Buffering Model

## Overview
The engine uses a producer–consumer model to decouple audio capture from processing.

---

## Threads
- Audio Thread: captures audio frames
- Processing Thread: analyzes frames and detects events

---

## Buffering
- A bounded FIFO queue connects the threads
- Audio thread pushes frames
- Processing thread pulls frames

---

## Frame Dropping
- If the queue is full, oldest frames are dropped
- Dropping frames is preferred over blocking
- Occasional loss does not break detection

---

## Thread Safety
- Audio thread writes only
- Processing thread reads only
- Counter updates occur only on processing thread

---

## Shutdown
- stop(): audio stops → queue cleared → processing finishes
- error(): immediate termination and cleanup
