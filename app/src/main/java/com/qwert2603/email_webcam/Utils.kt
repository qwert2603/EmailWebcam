package com.qwert2603.email_webcam

fun <T> tryIt(logError: Boolean = true, action: () -> T): T? {
    try {
        return action()
    } catch (t: Throwable) {
        if (logError) LogUtils.e("error", t)
    }
    return null
}