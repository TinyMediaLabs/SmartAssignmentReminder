package com.coffeetocode.assignmentreminder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
public class MainActivity extends ActionBarActivity {

    public final static int NEW_ASSIGNMENT_REQUEST = 1;
    private static final int RESULT_SETTINGS = 1;
    public List<Assignment> assignments = new ArrayList<Assignment>();
    DBHandler dbHandler = new DBHandler(this);
    private DrawerLayout drawerLayout;
    private LinearLayout drawer;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mTitle, mDrawerTitle;
    private ListView cardFeed;
    private CardArrayAdapter cardArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer = (LinearLayout) findViewById(R.id.left_drawer);
        cardFeed = (ListView) findViewById(R.id.cards_feed);
        cardArrayAdapter = new CardArrayAdapter(this, R.layout.assignment_card);

        updateCards();

        cardFeed.setAdapter(cardArrayAdapter);

        mTitle = getTitle();
        mDrawerTitle = getTitle();
        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.drawable.ic_drawer, R.string.drawer_open,
                R.string.drawer_close) {
            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
            case R.id.action_new:
                Intent i = new Intent(this, AddAssignment.class);
                startActivityForResult(i, NEW_ASSIGNMENT_REQUEST);
                return true;
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
            cardArrayAdapter.add(new Card(assignments.get(i).getID(), assignments.get(i).getTitle(), assignments.get(i).getDescription(), assignments.get(i).getSubject()));
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
        if (resultCode == RESULT_SETTINGS) {
            SharedPreferences sharedPrefs = PreferenceManager
                    .getDefaultSharedPreferences(this);

        }
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the navigation drawer is open, hide action items related to the
        // content
        // view
        boolean drawerOpen = drawerLayout.isDrawerOpen(drawer);
        menu.findItem(R.id.action_new).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    public void openSettings(View view) {
        Intent i = new Intent(this, Settings.class);
        startActivityForResult(i, RESULT_SETTINGS);
    }


}

