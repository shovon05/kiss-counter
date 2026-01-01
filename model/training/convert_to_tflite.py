import tensorflow as tf

# Load trained Keras model
model = tf.keras.models.load_model("../tflite/kiss_model.h5")

# Create TFLite converter
converter = tf.lite.TFLiteConverter.from_keras_model(model)

# Enable basic optimizations (size + speed)
converter.optimizations = [tf.lite.Optimize.DEFAULT]

# Convert model
tflite_model = converter.convert()

# Save TFLite model
with open("../tflite/kiss_model.tflite", "wb") as f:
    f.write(tflite_model)

print("TFLite model saved successfully.")
