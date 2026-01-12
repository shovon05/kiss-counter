# Kiss Event Definition

## What Is a Kiss Event
A kiss event is a short, impulsive acoustic burst consistent with lip contact and release.

---

## Temporal Rules
- Minimum duration: 40 ms
- Maximum duration: 300 ms
- Cooldown after event: 500 ms

---

## Confidence
- Event is valid only if confidence ≥ 0.85
- Confidence aggregation allowed across frames

---

## Aggregation
- Frames within 100 ms are merged
- One merged group = one kiss count

---

## Rejection Rules
- Speech-like patterns
- Continuous audio
- Periodic sounds
- Mechanical clicks
