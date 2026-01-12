# Android Engine Adapter

This module bridges the platform-agnostic KissEngine core
with Android-specific components.

Responsibilities:
- Microphone access
- Audio capture
- Lifecycle management
- Background execution

Non-responsibilities:
- UI
- Detection logic
- Kiss classification rules

The core engine must remain untouched by Android code.
