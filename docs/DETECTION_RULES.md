# Kiss Detection Rules

## Model Output
- The ML model outputs a confidence score between 0.0 and 1.0 per audio window

## Confidence Threshold
- A kiss candidate must exceed a configurable threshold
- Default threshold: 0.75

## Temporal Stability
- Confidence must remain above threshold for N consecutive windows
- Prevents single-frame false positives

## Cooldown
- After a confirmed kiss event:
  - Detection is paused for a short cooldown period
  - Prevents double-counting the same kiss

## Event Emission
- One confirmed detection emits exactly one KISS_EVENT
- Counter increments by 1
- Buffers are reset after emission

## Non-Goals
- No intensity scoring
- No quality ranking
- No event merging
