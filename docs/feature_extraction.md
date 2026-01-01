## Feature Extraction Overview

Audio input is transformed into MFCC-based features
for lightweight and robust sound event classification.

## Parameters

- Sample rate: 16 kHz
- Frame size: 25 ms (400 samples)
- Hop length: 10 ms (160 samples)
- Window: Hamming

## Features

- 13 MFCC coefficients
- Delta (Δ)
- Delta-Delta (ΔΔ)
- Total: 39 features per frame

## Temporal Aggregation

- Analysis window: 0.5 seconds
- ~50 frames per inference window
- Feature shape: (frames × features)

## Normalization

- Mean and variance normalization
- Statistics computed from training set only
- Same parameters applied on-device
