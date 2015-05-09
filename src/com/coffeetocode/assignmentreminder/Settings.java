package com.coffeetocode.assignmentreminder;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

/**
 * Created by Samsung on 5/8/2015.
 */
public class Settings extends ActionBarActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        getFragmentManager().beginTransaction()
                .replace(R.id.container,
                        new MainSettingsFragment()).commit();
    }

    public static class MainSettingsFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings);
            Preference button = (Preference) findPreference(getString(R.string.pref_clear_data));
            button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Delete assignments?")
                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface arg0, int arg1) {
                                    DBHandler dbHandler = new DBHandler(getActivity());
                                    dbHandler.deleteAllAssignments();
                                    Toast.makeText(getActivity(), "Assignments deleted", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int arg1) {
                                    dialog.cancel();
                                }
                            }).create().show();
                    return true;
                }
            });
        }
    }
}
