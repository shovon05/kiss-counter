package com.shovon.kisscounter

import android.Manifest
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import kotlin.math.abs

class MainActivity : AppCompatActivity() {

    private lateinit var interpreter: Interpreter
    private lateinit var audioRecord: AudioRecord
    private var isRecording = false

    private val AUDIO_PERMISSION_CODE = 1001
    private val SAMPLE_RATE = 16000

    // ---- SOUND EVENT PARAMETERS ----
    private val SOUND_THRESHOLD = 600.0
    private val COOLDOWN_MS = 800
    private var lastEventTime = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(android.R.layout.simple_list_item_1)

        interpreter = loadModel()
        Log.d("KISS_COUNTER", "Model loaded successfully")

        checkAudioPermission()
    }

    // ---------------- MODEL ----------------

    private fun loadModel(): Interpreter {
        val assetFileDescriptor = assets.openFd("kiss_model.tflite")
        val inputStream = FileInputStream(assetFileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = assetFileDescriptor.startOffset
        val declaredLength = assetFileDescriptor.declaredLength

        val modelBuffer: MappedByteBuffer = fileChannel.map(
            FileChannel.MapMode.READ_ONLY,
            startOffset,
            declaredLength
        )

        return Interpreter(modelBuffer)
    }

    // ---------------- PERMISSIONS ----------------

    private fun checkAudioPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                AUDIO_PERMISSION_CODE
            )
        } else {
            startAudioCapture()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == AUDIO_PERMISSION_CODE &&
            grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            startAudioCapture()
        }
    }

    // ---------------- AUDIO ----------------

    private fun startAudioCapture() {
        val bufferSize = AudioRecord.getMinBufferSize(
            SAMPLE_RATE,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT
        )

        audioRecord = AudioRecord(
            MediaRecorder.AudioSource.MIC,
            SAMPLE_RATE,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT,
            bufferSize
        )

        audioRecord.startRecording()
        isRecording = true

        Thread {
            val buffer = ShortArray(bufferSize)

            while (isRecording) {
                val read = audioRecord.read(buffer, 0, buffer.size)

                if (read > 0) {
                    var sum = 0.0
                    for (i in 0 until read) {
                        sum += abs(buffer[i].toInt())
                    }

                    val amplitude = sum / read
                    val now = System.currentTimeMillis()

                    if (amplitude > SOUND_THRESHOLD &&
                        now - lastEventTime > COOLDOWN_MS
                    ) {
                        lastEventTime = now
                        Log.d("KISS_COUNTER", "🔊 Sound event detected!")
                    }
                }
            }
        }.start()
    }

    // ---------------- CLEANUP ----------------

    override fun onDestroy() {
        super.onDestroy()
        if (isRecording) {
            isRecording = false
            audioRecord.stop()
            audioRecord.release()
        }
    }
}
