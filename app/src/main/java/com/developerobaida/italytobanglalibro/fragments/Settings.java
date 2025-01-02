package com.developerobaida.italytobanglalibro.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.developerobaida.italytobanglalibro.activities.Feedback;
import com.developerobaida.italytobanglalibro.databinding.FragmentSettingsBinding;
import com.developerobaida.italytobanglalibro.models.SharedPref;
import com.google.android.gms.tasks.Task;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;


public class Settings extends Fragment {
    FragmentSettingsBinding binding;

    ReviewManager manager;
    ReviewInfo reviewInfo;
    SharedPref prefs;
    boolean isNightMode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater,container,false);
        prefs = new SharedPref(getContext());
        isNightMode = prefs.getThemeMode();

        if (isNightMode) {
            binding.mode.setChecked(true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

        initReview();
        setupClickListeners();

        return binding.getRoot();
    }


    private void setupClickListeners() {
        String versionName;
        try {
            PackageInfo packageInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            versionName = packageInfo.versionName;
            binding.tvVersion.setText("Version "+versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        binding.versionLay.setOnClickListener(view -> {
            try {
                PackageInfo packageInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);

                Toast.makeText(getContext(), packageInfo.versionName.toString(), Toast.LENGTH_SHORT).show();
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        });


        binding.shareApp.setOnClickListener(v -> shareApp(getContext()));
        binding.rateUs.setOnClickListener(v -> {
            if (reviewInfo != null) {
                Task<Void> flow = manager.launchReviewFlow(getActivity(), reviewInfo);
                flow.addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), "Review Successful", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
        binding.seeOnPlayStore.setOnClickListener(view -> openPlayStore());
        binding.privacyPolicy.setOnClickListener(v -> openPrivacyPolicy());
        binding.removeAd.setOnClickListener(v -> Toast.makeText(getContext(), "This feature is currently not available", Toast.LENGTH_SHORT).show());
        binding.feedback.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), Feedback.class);
            startActivity(intent);
        });
        binding.mode.setOnClickListener(view -> themeMode());

    }
    private void initReview() {
        manager = ReviewManagerFactory.create(requireActivity());
        Task<ReviewInfo> request = manager.requestReviewFlow();
        request.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                reviewInfo = task.getResult();
            } else Toast.makeText(getContext(), "Unable to start in-app review", Toast.LENGTH_SHORT).show();

        });
    }

    private void openPlayStore() {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getContext().getPackageName()));
            intent.setPackage("com.android.vending");
            getContext().startActivity(intent);
        } catch (android.content.ActivityNotFoundException e) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getContext().getPackageName()));
            getContext().startActivity(intent);
        }
    }

    private void openPrivacyPolicy() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://sites.google.com/view/bangla-to-italian/home"));
        startActivity(intent);
    }
    public void shareApp(Context context){
        String packageName = context.getPackageName();
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT,"Download now : https://play.google.com/store/apps/details?id="+packageName);
        intent.setType("text/plain");
        context.startActivity(intent);
    }

    public void themeMode(){

        if (isNightMode){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            prefs.setThemeMode(false);
        }else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            prefs.setThemeMode(true);
        }
        requireActivity().recreate();

    }
}