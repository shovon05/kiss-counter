package core

class SimpleKissEngine : KissEngine {

    private var state: EngineState = EngineState.IDLE
    private var kissCount: Int = 0
    private var listener: ((KissEvent) -> Unit)? = null

    override fun start() {
        if (state == EngineState.RUNNING) return
        state = EngineState.RUNNING
    }

    override fun stop() {
        if (state != EngineState.RUNNING) return
        state = EngineState.STOPPED
    }

    override fun isRunning(): Boolean {
        return state == EngineState.RUNNING
    }

    override fun getKissCount(): Int {
        return kissCount
    }

    override fun setOnKissDetectedListener(listener: (KissEvent) -> Unit) {
        this.listener = listener
    }

    /**
     * Test-only method.
     * Simulates a detected kiss event.
     */
    fun simulateKiss(confidence: Float = 1.0f) {
        if (state != EngineState.RUNNING) return

        kissCount++

        listener?.invoke(
            KissEvent(
                timestampMillis = System.currentTimeMillis(),
                confidence = confidence
            )
        )
    }
}
