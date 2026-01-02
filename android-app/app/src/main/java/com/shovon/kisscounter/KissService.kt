package com.shovon.kisscounter

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.task.audio.AudioRecord
import org.tensorflow.lite.task.audio.TensorAudio
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class KissService : Service() {

    private lateinit var interpreter: Interpreter
    private lateinit var audioRecord: AudioRecord
    private lateinit var tensorAudio: TensorAudio

    private val SAMPLE_RATE = 16000
    private val KISS_THRESHOLD = 0.75f
    private val COOLDOWN_MS = 1200
    private var lastKissTime = 0L

    override fun onCreate() {
        super.onCreate()
        startForeground(1, createNotification())

        interpreter = loadModel()
        tensorAudio = TensorAudio.create(interpreter)

        startListening()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun startListening() {
        val audioFormat = AudioRecord.createAudioFormat(SAMPLE_RATE, 1)
        val recordConfig = AudioRecord.createRecordConfig(audioFormat, 0.5f)

        audioRecord = AudioRecord.createAudioRecord(recordConfig)
        audioRecord.startRecording()

        Thread {
            while (true) {
                tensorAudio.load(audioRecord)

                val output = Array(1) { FloatArray(1) }
                interpreter.run(tensorAudio.tensorBuffer.buffer, output)

                val score = output[0][0]
                val now = System.currentTimeMillis()

                if (score > KISS_THRESHOLD &&
                    now - lastKissTime > COOLDOWN_MS
                ) {
                    lastKissTime = now
                    incrementKissCount()
                    Log.d("KISS_SERVICE", "💋 Kiss detected (score=$score)")
                }
            }
        }.start()
    }

    private fun incrementKissCount() {
        val prefs = getSharedPreferences("kiss_prefs", Context.MODE_PRIVATE)
        val count = prefs.getInt("kiss_count", 0) + 1
        prefs.edit().putInt("kiss_count", count).apply()
    }

    private fun loadModel(): Interpreter {
        val afd = assets.openFd("kiss_model.tflite")
        val inputStream = FileInputStream(afd.fileDescriptor)
        val fileChannel = inputStream.channel
        val modelBuffer: MappedByteBuffer = fileChannel.map(
            FileChannel.MapMode.READ_ONLY,
            afd.startOffset,
            afd.declaredLength
        )
        return Interpreter(modelBuffer)
    }

    private fun createNotification(): Notification {
        val channelId = "kiss_counter_channel"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Kiss Counter",
                NotificationManager.IMPORTANCE_LOW
            )
            getSystemService(NotificationManager::class.java)
                .createNotificationChannel(channel)
        }

        return Notification.Builder(this, channelId)
            .setContentTitle("Kiss Counter Running")
            .setContentText("Listening in background 💋")
            .setSmallIcon(android.R.drawable.ic_btn_speak_now)
            .build()
    }
}
