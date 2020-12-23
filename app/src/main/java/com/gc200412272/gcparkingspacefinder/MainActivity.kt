package com.gc200412272.gcparkingspacefinder

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {

                val i = Intent(applicationContext, LoginActivity::class.java)

                startActivity(i)
            }
        }, 2000)
    }
}