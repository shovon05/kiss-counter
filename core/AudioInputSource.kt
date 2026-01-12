package core

/**
 * Platform-specific audio input provider.
 *
 * The engine pulls raw audio frames from this source.
 * No Android, no AudioRecord, no permissions here.
 */
interface AudioInputSource {

    /** Start producing audio data */
    fun start()

    /** Stop producing audio data */
    fun stop()

    /**
     * Register a callback that receives audio frames.
     * Audio format is defined in AUDIO_CONSTRAINTS.md
     */
    fun setOnAudioFrameListener(
        listener: (audioFrame: FloatArray) -> Unit
    )
}
