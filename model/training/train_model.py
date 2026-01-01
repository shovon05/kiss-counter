import numpy as np
import tensorflow as tf
from tensorflow.keras import layers, models

# ======================
# Load Dataset
# ======================
data = np.load("../processed/kiss_dataset.npz")

X = data["X"]
y = data["y"]

# Add channel dimension for CNN
X = X[..., np.newaxis]

print("X shape:", X.shape)
print("y shape:", y.shape)

# ======================
# Train / Validation Split
# ======================
split = int(0.8 * len(X))
X_train, X_val = X[:split], X[split:]
y_train, y_val = y[:split], y[split:]

# ======================
# Build Model
# ======================
model = models.Sequential([
    layers.Conv2D(16, (3, 3), activation="relu", input_shape=X_train.shape[1:]),
    layers.BatchNormalization(),
    layers.MaxPooling2D((2, 2)),

    layers.Conv2D(32, (3, 3), activation="relu"),
    layers.BatchNormalization(),
    layers.MaxPooling2D((2, 2)),

    layers.Flatten(),
    layers.Dense(64, activation="relu"),
    layers.Dropout(0.3),

    layers.Dense(1, activation="sigmoid")
])

model.compile(
    optimizer=tf.keras.optimizers.Adam(learning_rate=0.001),
    loss="binary_crossentropy",
    metrics=["accuracy", tf.keras.metrics.Precision(), tf.keras.metrics.Recall()]
)

model.summary()

# ======================
# Train
# ======================
history = model.fit(
    X_train,
    y_train,
    validation_data=(X_val, y_val),
    epochs=20,
    batch_size=32
)

# ======================
# Save Model
# ======================
model.save("../tflite/kiss_model.h5")

print("Model training complete and saved.")
