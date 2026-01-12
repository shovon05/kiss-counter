package core

import kotlin.math.abs

class SimpleKissEngine(
    private val audioSource: AudioInputSource
) : KissEngine {

    private var state: EngineState = EngineState.IDLE
    private var kissCount = 0
    private var listener: ((KissEvent) -> Unit)? = null

    private val THRESHOLD = 0.5f

    override fun start() {
        if (state != EngineState.IDLE && state != EngineState.STOPPED) return

        state = EngineState.RUNNING

        audioSource.setOnAudioFrameListener { frame ->
            processFrame(frame)
        }

        audioSource.start()
    }

    override fun stop() {
        if (state != EngineState.RUNNING) return

        audioSource.stop()
        state = EngineState.STOPPED
    }

    override fun isRunning(): Boolean = state == EngineState.RUNNING

    override fun getKissCount(): Int = kissCount

    override fun setOnKissDetectedListener(listener: (KissEvent) -> Unit) {
        this.listener = listener
    }

    private fun processFrame(frame: FloatArray) {
    if (state != EngineState.RUNNING) return

    if (detector.analyze(frame)) {
        kissCount++
        listener?.invoke(
            KissEvent(
                timestampMillis = System.currentTimeMillis(),
                confidence = 1.0f
            )
        )
    }
}

}
