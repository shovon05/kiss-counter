package com.shovon.kisscounter

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class MainActivity : AppCompatActivity() {

    private var interpreter: Interpreter? = null
    private val AUDIO_PERMISSION_CODE = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Simple, safe UI
        val tv = TextView(this)
        tv.text = "Kiss Counter\nInitializing…"
        tv.textSize = 20f
        setContentView(tv)

        checkAudioPermission()

        // Load ML model SAFELY (never crash app)
        try {
            interpreter = loadModel()
            Log.d("KISS_COUNTER", "Model loaded successfully")
            tv.text = "Kiss Counter\nModel loaded ✅"
        } catch (e: Exception) {
            Log.e("KISS_COUNTER", "Model load failed", e)
            tv.text = "Kiss Counter\nModel failed to load ❌"
        }
    }

    private fun loadModel(): Interpreter {
        val afd = assets.openFd("kiss_model.tflite")
        val inputStream = FileInputStream(afd.fileDescriptor)
        val fileChannel = inputStream.channel
        val buffer: MappedByteBuffer = fileChannel.map(
            FileChannel.MapMode.READ_ONLY,
            afd.startOffset,
            afd.declaredLength
        )
        return Interpreter(buffer)
    }

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
        }
    }
}
