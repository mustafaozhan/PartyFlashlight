package com.ozhan.mustafa.partyflashlight

import android.media.MediaRecorder

import java.io.IOException

/**
 * Created by mustafa on 8/28/16.
 */
class SoundMeter {


    private var mRecorder: MediaRecorder? = null


    fun start() {

        if (mRecorder == null) {
            mRecorder = MediaRecorder()
            //mRecorder.reset();
            mRecorder!!.setAudioSource(MediaRecorder.AudioSource.MIC)
            mRecorder!!.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            mRecorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            mRecorder!!.setOutputFile("/dev/null")
            try {
                mRecorder!!.prepare()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            mRecorder!!.start()
        }
    }

    fun stop() {
        if (mRecorder != null) {
            mRecorder!!.stop()
            mRecorder!!.release()
            mRecorder = null
        }
    }

    val amplitude: Double
        get() {
            if (mRecorder != null)
                return mRecorder!!.maxAmplitude.toDouble()
            else
                return 0.0

        }
}
