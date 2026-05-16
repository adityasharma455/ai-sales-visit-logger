package com.example.smartsalesvisit.common

import android.media.MediaPlayer

fun playRecording(path: String?) {
    val mediaPlayer = MediaPlayer().apply {
        setDataSource(path)
        prepare()
        start()
    }
}