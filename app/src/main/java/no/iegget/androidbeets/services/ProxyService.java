package no.iegget.androidbeets.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.danikula.videocache.HttpProxyCacheServer;

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

    public String getProxyUrl(String url) {
        Log.w(TAG, getProxy().getProxyUrl(url));
        return getProxy().getProxyUrl(url);
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
