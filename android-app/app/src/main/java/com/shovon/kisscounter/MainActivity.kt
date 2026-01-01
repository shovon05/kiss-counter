package com.shovon.kisscounter

import android.Manifest
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import kotlin.math.sqrt

class MainActivity : AppCompatActivity() {

    private lateinit var interpreter: Interpreter
    private lateinit var audioRecord: AudioRecord
    private var isRecording = false

    private lateinit var counterText: TextView

    private val AUDIO_PERMISSION_CODE = 1001
    private val SAMPLE_RATE = 16000
    private val WINDOW_SIZE = 8000
    private val KISS_THRESHOLD = 0.7f

    private var kissCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // -------- UI --------
        counterText = TextView(this).apply {
            text = "💋 Kisses: 0"
            textSize = 28f
            gravity = Gravity.CENTER
        }
        setContentView(counterText)

        // -------- MODEL --------
        interpreter = loadModel()
        Log.d("KISS_COUNTER", "Model loaded")

        // -------- PERMISSION --------
        checkAudioPermission()
    }

    // ---------------- MODEL ----------------

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

    // ---------------- AUDIO + ML ----------------

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
            val audioBuffer = ShortArray(WINDOW_SIZE)

            while (isRecording) {
                val read = audioRecord.read(audioBuffer, 0, audioBuffer.size)
                if (read == WINDOW_SIZE) {
                    val features = extractFeatures(audioBuffer)
                    val score = runInference(features)

                    if (score > KISS_THRESHOLD) {
                        kissCount++
                        runOnUiThread {
                            counterText.text = "💋 Kisses: $kissCount"
                        }
                        Log.d(
                            "KISS_COUNTER",
                            "Kiss detected! score=$score count=$kissCount"
                        )
                    }
                }
            }
        }.start()
    }

    // ---------------- FEATURES ----------------

    private fun extractFeatures(buffer: ShortArray): Array<Array<Array<FloatArray>>> {
        val rms = FloatArray(39)
        val chunk = buffer.size / rms.size

        for (i in rms.indices) {
            var sum = 0.0
            for (j in 0 until chunk) {
                val v = buffer[i * chunk + j].toFloat()
                sum += v * v
            }
            rms[i] = sqrt(sum / chunk).toFloat() / 32768f
        }

        val input = Array(1) {
            Array(50) {
                Array(39) { FloatArray(1) }
            }
        }

        for (i in rms.indices) {
            input[0][i][i][0] = rms[i]
        }

        return input
    }

    // ---------------- INFERENCE ----------------

    private fun runInference(input: Array<Array<Array<FloatArray>>>): Float {
        val output = Array(1) { FloatArray(1) }
        interpreter.run(input, output)
        return output[0][0]
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
