package fr.creads.midipile.activities;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import fr.creads.midipile.R;
import fr.creads.midipile.fragments.HomeFragment;
import fr.creads.midipile.fragments.LastWinnerFragment;
import fr.creads.midipile.navigationdrawer.NavigationDrawerFragment;


public class HomeActivity extends FragmentActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {

        Log.d("fr.creads.midipile", "onNavigationDrawerItemSelected ====== position:" + Integer.toString(position));
        switch (position) {
            case 0:
                changeFragment( new HomeFragment(), position,  R.anim.fade_in, R.anim.fade_out);
                break;
            case 1:
                changeFragment( new LastWinnerFragment(), position,  R.anim.fade_in, R.anim.fade_out);
                break;
            case 2:
                changeFragment( new LastWinnerFragment(), position,  R.anim.fade_in, R.anim.fade_out);
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                changeFragment( new HomeFragment(), number,  R.anim.fade_in, R.anim.fade_out);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    /**
     * Change content fragment
     * @param frag
     * @param position
     */
    private void changeFragment(Fragment frag, int position, int animIn, int animOut){

        Log.d("fr.creads.midipile", "Change fragment ======");
        // animation : .setCustomAnimations(animIn, animOut, animIn, animOut)

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()

            .replace(R.id.container, frag, Integer.toString(position))
            .addToBackStack(frag.getClass().toString())
            .commit();
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        //actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        //actionBar.setDisplayShowTitleEnabled(true);
        //actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.home, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
