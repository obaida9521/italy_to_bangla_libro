package com.developerobaida.italytobanglalibro.activities;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.developerobaida.italytobanglalibro.R;
import com.developerobaida.italytobanglalibro.databinding.ActivityMainBinding;
import com.developerobaida.italytobanglalibro.fragments.Home;
import com.developerobaida.italytobanglalibro.fragments.Settings;
import com.developerobaida.italytobanglalibro.fragments.TranslatorFragment;
import com.developerobaida.italytobanglalibro.models.GetUtils;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;

import nl.joery.animatedbottombar.AnimatedBottomBar;

public class MainActivity extends GetUtils {
    ActivityMainBinding binding;
    FragmentStateAdapter adapter;

    private static final String TAG = "UpdateCheck";
    private static final int REQUEST_CODE_UPDATE = 100;
    AppUpdateManager appUpdateManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        appUpdateManager = AppUpdateManagerFactory.create(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO},1414);
        }


        adapter = new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                switch (position) {
                    case 0:
                        return new Home();
                    case 1:
                        return new TranslatorFragment();
                    case 2:
                        return new Settings();
                    default:
                        return new Home();
                }
            }

            @Override
            public int getItemCount() {
                return 3;
            }
        };
        binding.viewPager.setAdapter(adapter);

        binding.bottomNav.setOnTabSelectListener(new AnimatedBottomBar.OnTabSelectListener() {
            @Override
            public void onTabSelected(int i, @Nullable AnimatedBottomBar.Tab tab, int i1, @NonNull AnimatedBottomBar.Tab tab1) {
                int itemId = tab1.getId();
                if (itemId == R.id.home) {
                    binding.viewPager.setCurrentItem(0);
                } else if (itemId == R.id.translate) {
                    binding.viewPager.setCurrentItem(1);
                } else if (itemId == R.id.settings) {
                    binding.viewPager.setCurrentItem(2);
                }
            }

            @Override
            public void onTabReselected(int i, @NonNull AnimatedBottomBar.Tab tab) {

            }
        });


        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:

                        binding.bottomNav.selectTabById(R.id.home,true);
                        break;
                    case 1:
                        binding.bottomNav.selectTabById(R.id.translate,true);
                        break;
                    case 2:
                        binding.bottomNav.selectTabById(R.id.settings,true);
                        break;
                }
            }
        });

    }



    private void checkForAppUpdate() {

        appUpdateManager.getAppUpdateInfo().addOnSuccessListener(appUpdateInfo -> {
            int updateAvailability = appUpdateInfo.updateAvailability();
            Log.i(TAG, "Update availability status: " + updateAvailability);

            if (updateAvailability == UpdateAvailability.UPDATE_AVAILABLE) {
                Log.i(TAG, "An update is available");
                if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                    Log.i(TAG, "Starting flexible update flow...");
                    try {
                        appUpdateManager.startUpdateFlowForResult(
                                appUpdateInfo, AppUpdateType.FLEXIBLE, MainActivity.this, REQUEST_CODE_UPDATE);
                    } catch (IntentSender.SendIntentException e) {
                        Log.e(TAG, "Error starting update flow: " + e.getMessage());
                    }
                } else {
                    Log.i(TAG, "Flexible update not allowed for this update.");
                }
            } else if (updateAvailability == UpdateAvailability.UPDATE_NOT_AVAILABLE) {
                Log.i(TAG, "No update available.");
            } else {
                Log.i(TAG, "Update availability status is unknown or unsupported.");
            }
        }).addOnFailureListener(e -> Log.e(TAG, "Error checking for updates: " + e.getMessage()));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @NonNull Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_UPDATE) {
            if (resultCode != RESULT_OK) {
                Log.e(TAG, "Update flow failed or was canceled by the user. Result code: " + resultCode);
                checkForAppUpdate();
            } else {
                Log.i(TAG, "Update flow completed successfully.");
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        appUpdateManager.getAppUpdateInfo().addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.installStatus() == com.google.android.play.core.install.model.InstallStatus.DOWNLOADED) {
                appUpdateManager.completeUpdate();
                Log.i(TAG, "Downloaded update is now being installed.");
            }
        });
    }
}