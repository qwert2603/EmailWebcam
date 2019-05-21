package com.qwert2603.email_webcam

import android.annotation.SuppressLint
import android.app.Application
import android.content.Intent
import android.graphics.Bitmap
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors
import kotlin.concurrent.thread

class App : Application() {

    companion object {
        private lateinit var dir: File

        private val uploadExecutor = Executors.newSingleThreadExecutor()

        @SuppressLint("SimpleDateFormat")
        private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

        private fun upload() {
            uploadExecutor.submit {
                dir.listFiles()
                    .forEach { file ->
                        LogUtils.d("upload started $file")
                        tryIt {
                            EmailUtils.sendEmail(file)
                            file.delete()
                            LogUtils.d("upload completed $file")
                        }
                    }
            }
        }

        fun savePhoto(bitmap: Bitmap) {
            val fileName = "${dateFormat.format(Date())}.png"
            uploadExecutor.submit {
                FileOutputStream(File(dir, fileName)).use {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
                }
                bitmap.recycle()
                upload()
            }
        }
    }

    override fun onCreate() {
        super.onCreate()

        dir = File(filesDir, "photos")
        dir.mkdirs()

        upload()

        thread {
            Thread.sleep(5 * 1000)
            while (true) {
                val intent = Intent(this, SurfaceViewActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                Thread.sleep(45 * 1000)
            }
        }
    }
}
