# Engine State Machine

## Internal Operational States
- INITIALIZED
- LISTENING
- ANALYZING
- COOLDOWN
- ERROR

---

## State Descriptions

### INITIALIZED
Engine created, audio resources allocated, no audio flowing.

### LISTENING
Audio input active, silence filtered, waiting for candidate events.

### ANALYZING
Candidate audio frames aggregated and evaluated.

### COOLDOWN
Kiss event confirmed, counter incremented, input temporarily ignored.

### ERROR
Fatal error encountered, engine halted.

---

## Transitions
- INITIALIZED → LISTENING
- LISTENING → ANALYZING
- ANALYZING → COOLDOWN (kiss confirmed)
- ANALYZING → LISTENING (candidate rejected)
- COOLDOWN → LISTENING
- ANY → ERROR
- ERROR → INITIALIZED (reset only)

---

## Counting Rule
Counter increments only on ANALYZING → COOLDOWN transition.
