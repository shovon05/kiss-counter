## Model Overview

A lightweight CNN is used to classify
kiss-like acoustic events from MFCC features.

## Input

- Shape: (50, 39, 1)
- Time frames × MFCC features × channel

## Architecture

- Conv2D (16 filters, 3×3, ReLU)
- Batch Normalization
- Max Pooling (2×2)

- Conv2D (32 filters, 3×3, ReLU)
- Batch Normalization
- Max Pooling (2×2)

- Flatten
- Dense (64, ReLU)
- Dropout (0.3)

- Dense (1, Sigmoid)

## Training

- Loss: Binary Crossentropy
- Optimizer: Adam (lr=0.001)
- Metrics: Accuracy, Precision, Recall

## Constraints

- Designed for on-device inference
- Low latency and low memory footprint
