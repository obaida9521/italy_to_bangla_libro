package com.developerobaida.italytobanglalibro.activities;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.developerobaida.italytobanglalibro.R;
import com.developerobaida.italytobanglalibro.adLibrary.AdmobAd;
import com.developerobaida.italytobanglalibro.adLibrary.AdmobAdCallBack;
import com.developerobaida.italytobanglalibro.adapters.Adapter2;
import com.developerobaida.italytobanglalibro.api.ApiController;
import com.developerobaida.italytobanglalibro.databinding.ActivityMain2Binding;
import com.developerobaida.italytobanglalibro.models.GetUtils;
import com.developerobaida.italytobanglalibro.models.Model2;
import com.developerobaida.italytobanglalibro.models.ResponseWrapper2;

import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity2 extends GetUtils{
    ActivityMain2Binding binding;
    Adapter2 adapter;
    ArrayList<Model2> arrayList = new ArrayList<>();
    Call<ResponseWrapper2> call;
    String title;
    TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMain2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Bundle bundle = getIntent().getExtras();
        title = bundle.getString("title");
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() !=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        binding.toolbar.setTitle(title);

        new AdmobAd(this, new AdmobAdCallBack() {
            @Override
            public void onAdLoaded() {
            }
        }).loadBanner(binding.bannerAd);

        textToSpeech = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                int result = textToSpeech.setLanguage(new Locale("it","IT"));

                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Toast.makeText(MainActivity2.this, "Italian language need activate from settings", Toast.LENGTH_SHORT).show();
                    Intent installTTSIntent = new Intent();
                    installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                    startActivity(installTTSIntent);
                }
            }
        });


        getData();

    }


    private void getData(){
        binding.progress.setVisibility(View.VISIBLE);
        call = ApiController.getInstance().getApi().getQuestions(title);
        call.enqueue(new Callback<ResponseWrapper2>() {
            @Override
            public void onResponse(Call<ResponseWrapper2> call, Response<ResponseWrapper2> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ResponseWrapper2 wrapper = response.body();
                    if ("success".equals(wrapper.getStatus())) {
                        arrayList = wrapper.getData();
                        Log.d("response", arrayList.toString());

                        binding.progress.setVisibility(View.GONE);

                        adapter = new Adapter2(MainActivity2.this,arrayList,textToSpeech);
                        binding.rec.setAdapter(adapter);
                        binding.rec.setLayoutManager(new LinearLayoutManager(MainActivity2.this));
                    } else {
                        Log.e("error", "Status not success");
                        binding.progress.setVisibility(View.GONE);
                    }
                } else {
                    Log.e("error", "Response body is null or unsuccessful");
                    binding.progress.setVisibility(View.GONE);
                }
            }
            @Override
            public void onFailure(Call<ResponseWrapper2> call, Throwable throwable) {
                binding.progress.setVisibility(View.GONE);
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home) super.onBackPressed();
        return super.onOptionsItemSelected(item);
    }

}