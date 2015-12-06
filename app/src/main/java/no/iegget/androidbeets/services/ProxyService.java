package no.iegget.androidbeets.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.activeandroid.query.Select;
import com.danikula.videocache.HttpProxyCacheServer;

import no.iegget.androidbeets.models.Track;
import no.iegget.androidbeets.utils.Global;

/**
 * Created by iver on 06/12/15.
 */
public class ProxyService extends Service {

    private final String TAG = this.getClass().getSimpleName();
    private HttpProxyCacheServer proxy;
    private final IBinder mBinder = new LocalBinder();

    public class LocalBinder extends Binder {
        public ProxyService getService() {
            // Return this instance of LocalService so clients can call public methods
            return ProxyService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public String getProxyUrl(int id) {
        String result = getProxyOrOfflineUrl(id);
        Log.w(TAG, "Retrieving stream from: " + result);
        return result;

    }

    private String getProxyOrOfflineUrl(int id) {
        return (isAvailableOffline(id)) ?
                getOfflineUrl(id) : getProxy().getProxyUrl(Global.getPlaybackUrl(id));
    }

    private String getOfflineUrl(int id) {
        return ((Track) new Select()
                .from(Track.class)
                .where("track_id = ?", Integer.toString(id))
                .executeSingle())
                .getLocalPath();
    }

    private boolean isAvailableOffline(int id) {
        return new Select()
                .from(Track.class)
                .where("track_id = ?", Integer.toString(id))
                .execute()
                .size() > 0;
    }

    private HttpProxyCacheServer getProxy() {
        return proxy == null ? (proxy = newProxy()) : proxy;
    }

    private HttpProxyCacheServer newProxy() {
        return new HttpProxyCacheServer.Builder(this)
                .maxCacheSize(1024 * 1024 * 1024)
                .build();
    }
}
