package com.shovon.kisscounter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.adapter.AndroidAudioInputSource
import core.SimpleKissEngine

class MainActivity : AppCompatActivity() {

    private lateinit var engine: SimpleKissEngine

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val audioSource = AndroidAudioInputSource()
        engine = SimpleKissEngine(audioSource)

        engine.setOnKissDetectedListener {
            // No UI yet — just proving it works
            println("Kiss detected on Android")
        }
    }

    override fun onStart() {
        super.onStart()
        engine.start()
    }

    override fun onStop() {
        super.onStop()
        engine.stop()
    }
}
