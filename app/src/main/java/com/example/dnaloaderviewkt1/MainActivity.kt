package com.example.dnaloaderviewkt1

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var dnaLoaderView: DnaLoaderView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dnaLoaderView = findViewById(R.id.dnaLoaderView)

        // Start the animation after a delay (optional)
        Handler(Looper.getMainLooper()).postDelayed({
            dnaLoaderView.startAnimation()
        }, 1000)
    }
}
