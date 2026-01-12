package core

class MockAudioInputSource : AudioInputSource {

    private var listener: ((FloatArray) -> Unit)? = null
    private var running = false

    override fun start() {
        running = true
    }

    override fun stop() {
        running = false
    }

    override fun setOnAudioFrameListener(listener: (FloatArray) -> Unit) {
        this.listener = listener
    }

    /** Simulate silence */
    fun emitSilence() {
        if (!running) return
        listener?.invoke(FloatArray(1024) { 0.01f })
    }

    /** Simulate a kiss-like sound */
    fun emitKiss() {
        if (!running) return
        listener?.invoke(FloatArray(1024) { 0.8f })
    }
}

