package com.shovon.kisscounter

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Gravity
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.task.audio.AudioRecord
import org.tensorflow.lite.task.audio.TensorAudio
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class MainActivity : AppCompatActivity() {

    private lateinit var interpreter: Interpreter
    private lateinit var audioRecord: AudioRecord
    private lateinit var tensorAudio: TensorAudio

    private lateinit var counterText: TextView

    private val AUDIO_PERMISSION_CODE = 1001
    private val SAMPLE_RATE = 16000
    private val KISS_THRESHOLD = 0.7f

    private var kissCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // UI
        counterText = TextView(this).apply {
            text = "💋 Kisses: 0"
            textSize = 28f
            gravity = Gravity.CENTER
        }
        setContentView(counterText)

        interpreter = loadModel()
        tensorAudio = TensorAudio.create(interpreter)

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

    // ---------------- PERMISSION ----------------

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
            startAudio()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == AUDIO_PERMISSION_CODE &&
            grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            startAudio()
        }
    }

    // ---------------- AUDIO + MFCC ----------------

    private fun startAudio() {
        val audioFormat = AudioRecord.createAudioFormat(
            SAMPLE_RATE,
            1
        )

        val recordConfig = AudioRecord.createRecordConfig(
            audioFormat,
            0.5f
        )

        audioRecord = AudioRecord.createAudioRecord(recordConfig)
        audioRecord.startRecording()

        Thread {
            while (true) {
                tensorAudio.load(audioRecord)
                val output = Array(1) { FloatArray(1) }
                interpreter.run(tensorAudio.tensorBuffer.buffer, output)

                val score = output[0][0]
                if (score > KISS_THRESHOLD) {
                    kissCount++
                    runOnUiThread {
                        counterText.text = "💋 Kisses: $kissCount"
                    }
                }
            }
        }.start()
    }
}
