package com.example.stopwatch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.widget.Button
import android.widget.Chronometer
import com.example.stopwatch.databinding.ActivityMainBinding

// Add key Strings for use with the Bundle
const val OFFSET_KEY = "offset"
const val RUNNING_KEY = "running"
const val BASE_KEY = "base"

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var stopwatch: Chronometer
    private var running = false
    private var offset: Long = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
//        stopwatch = findViewById(R.id.stopwatch)

        // Restore the previous state
        if (savedInstanceState!=null) {
            Log.d("MainActivity", "onSaveInstanceState Retrieving")
            offset = savedInstanceState.getLong(OFFSET_KEY, 0)
            running = savedInstanceState.getBoolean(RUNNING_KEY, false)
            if (running) {
                binding.stopwatch.base = savedInstanceState.getLong(BASE_KEY, 0)
                binding.stopwatch.start()
            } else setBaseTime()
        }




//        val startButton = findViewById<Button>(R.id.start_button)
        binding.startButton.setOnClickListener {
            if (!running) {
                setBaseTime()
                binding.stopwatch.start()
                running = true
            }
        }


//        val pauseButton = findViewById<Button>(R.id.pause_button)
        binding.pauseButton.setOnClickListener {
            if (running) {
                saveOffset()
                binding.stopwatch.stop()
                running = false
            }
        }


//        val resetButton = findViewById<Button>(R.id.reset_button)
        binding.resetButton.setOnClickListener {
            offset = 0
            setBaseTime()
        }

    }

    private fun setBaseTime() {
        binding.stopwatch.base = SystemClock.elapsedRealtime() - offset
    }

    private fun saveOffset() {
        offset = SystemClock.elapsedRealtime() - binding.stopwatch.base
    }

    override fun onSaveInstanceState(outState: Bundle) {
        Log.d("MainActivity", "onSaveInstanceState Called")
        outState.putLong(OFFSET_KEY, offset)
        outState.putBoolean(RUNNING_KEY, running)
        outState.putLong(BASE_KEY, binding.stopwatch.base)
        super.onSaveInstanceState(outState)
    }

    override fun onStop() {
        super.onStop()
        saveOffset()
        stopwatch.stop()
    }

    override fun onRestart() {
        super.onRestart()
        if (running) {
            setBaseTime()
            stopwatch.start()
            offset = 0
        }
    }
}