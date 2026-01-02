package com.shovon.kisscounter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var counterText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        counterText = TextView(this).apply {
            textSize = 26f
            gravity = Gravity.CENTER
        }

        val startBtn = Button(this).apply {
            text = "Start Counting 💋"
            setOnClickListener {
                startService(Intent(this@MainActivity, KissService::class.java))
                updateCounter()
            }
        }

        val stopBtn = Button(this).apply {
            text = "Stop Counting"
            setOnClickListener {
                stopService(Intent(this@MainActivity, KissService::class.java))
            }
        }

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            addView(counterText)
            addView(startBtn)
            addView(stopBtn)
        }

        setContentView(layout)
        updateCounter()
    }

    private fun updateCounter() {
        val prefs = getSharedPreferences("kiss_prefs", Context.MODE_PRIVATE)
        val count = prefs.getInt("kiss_count", 0)
        counterText.text = "💋 Kisses: $count"
    }

    override fun onResume() {
        super.onResume()
        updateCounter()
    }
}
