# Audio Input Constraints

## Supported Format
- Sample rate: 16,000 Hz
- Channels: Mono
- Encoding: PCM 16-bit signed
- Endianness: Little-endian
- Source: Live microphone only

---

## Buffering
- Frame duration: 20–30 ms
- Frames must be contiguous
- No gaps or overlaps

---

## Rejection Rules
The engine must reject audio if:
- Sample rate ≠ 16 kHz
- Channels ≠ mono
- Encoding ≠ PCM 16-bit
- Source is not live microphone

---

## Error Handling
- Invalid audio triggers immediate engine stop
- Error must be reported before shutdown
