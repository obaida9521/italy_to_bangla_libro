package com.developerobaida.italytobanglalibro.models;


import android.content.Context;
import android.content.SharedPreferences;

import com.developerobaida.italytobanglalibro.R;


public class SharedPref {
    private static final String THEME_MODE_KEY = "mode";
    private final SharedPreferences preferences;

    public SharedPref(Context context) {
        preferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
    }

    public void setThemeMode(boolean mode) {
        preferences.edit().putBoolean(THEME_MODE_KEY, mode).apply();
    }

    public boolean getThemeMode() {
        return preferences.getBoolean(THEME_MODE_KEY, false);
    }

    public void clearPreferences() {
        preferences.edit().clear().apply();
    }

}
