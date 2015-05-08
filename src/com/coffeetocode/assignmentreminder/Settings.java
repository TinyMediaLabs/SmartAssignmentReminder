package com.coffeetocode.assignmentreminder;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by Samsung on 5/8/2015.
 */
@SuppressWarnings("deprecation")
public class Settings extends PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.settings);
    }
}
