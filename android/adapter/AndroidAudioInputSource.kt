package android.adapter

import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import core.AudioInputSource
import kotlin.concurrent.thread

class AndroidAudioInputSource : AudioInputSource {

    private var audioRecord: AudioRecord? = null
    private var running = false
    private var listener: ((FloatArray) -> Unit)? = null

    private val sampleRate = 16_000
    private val bufferSize =
        AudioRecord.getMinBufferSize(
            sampleRate,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT
        )

    override fun start() {
        if (running) return

        audioRecord = AudioRecord(
            MediaRecorder.AudioSource.MIC,
            sampleRate,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT,
            bufferSize
        )

        audioRecord?.startRecording()
        running = true

        thread(start = true) {
            val buffer = ShortArray(bufferSize)

            while (running) {
                val read = audioRecord?.read(buffer, 0, buffer.size) ?: 0
                if (read > 0) {
                    val floatFrame = FloatArray(read) {
                        buffer[it] / Short.MAX_VALUE.toFloat()
                    }
                    listener?.invoke(floatFrame)
                }
            }
        }
    }

    override fun stop() {
        running = false
        audioRecord?.stop()
        audioRecord?.release()
        audioRecord = null
    }

    override fun setOnAudioFrameListener(listener: (FloatArray) -> Unit) {
        this.listener = listener
    }
}
