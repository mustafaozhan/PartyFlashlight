package com.ozhan.mustafa.partyflashlight

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd

class MainActivity : AppCompatActivity() {

    internal var maxLaugh = 1.0
    internal var count = 0
    internal var ad = 0
    internal var interstitial = InterstitialAd(this)

    // Requesting permission to RECORD_AUDIO
    private var permissionToRecordAccepted = false
    private val permissions = arrayOf(Manifest.permission.RECORD_AUDIO)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val decorView = window.decorView
        val uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
        decorView.systemUiVisibility = uiOptions
        setContentView(R.layout.activity_main)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        Toast.makeText(this@MainActivity, "Tap to start Party Flashlight !!", Toast.LENGTH_SHORT).show()

        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION)


        val isFirst = booleanArrayOf(true)
        val im = findViewById(R.id.imageView) as ImageView
        val sm = SoundMeter()
        im.setOnClickListener {
            ad++
            decorView.systemUiVisibility = uiOptions
            if (isFirst[0]) {
                val h = Handler()
                val delay = 50 //milliseconds
                h.postDelayed(object : Runnable {
                    override fun run() {
                        val current = sm.amplitude
                        sm.start()
                        if (current > maxLaugh) {
                            maxLaugh = current
                        }
                        if (current > 300) {
                            if (current > 6 * maxLaugh / 7) {
                                im.setBackgroundColor(Color.RED)
                            } else if (current > 5 * maxLaugh / 7) {
                                im.setBackgroundColor(Color.BLUE)
                            } else if (current > 4 * maxLaugh / 7) {
                                im.setBackgroundColor(Color.GREEN)
                            } else if (current > 3 * maxLaugh / 7) {
                                im.setBackgroundColor(Color.YELLOW)
                            } else if (current > 2 * maxLaugh / 7) {
                                im.setBackgroundColor(Color.WHITE)
                            } else {
                                im.setBackgroundResource(R.drawable.asd)
                            }
                        }
                        if (ad == 2) {
                            im.setBackgroundResource(R.drawable.asd)
                        }
                        count++
                        if (count == 20) {
                            count = 0
                            maxLaugh = 5 * maxLaugh / 6
                        }
                        h.postDelayed(this, delay.toLong())
                    }
                }, delay.toLong())
                isFirst[0] = false
            }
            if (ad > 1) {

                ad = 0
                interstitial = InterstitialAd(this@MainActivity)
                interstitial.adUnitId = R.string.ad_id.toString()

                val adRequest1 = AdRequest.Builder().build()

                interstitial.loadAd(adRequest1)

                interstitial.adListener = object : AdListener() {
                    override fun onAdLoaded() {
                        if (interstitial.isLoaded) {
                            interstitial.show()
                        }
                    }
                }
            }
        }
        sm.stop()
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_RECORD_AUDIO_PERMISSION -> permissionToRecordAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
        }
        if (!permissionToRecordAccepted) finish()

    }

    companion object {
        private val REQUEST_RECORD_AUDIO_PERMISSION = 200
    }


}