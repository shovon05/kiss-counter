# Kiss Counter — Core Architecture

## Overview
The Kiss Counter core is composed of four independent components arranged in a unidirectional pipeline.

The architecture is designed to keep detection and counting logic independent from UI, platform, and implementation details.

## Components

### Audio Source
Responsible for providing a continuous stream of raw audio samples.
This component performs no analysis or decision-making.

### Kiss Event Detector
Responsible for analyzing audio samples and determining whether a kiss-like sound event has occurred.
Outputs a boolean signal indicating detection.

### Event Counter
Responsible for maintaining a running count of detected kiss events.
Increments only when a detection signal is received.

### State Output
Responsible for exposing the current state (e.g., count) to external consumers such as UI layers or logs.

## Data Flow
Audio data flows strictly in one direction:

Audio Source → Kiss Event Detector → Event Counter → State Output

No component depends on downstream components.

## Design Principle
Each component must be replaceable without modifying others.
