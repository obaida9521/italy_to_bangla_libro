package com.developerobaida.italytobanglalibro.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.developerobaida.italytobanglalibro.R;
import com.developerobaida.italytobanglalibro.databinding.ActivityFeedbackBinding;
import com.developerobaida.italytobanglalibro.models.GetUtils;

public class Feedback extends GetUtils {
    ActivityFeedbackBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityFeedbackBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() !=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        binding.submit.setOnClickListener(v -> {
            if (binding.input.getText().toString() != null && !binding.input.getText().toString().isEmpty()){

                Intent emailIntent = new Intent(Intent.ACTION_VIEW);
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"italytobanglaapp@gmail.com"});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "feedback");
                emailIntent.putExtra(Intent.EXTRA_TEXT, binding.input.getText().toString());

                startActivity(emailIntent);
            }else Toast.makeText(this, "Write something!", Toast.LENGTH_SHORT).show();

        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home) super.onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}