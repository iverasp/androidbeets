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
import android.widget.ImageButton;
import android.widget.TextView;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import no.iegget.androidbeets.dialogs.AddTrackToPlaylistDialog;
import no.iegget.androidbeets.fragments.AlbumFragment;
import no.iegget.androidbeets.fragments.AlbumsFragment;
import no.iegget.androidbeets.fragments.ArtistsFragment;
import no.iegget.androidbeets.fragments.PlayerFragment;
import no.iegget.androidbeets.fragments.PlaylistFragment;
import no.iegget.androidbeets.fragments.PlaylistsFragment;
import no.iegget.androidbeets.fragments.PreferencesFragment;
import no.iegget.androidbeets.models.Album;
import no.iegget.androidbeets.models.Artist;
import no.iegget.androidbeets.models.Playlist;
import no.iegget.androidbeets.models.Track;
import no.iegget.androidbeets.services.DownloadService;
import no.iegget.androidbeets.services.PlayerService;
import no.iegget.androidbeets.utils.PreferencesManager;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        ArtistsFragment.OnListFragmentInteractionListener,
        AlbumsFragment.OnListFragmentInteractionListener,
        AlbumFragment.OnListFragmentInteractionListener,
        PlayerFragment.OnFragmentInteractionListener,
        PlaylistsFragment.OnListFragmentInteractionListener,
        PlaylistFragment.OnListFragmentInteractionListener,
        AlbumFragment.OnListFragmentOptionsInteractionListener,
        AddTrackToPlaylistDialog.OnAddTrackToPlaylistListener,
        SlidingUpPanelLayout.PanelSlideListener,
        Button.OnClickListener {

    private String TAG = this.getClass().getSimpleName();
    private int currentMenuItem = 3;

    PlayerService mPlayerService;
    DownloadService mDownloadService;
    boolean mPlayerBound = false;
    boolean mDownloadBound = false;

    private Toolbar toolbar;

    private SlidingUpPanelLayout mSlidingUpPanelLayout;
    private boolean firstPlay;
    private ImageButton toggleButton;
    private TextView trackTitleText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        PreferencesManager.initializeInstance(this);

        mSlidingUpPanelLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        mSlidingUpPanelLayout.setTouchEnabled(false);
        mSlidingUpPanelLayout.setPanelSlideListener(this);
        mSlidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        firstPlay = false;
        toggleButton = (ImageButton) findViewById(R.id.panel_player_toggle_button);
        toggleButton.setOnClickListener(this);
        toggleButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_circle_outline_black_24dp));
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

        //ArtistsFragment homeFragment = new ArtistsFragment();
        PlaylistsFragment homeFragment = PlaylistsFragment.newInstance(1);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content_frame, homeFragment);
        //transaction.addToBackStack(null);
        transaction.commit();

        setToolbarTitle("Playlists");

    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent playerIntent = new Intent(this, PlayerService.class);
        bindService(playerIntent, mPlayerConnection, Context.BIND_AUTO_CREATE);
        Intent downloadIntent = new Intent(this, DownloadService.class);
        bindService(downloadIntent, mDownloadConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mPlayerBound) {
            unbindService(mPlayerConnection);
            mPlayerBound = false;
        }
        if (mDownloadBound) {
            unbindService(mDownloadConnection);
            mDownloadBound = false;
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

        Fragment fragment;

        switch(id) {
            case (R.id.nav_search):
                return false;
            case (R.id.nav_artists):
                fragment = ArtistsFragment.newInstance(1);
                setToolbarTitle("Artists");
                break;
            case (R.id.nav_albums):
                fragment = AlbumsFragment.newInstance(2, new Artist(""));
                setToolbarTitle("Albums");
                break;
            case (R.id.nav_playlists):
                fragment = PlaylistsFragment.newInstance(1);
                setToolbarTitle("Playlists");
                break;
            case (R.id.nav_home):
                return false;
            case (R.id.nav_settings):
                fragment = PreferencesFragment.newInstance();
                setToolbarTitle("Preferences");
                break;
            default:
                return false;
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
    public void onListFragmentInteraction(Artist artist) {
        AlbumsFragment albumsFragment = AlbumsFragment.newInstance(
                2,
                new Artist(artist.getName())
        );
        replaceFragment(albumsFragment);
        setToolbarTitle(artist.getName());
    }

    @Override
    public void onListFragmentInteraction(Album album) {
        AlbumFragment albumFragment = AlbumFragment.newInstance(1, album);
        replaceFragment(albumFragment);
        setToolbarTitle(album.getAlbum());
    }

    @Override
    public void onListFragmentInteraction(Track track) {
        Log.w(TAG, "should play file " + track.getTrackId());
        if (!firstPlay) mSlidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        if (mPlayerBound) {
            mPlayerService.loadTrack(track);
            trackTitleText.setText(track.getTitle());
            toggleButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause_circle_outline_black_24dp));
        }
    }

    @Override
    public void onListFragmentOptionsInteractionListener(Track track) {
        AddTrackToPlaylistDialog.newInstance(track).show(getFragmentManager(), "dialog");
    }

    @Override
    public void onAddTrackToPlaylist(Playlist playlist, Track track) {
        if (playlist.isAvailableOffline() && !track.isAvailableOffline())
            mDownloadService.addToQueue(track);
    }

    @Override
    public void onListFragmentInteraction(Playlist playlist) {
        PlaylistFragment playlistFragment = PlaylistFragment.newInstance(1, playlist);
        replaceFragment(playlistFragment);
        setToolbarTitle(playlist.getName());
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mPlayerConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            PlayerService.LocalBinder binder = (PlayerService.LocalBinder) service;
            mPlayerService = binder.getService();
            mPlayerBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mPlayerBound = false;
        }
    };

    private ServiceConnection mDownloadConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            DownloadService.LocalBinder binder = (DownloadService.LocalBinder) service;
            mDownloadService = binder.getService();
            mDownloadBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mDownloadBound = false;
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
        if (mPlayerService.isPlaying()) {
            mPlayerService.pause();
            toggleButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_circle_outline_black_24dp));
        } else {
            mPlayerService.resume();
            toggleButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause_circle_outline_black_24dp));
        }
    }
}
