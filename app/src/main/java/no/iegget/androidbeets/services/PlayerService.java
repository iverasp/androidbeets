package no.iegget.androidbeets.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;

import no.iegget.androidbeets.MainActivity;
import no.iegget.androidbeets.models.Track;
import no.iegget.androidbeets.utils.BaseUrl;

/**
 * Created by iver on 01/12/15.
 */
public class PlayerService extends Service
        implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener,
        AudioManager.OnAudioFocusChangeListener {


    MediaPlayer mMediaPlayer = null;
    private final IBinder mBinder = new LocalBinder();
    private final String TAG = this.getClass().getSimpleName();

    WifiManager.WifiLock wifiLock = null;

    PendingIntent pi;

    public class LocalBinder extends Binder {

        public PlayerService getService() {
            // Return this instance of LocalService so clients can call public methods
            return PlayerService.this;
        }
    }
    public int onStartCommand(Intent intent, int flags, int startId) {
        initMediaPlayer();
        pi  = PendingIntent.getActivity(getApplicationContext(), 0,
                new Intent(getApplicationContext(), MainActivity.class),
                PendingIntent.FLAG_UPDATE_CURRENT);

        return 0;
    }

    private void initMediaPlayer() {
        Log.w(TAG, "initmediaplayer");
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnErrorListener(this);
        mMediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
    }

    private void makeForeground(String songName) {

        Notification.Builder notification = new Notification.Builder(this)
                .setContentTitle("AndroidBeets")
                .setContentText("Playing: " + songName);

        Intent resultIntent = new Intent(this, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        notification.setContentIntent(resultPendingIntent);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(99, notification.build());

        startForeground(BIND_IMPORTANT, notification.build());
    }

    public void loadTrack(Track track) {
        Log.w(TAG, "loading track: " + track.getTitle());

        wifiLock = ((WifiManager) getSystemService(Context.WIFI_SERVICE))
                .createWifiLock(WifiManager.WIFI_MODE_FULL, "mylock");
        wifiLock.acquire();
        makeForeground(track.getTitle());
        if (mMediaPlayer == null) {
            initMediaPlayer();
        }

        mMediaPlayer.reset();

        try {
            mMediaPlayer.setDataSource(BaseUrl.baseUrl + "/item/" + track.getId() + "/transcode?coding=mp3&bitrate=192");
            mMediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void pause() {
        if (mMediaPlayer != null) {
            mMediaPlayer.pause();
        }
    }

    public void resume() {
        if (mMediaPlayer != null ) {
            mMediaPlayer.start();
        }
    }

    public boolean isPlaying() {
        if (mMediaPlayer == null) return false;
        return mMediaPlayer.isPlaying();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        wifiLock.release();
        stopForeground(true);
    }

    @Override
    public void onDestroy() {
        if (mMediaPlayer != null) mMediaPlayer.release();
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        // http://developer.android.com/guide/topics/media/mediaplayer.html#audiofocus
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_GAIN:
                // resume playback
                if (mMediaPlayer == null) initMediaPlayer();
                else if (!mMediaPlayer.isPlaying()) mMediaPlayer.start();
                mMediaPlayer.setVolume(1.0f, 1.0f);
                break;

            case AudioManager.AUDIOFOCUS_LOSS:
                // Lost focus for an unbounded amount of time: stop playback and release media player
                if (mMediaPlayer.isPlaying()) mMediaPlayer.stop();
                mMediaPlayer.release();
                mMediaPlayer = null;
                break;

            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                // Lost focus for a short time, but we have to stop
                // playback. We don't release the media player because playback
                // is likely to resume
                if (mMediaPlayer.isPlaying()) mMediaPlayer.pause();
                break;

            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                // Lost focus for a short time, but it's ok to keep playing
                // at an attenuated level
                if (mMediaPlayer.isPlaying()) mMediaPlayer.setVolume(0.1f, 0.1f);
                break;
        }
    }
}
