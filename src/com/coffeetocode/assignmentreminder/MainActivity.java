package com.coffeetocode.assignmentreminder;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.coffeetocode.assignmentreminder.Assignment;

@SuppressWarnings("deprecation")
public class MainActivity extends Activity {

    private class DrawerItemClickListener implements
            ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {

            // Highlight the selected item, update the title, and close the
            // drawer
            // update selected item and title, then close the drawer
            mDrawerList.setItemChecked(position, true);

            String text = "You clicked item #" + (position + 1);
            Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
            cardArrayAdapter.remove(position);
            mDrawerLayout.closeDrawer(mDrawerList);

        }
    }

    private String[] mDrawerItems;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mTitle, mDrawerTitle;
    private ListView cardFeed;
    public List<Assignment> assignments = new ArrayList<Assignment>();
    private CardArrayAdapter cardArrayAdapter;
    DBHandler dbHandler = new DBHandler(this);
    public final static int NEW_ASSIGNMENT_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDrawerItems = getResources().getStringArray(R.array.drawer_item_names);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        cardFeed = (ListView) findViewById(R.id.cards_feed);
        cardArrayAdapter = new CardArrayAdapter(this, R.layout.assignment_card);

        updateCards();

        cardFeed.setAdapter(cardArrayAdapter);

        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mDrawerItems));
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mTitle = getTitle();
        mDrawerTitle = getTitle();
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, R.string.drawer_open,
                R.string.drawer_close) {
            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to
                // onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to
                // onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
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
            cardArrayAdapter.add(new Card(assignments.get(i).getID(), assignments.get(i).getTitle(), assignments.get(i).getDescription()));
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
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the navigation drawer is open, hide action items related to the
        // content
        // view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_new).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }
}
