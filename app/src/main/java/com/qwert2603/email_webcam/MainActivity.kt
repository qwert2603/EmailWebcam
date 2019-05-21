package com.qwert2603.email_webcam

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.io.FileOutputStream
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val pic = File(filesDir, "pic.png")
        assets.open("ocv_part.png").use {
            val bytes = it.readBytes()
            FileOutputStream(pic).use {
                it.write(bytes)
            }
        }

        thread {
            while (true) {
                EmailUtils.sendEmail(pic)
                Thread.sleep(10000)
            }
        }
    }
}
