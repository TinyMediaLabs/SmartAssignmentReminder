package com.coffeetocode.assignmentreminder;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@SuppressWarnings("deprecation")
public class MainActivity extends ActionBarActivity {

    public final static int NEW_ASSIGNMENT_REQUEST = 1;
    public final static int EDIT_ASSIGNMENT_REQUEST = 2;
    public final static int SETTINGS_REQUEST = 3;
    public List<Assignment> assignments = new ArrayList<Assignment>();
    DBHandler dbHandler = new DBHandler(this);
    private DrawerLayout drawerLayout;
    private LinearLayout drawer;
    private ActionBarDrawerToggle drawerToggle;
    private ListView cardFeed;
    private CardArrayAdapter cardArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (dbHandler.getDayCount() == 0) {
            dbHandler.createDay(new Day(Calendar.getInstance(), null, 0));
        }

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer = (LinearLayout) findViewById(R.id.left_drawer);
        cardFeed = (ListView) findViewById(R.id.cards_feed);
        cardArrayAdapter = new CardArrayAdapter(this, R.layout.assignment_card);

        updateCards();

        cardFeed.setAdapter(cardArrayAdapter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                toolbar, R.string.drawer_open,
                R.string.drawer_close);
        drawerLayout.setDrawerListener(drawerToggle);

        SharedPreferences sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this);

        if (sharedPrefs.getBoolean("auto_delete", true)) {
            deleteExpiredAssignments();
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        drawerToggle.syncState();
    }

    public void deleteExpiredAssignments() {
        Calendar c = Calendar.getInstance();
        for (int i = 0; i < assignments.size(); i++) {
            if (c.compareTo(assignments.get(i).getDeadline()) == 1) {
                dbHandler.deleteAssignment(assignments.get(i).getID());
            }
        }
        updateCards();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem options = menu.findItem(R.id.action_settings);
        options.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateCards() {

        assignments = dbHandler.getAllAssignments();
        cardArrayAdapter.clear();
        for (int i = 0; i < assignments.size(); i++)
            cardArrayAdapter.add(new Card(assignments.get(i).getID(),
                    assignments.get(i).getTitle(),
                    assignments.get(i).getDescription(),
                    assignments.get(i).getSubject(),
                    assignments.get(i).getDeadline(),
                    assignments.get(i).getDifficulty()));
        SharedPreferences sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this);
        if (sharedPrefs.getString("prefSortType", "Deadline").equals("Deadline")) {
            cardArrayAdapter.sortBy("Deadline");
        } else {
            if (sharedPrefs.getString("prefSortType", "Deadline").equals("Difficulty")) {
                cardArrayAdapter.sortBy("Difficulty");
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == NEW_ASSIGNMENT_REQUEST) {
            // Make sure the request was successful
            updateCards();
            if (resultCode == RESULT_OK) {
                // all ok
            }
        }
        if (requestCode == EDIT_ASSIGNMENT_REQUEST) {
            // Make sure the request was successful
            updateCards();
            if (resultCode == RESULT_OK) {
                // all ok
            }
        }
        if (requestCode == SETTINGS_REQUEST) {
            updateCards();
            //Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // all ok
            }
        }
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the navigation drawer is open, hide action items related to the
        // content
        // view
        boolean drawerOpen = drawerLayout.isDrawerOpen(drawer);
        return super.onPrepareOptionsMenu(menu);
    }

    public void openSettings(View view) {
        drawerLayout.closeDrawers();
        Intent i = new Intent(this, Settings.class);
        startActivityForResult(i, SETTINGS_REQUEST);
    }

    public void showInfoDialog(View view) {
        new AlertDialog.Builder(this)
                .setTitle("Welcome")
                .setMessage("This app was developed by Tiny Media Labs to make your daily life easier. Have fun :)")
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                        dialog.cancel();
                    }
                }).create().show();
    }

    public void newAssignment(View view) {
        Intent i = new Intent(this, AddAssignment.class);
        startActivityForResult(i, NEW_ASSIGNMENT_REQUEST);
    }

}

