package core

/**
 * Demonstrates how the KissEngine works using a mock audio source.
 *
 * This file is NOT part of the app.
 * It exists to prove engine correctness.
 */
fun main() {
    val audioSource = MockAudioInputSource()
    val engine = SimpleKissEngine(audioSource)

    engine.setOnKissDetectedListener {
        println("💋 Kiss detected! confidence=${it.confidence}")
    }

    println("Starting engine...")
    engine.start()

    println("Emitting silence")
    audioSource.emitSilence()

    println("Emitting kiss")
    audioSource.emitKiss()

    println("Emitting another kiss")
    audioSource.emitKiss()

    engine.stop()

    println("Total kisses detected: ${engine.getKissCount()}")
}
