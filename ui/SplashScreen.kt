package com.qceccenter.qcec.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.qceccenter.qcec.R

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        supportActionBar?.hide()
        Handler().postDelayed({
            val intent = Intent(this@SplashScreen, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }, 1000)
    }
}