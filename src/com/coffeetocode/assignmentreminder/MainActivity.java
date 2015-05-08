package com.coffeetocode.assignmentreminder;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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
import java.util.List;

@SuppressWarnings("deprecation")
public class MainActivity extends ActionBarActivity {

    public final static int NEW_ASSIGNMENT_REQUEST = 1;
    private static final int RESULT_SETTINGS = 1;
    public List<Assignment> assignments = new ArrayList<Assignment>();
    DBHandler dbHandler = new DBHandler(this);
    BackgroundThread backgroundThread;
    Handler backgroundHandler;
    private DrawerLayout drawerLayout;
    private LinearLayout drawer;
    private ActionBarDrawerToggle drawerToggle;
    private ListView cardFeed;
    private CardArrayAdapter cardArrayAdapter;

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        backgroundThread = new BackgroundThread();
        backgroundThread.setRunning(true);
        backgroundThread.start();
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        boolean retry = true;
        backgroundThread.setRunning(false);

        while (retry) {
            try {
                backgroundThread.join();
                retry = false;
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                toolbar, R.string.drawer_open,
                R.string.drawer_close);
        drawerLayout.setDrawerListener(drawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        drawerToggle.syncState();
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
        return super.onPrepareOptionsMenu(menu);
    }

    public void openSettings(View view) {
        drawerLayout.closeDrawers();
        Intent i = new Intent(this, Settings.class);
        startActivityForResult(i, RESULT_SETTINGS);
    }

    public void newAssignment(View view) {
        Intent i = new Intent(this, AddAssignment.class);
        startActivityForResult(i, NEW_ASSIGNMENT_REQUEST);
    }

    public class BackgroundThread extends Thread {
        final static String ACTION = "NotifyServiceAction";
        private static final int MY_NOTIFICATION_ID = 1;
        private final String myBlog = "http://android-er.blogspot.com/";
        boolean running = false;
        NotificationManager notificationManager;
        Notification myNotification;

        void setRunning(boolean b) {
            running = b;
        }

        @Override
        public synchronized void start() {
            // TODO Auto-generated method stub
            super.start();
            notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }

        @Override
        public void run() {
            // TODO Auto-generated method stub
            while (running) {
                try {
                    sleep(1000000); //send notification in every 10sec.
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                // Send Notification
                myNotification = new Notification(R.drawable.ic_launcher,
                        "Notification!",
                        System.currentTimeMillis());
                Context context = getApplicationContext();
                String notificationTitle = "Assignment incoming!";
                String notificationText = "http://android-er.blogspot.com/";
                Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(myBlog));
                PendingIntent pendingIntent
                        = PendingIntent.getActivity(getBaseContext(),
                        0, myIntent,
                        Intent.FLAG_ACTIVITY_NEW_TASK);
                myNotification.defaults |= Notification.DEFAULT_SOUND;
                myNotification.flags |= Notification.FLAG_AUTO_CANCEL;
                myNotification.setLatestEventInfo(context,
                        notificationTitle,
                        notificationText,
                        pendingIntent);
                notificationManager.notify(MY_NOTIFICATION_ID, myNotification);
            }
        }
    }

}

