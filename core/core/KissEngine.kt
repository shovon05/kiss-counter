package core

/**
 * KissEngine
 *
 * Platform-agnostic engine responsible for detecting kiss-like
 * acoustic events and emitting structured events.
 *
 * No UI, no Android, no audio implementation details here.
 */
interface KissEngine {

    /** Starts the detection engine */
    fun start()

    /** Stops the detection engine */
    fun stop()

    /** Returns whether the engine is currently running */
    fun isRunning(): Boolean

    /** Returns total number of detected kiss events */
    fun getKissCount(): Int

    /**
     * Register a listener for kiss detection events.
     * Only one listener is supported for now.
     */
    fun setOnKissDetectedListener(listener: (KissEvent) -> Unit)
}

/**
 * Immutable event emitted when a kiss is detected.
 */
data class KissEvent(
    val timestampMillis: Long,
    val confidence: Float
)
