package com.ozhan.mustafa.partyflashlight;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;

    double maxLaugh = 1;
    int count = 0;
    int ad = 0;
    InterstitialAd interstitial = new InterstitialAd(this);

    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final View decorView = getWindow().getDecorView();
        final int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Toast.makeText(MainActivity.this, "Tap to start Party Flashlight !!", Toast.LENGTH_SHORT).show();

        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);




        final boolean[] isFirst = {true};
        final ImageView im = (ImageView) findViewById(R.id.imageView);
        final SoundMeter sm = new SoundMeter();
        im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ad++;
                decorView.setSystemUiVisibility(uiOptions);
                if (isFirst[0]) {
                    final Handler h = new Handler();
                    final int delay = 50; //milliseconds
                    h.postDelayed(new Runnable() {
                        public void run() {
                            double current = sm.getAmplitude();
                            sm.start();
                            if (current > maxLaugh) {
                                maxLaugh = current;
                            }
                            if (current > 300) {
                                if (current > 6 * maxLaugh / 7) {
                                    im.setBackgroundColor(Color.RED);
                                } else if (current > 5 * maxLaugh / 7) {
                                    im.setBackgroundColor(Color.BLUE);
                                } else if (current > 4 * maxLaugh / 7) {
                                    im.setBackgroundColor(Color.GREEN);
                                } else if (current > 3 * maxLaugh / 7) {
                                    im.setBackgroundColor(Color.YELLOW);
                                } else if (current > 2 * maxLaugh / 7) {
                                    im.setBackgroundColor(Color.WHITE);
                                } else {
                                    im.setBackgroundResource(R.drawable.asd);
                                }
                            }
                            if (ad == 2) {
                                im.setBackgroundResource(R.drawable.asd);
                            }
                            count++;
                            if (count == 20) {
                                count = 0;
                                maxLaugh = 5 * maxLaugh / 6;
                            }
                            h.postDelayed(this, delay);
                        }
                    }, delay);
                    isFirst[0] = false;
                }
                if (ad > 1) {

                    ad=0;
                    interstitial = new InterstitialAd(MainActivity.this);
                    interstitial.setAdUnitId(String.valueOf(R.string.ad_id));

                    AdRequest adRequest1 = new AdRequest.Builder().build();

                    interstitial.loadAd(adRequest1);

                    interstitial.setAdListener(new AdListener() {
                        @Override
                        public void onAdLoaded() {
                            if (interstitial.isLoaded()) {
                                interstitial.show();
                            }
                        }
                    });
                }
            }
        });
        sm.stop();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted ) finish();

    }


}