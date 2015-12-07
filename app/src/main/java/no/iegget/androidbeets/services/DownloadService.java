package no.iegget.androidbeets.services;

import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.activeandroid.query.Select;

import java.util.List;

import no.iegget.androidbeets.models.Download;
import no.iegget.androidbeets.models.Track;
import no.iegget.androidbeets.utils.Global;

/**
 * Created by iver on 06/12/15.
 */
public class DownloadService extends Service {

    private final String TAG = this.getClass().getSimpleName();
    private DownloadManager mDownloadManager;
    private BroadcastReceiver mReceiverDownloadComplete;
    private BroadcastReceiver mReceiverNotificationClicked;
    private IntentFilter intentFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
    private final IBinder mBinder = new LocalBinder();

    public class LocalBinder extends Binder {

        public DownloadService getService() {
            // Return this instance of LocalService so clients can call public methods
            return DownloadService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.w(TAG, "created downloadservice");
        mDownloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        makeDownloadCompleteReceiver();
        registerReceiver(mReceiverDownloadComplete, intentFilter);
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(mReceiverDownloadComplete);
    }

    private void makeDownloadCompleteReceiver() {
        this.mReceiverDownloadComplete = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                long reference = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                List<Download> references = new Select()
                        .from(Download.class)
                        .execute();
                for (Download download : references) {
                    if (download.getReference() == reference) {
                        DownloadManager.Query query = new DownloadManager.Query();
                        query.setFilterById(reference);
                        Cursor cursor = mDownloadManager.query(query);
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
                        int status = cursor.getInt(columnIndex);
                        int fileNameIndex = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME);
                        String savedFilePath = cursor.getString(fileNameIndex);
                        int columnReason = cursor.getColumnIndex(DownloadManager.COLUMN_REASON);
                        int reason = cursor.getInt(columnReason);
                        if (status == DownloadManager.STATUS_SUCCESSFUL) {
                            download.getTrack().setLocalPath(savedFilePath);
                            download.getTrack().save();
                            download.delete();
                        }
                        cursor.close();
                    }
                }
            }
        };
    }

    public long addToQueue(Track track) {
        DownloadManager.Request request =
                new DownloadManager.Request(Uri.parse(Global.getPlaybackUrl(track.getTrackId())));
        request.setTitle("Downloading track");
        request.setDescription(track.getTitle());
        request.setVisibleInDownloadsUi(false);
        long reference = mDownloadManager.enqueue(request);
        track.save();
        new Download(track, reference).save();
        return reference;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}
