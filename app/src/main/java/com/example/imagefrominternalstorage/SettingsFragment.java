package com.example.imagefrominternalstorage;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import java.util.List;

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.setting_preference, rootKey);
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        Preference preference = findPreference(s);
        if (preference==null){return;}
        if (preference instanceof ListPreference){
            ListPreference listPreference = (ListPreference) preference;
            if (s.equals("ActionColor"))
            {
                ColorDrawable drawable = new ColorDrawable(Color.parseColor(ImageFragment.getHexColor(listPreference.getValue())));
                MainActivity.actionBar.setBackgroundDrawable(drawable);
            }else if(s.equals("StatusColor")) {
                requireActivity().getWindow().setStatusBarColor(Color.parseColor(ImageFragment.getHexColor(listPreference.getValue())));
            }
            return;
        }
        preference.setSummary(sharedPreferences.getString(s,"Not"));
    }
}