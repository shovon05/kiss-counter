# Kiss Event Definition

## Definition
A kiss event is a short, discrete acoustic occurrence that matches the characteristic sound pattern of a human kiss (“kisi”).

Each kiss event is counted at most once.

## Properties
- Discrete: represents a single occurrence
- Short-duration: not continuous or sustained
- Acoustic-pattern based: identified by sound characteristics
- Debounced: repeated detections within a short interval may be treated as a single event

## Counting Rules
- One detected kiss event increments the counter by exactly one
- Prolonged or continuous sounds must not generate multiple counts
- Closely spaced events may be merged based on debounce logic

## Non-Events
The following must not be counted as kiss events:
- Speech sounds
- Background noise
- Random mouth clicks
- Environmental pops or taps

## Design Note
The exact detection mechanism is intentionally undefined here.
This definition applies regardless of whether detection is rule-based or ML-based.
