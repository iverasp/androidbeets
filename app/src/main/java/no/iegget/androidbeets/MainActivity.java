package no.iegget.androidbeets;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import no.iegget.androidbeets.content.AlbumContent;
import no.iegget.androidbeets.content.AlbumsContent;
import no.iegget.androidbeets.content.ArtistsContent;
import no.iegget.androidbeets.fragments.AlbumFragment;
import no.iegget.androidbeets.fragments.AlbumsFragment;
import no.iegget.androidbeets.fragments.ArtistsFragment;
import no.iegget.androidbeets.fragments.PlayerFragment;
import no.iegget.androidbeets.models.Album;
import no.iegget.androidbeets.models.Artist;
import no.iegget.androidbeets.models.Track;
import no.iegget.androidbeets.services.PlayerService;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        ArtistsFragment.OnListFragmentInteractionListener,
        AlbumsFragment.OnListFragmentInteractionListener,
        AlbumFragment.OnListFragmentInteractionListener,
        PlayerFragment.OnFragmentInteractionListener,
        SlidingUpPanelLayout.PanelSlideListener,
        Button.OnClickListener {

    private String TAG = this.getClass().getSimpleName();
    private int currentMenuItem = 3;

    PlayerService mPlayerService;
    boolean mBound = false;

    private Toolbar toolbar;

    private SlidingUpPanelLayout mSlidingUpPanelLayout;
    private boolean firstPlay;
    private Button toggleButton;
    private TextView trackTitleText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSlidingUpPanelLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        mSlidingUpPanelLayout.setPanelSlideListener(this);
        mSlidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        firstPlay = false;
        toggleButton = (Button) findViewById(R.id.panel_player_toggle_button);
        toggleButton.setOnClickListener(this);
        trackTitleText = (TextView) findViewById(R.id.panel_player_track_title);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawer,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        ) /*{
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle("App");
            }
            public void onDrawerOpened(View view) {
                getSupportActionBar().setTitle("Menu");
            }
        }*/;

        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ArtistsFragment homeFragment = new ArtistsFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content_frame, homeFragment);
        //transaction.addToBackStack(null);
        transaction.commit();

        setToolbarTitle("Artists");

    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, PlayerService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (mSlidingUpPanelLayout != null &&
                (mSlidingUpPanelLayout.getPanelState()
                        == SlidingUpPanelLayout.PanelState.EXPANDED
                        || mSlidingUpPanelLayout.getPanelState()
                        == SlidingUpPanelLayout.PanelState.ANCHORED)) {
            mSlidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else if (getFragmentManager().getBackStackEntryCount() != 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (currentMenuItem == id) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }
        currentMenuItem = id;

        Fragment fragment = null;
        Class fragmentClass = ArtistsFragment.class;

        if (id == R.id.nav_search) {
            fragmentClass = AlbumFragment.class;
        } else if (id == R.id.nav_artists) {
            fragmentClass = ArtistsFragment.class;
        } else if (id == R.id.nav_albums) {
            fragmentClass = AlbumsFragment.class;
        } else if (id == R.id.nav_home) {

        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_logout) {

        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        replaceFragment(fragment);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content_frame, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void setToolbarTitle(String title) {
        if (getSupportActionBar() != null) getSupportActionBar().setTitle(title);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }


    @Override
    public void onListFragmentInteraction(ArtistsContent.ArtistItem item) {
        AlbumsFragment albumsFragment = AlbumsFragment.newInstance(
                2,
                new Artist(item.name)
        );
        replaceFragment(albumsFragment);
        setToolbarTitle(item.name);
    }

    @Override
    public void onListFragmentInteraction(Album album) {
        AlbumFragment albumFragment = AlbumFragment.newInstance(1, album);

        replaceFragment(albumFragment);
        setToolbarTitle(album.getAlbum());
    }

    @Override
    public void onListFragmentInteraction(Track track) {
        Log.w(TAG, "should play file " + track.getId());
        if (!firstPlay) mSlidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        if (mBound) {
            mPlayerService.loadTrack(track);
            trackTitleText.setText(track.getTitle());
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            PlayerService.LocalBinder binder = (PlayerService.LocalBinder) service;
            mPlayerService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    @Override
    public void onPanelSlide(View panel, float slideOffset) {

    }

    @Override
    public void onPanelCollapsed(View panel) {

    }

    @Override
    public void onPanelExpanded(View panel) {

    }

    @Override
    public void onPanelAnchored(View panel) {

    }

    @Override
    public void onPanelHidden(View panel) {

    }

    @Override
    public void onClick(View v) {
        if (mPlayerService.isPlaying()) mPlayerService.pause();
        else if (!mPlayerService.isPlaying()) mPlayerService.resume();
    }
}
