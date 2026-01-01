import os
import numpy as np
import librosa

# ======================
# Configuration
# ======================
SAMPLE_RATE = 16000
N_MFCC = 13
FRAME_LENGTH = int(0.025 * SAMPLE_RATE)  # 25 ms
HOP_LENGTH = int(0.010 * SAMPLE_RATE)    # 10 ms
MAX_DURATION = 1.0  # seconds
TARGET_FRAMES = 50  # ~0.5s window

DATASET_DIR = "../../dataset/raw"

LABEL_MAP = {
    "kiss": 1,
    "speech": 0,
    "mouth_noise": 0,
    "transient": 0,
    "background": 0
}


# ======================
# Utility Functions
# ======================
def load_audio(path):
    audio, sr = librosa.load(
        path,
        sr=SAMPLE_RATE,
        mono=True,
        duration=MAX_DURATION
    )
    return audio


def extract_mfcc(audio):
    mfcc = librosa.feature.mfcc(
        y=audio,
        sr=SAMPLE_RATE,
        n_mfcc=N_MFCC,
        n_fft=FRAME_LENGTH,
        hop_length=HOP_LENGTH
    )

    delta = librosa.feature.delta(mfcc)
    delta2 = librosa.feature.delta(mfcc, order=2)

    features = np.vstack([mfcc, delta, delta2])
    return features.T  # shape: (frames, features)


def pad_or_trim(features):
    if features.shape[0] < TARGET_FRAMES:
        pad_width = TARGET_FRAMES - features.shape[0]
        features = np.pad(features, ((0, pad_width), (0, 0)))
    else:
        features = features[:TARGET_FRAMES]

    return features


# ======================
# Main Pipeline
# ======================
def process_dataset():
    X = []
    y = []

    for class_name, label in LABEL_MAP.items():
        class_dir = os.path.join(DATASET_DIR, class_name)

        if not os.path.isdir(class_dir):
            continue

        for filename in os.listdir(class_dir):
            if not filename.endswith(".wav"):
                continue

            path = os.path.join(class_dir, filename)

            try:
                audio = load_audio(path)
                mfcc_features = extract_mfcc(audio)
                mfcc_features = pad_or_trim(mfcc_features)

                X.append(mfcc_features)
                y.append(label)

            except Exception as e:
                print(f"Error processing {path}: {e}")

    return np.array(X), np.array(y)


if __name__ == "__main__":
    X, y = process_dataset()
    print("Feature shape:", X.shape)
    print("Labels shape:", y.shape)
