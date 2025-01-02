package com.developerobaida.italytobanglalibro.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.developerobaida.italytobanglalibro.R;
import com.developerobaida.italytobanglalibro.adLibrary.AdmobAd;

public class Splash extends AppCompatActivity {
    AdmobAd admobAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        admobAd = new AdmobAd(this);
        admobAd.initializeAdmobAd();
        //admobAd.loadAdmobInterstitialAd();

        new Handler().postDelayed(() -> {
            startActivity(new Intent(Splash.this,MainActivity.class));
            finish();
        },1500);
    }
}