package com.shovon.kisscounter

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.adapter.AndroidAudioInputSource
import core.SimpleKissEngine

class MainActivity : AppCompatActivity() {

    private lateinit var engine: SimpleKissEngine
    private lateinit var kissCountText: TextView

    private var kissCount = 0

    private val AUDIO_PERMISSION_CODE = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Attach UI
        setContentView(R.layout.activity_main)
        kissCountText = findViewById(R.id.kissCountText)
        kissCountText.text = "Kisses: 0"

        // Create engine
        val audioSource = AndroidAudioInputSource()
        engine = SimpleKissEngine(audioSource)

        // Listen for kiss events
        engine.setOnKissDetectedListener {
            kissCount++
            runOnUiThread {
                kissCountText.text = "Kisses: $kissCount"
            }
        }

        checkAudioPermission()
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
        } else {
            engine.start()
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
            engine.start()
        }
    }

    override fun onStop() {
        super.onStop()
        if (::engine.isInitialized) {
            engine.stop()
        }
    }
}
