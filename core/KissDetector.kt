package core

import kotlin.math.abs

class KissDetector(
    private val energyThreshold: Float = 0.35f,
    private val cooldownMillis: Long = 500
) {

    private var lastDetectionTime = 0L

    fun analyze(frame: FloatArray): Boolean {
        val now = System.currentTimeMillis()

        // Cooldown check
        if (now - lastDetectionTime < cooldownMillis) {
            return false
        }

        // Compute average absolute energy
        val energy = frame.map { abs(it) }.average().toFloat()

        return if (energy > energyThreshold) {
            lastDetectionTime = now
            true
        } else {
            false
        }
    }
}
