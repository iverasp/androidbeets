package no.iegget.androidbeets.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by iver on 07/12/15.
 */
public class PreferencesManager implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static PreferencesManager sInstance;
    private final SharedPreferences mPref;

    private PreferencesManager(Context context) {
        mPref = context.getSharedPreferences(Global.PREFERENCES_KEY, Context.MODE_PRIVATE);
    }

    public static synchronized void initializeInstance(Context context) {
        if (sInstance == null) {
            sInstance = new PreferencesManager(context);
        }
    }

    public static synchronized PreferencesManager getInstance() {
        if (sInstance == null) {
            throw new IllegalStateException(PreferencesManager.class.getSimpleName() +
                    " is not initialized, call initializeInstance(..) method first.");
        }
        return sInstance;
    }

    public String getServerAddress() {
        return mPref.getString(Global.SERVER_URL, "");
    }

    public String getServerPort() {
        return mPref.getString(Global.SERVER_PORT, "");
    }

    public String getServerUsername() {
        return mPref.getString(Global.SERVER_USERNAME, "");
    }

    public String getServerPassword() {
        return mPref.getString(Global.SERVER_PASSWORD, "");
    }

    public String getOfflineQuality() {
        return mPref.getString(Global.OFFLINE_QUALITY, "");
    }

    public String getStreamingQuality() {
        return mPref.getString(Global.STREAMING_QUALITY, "");
    }

    public boolean getDownloadWhileOnCellular() {
        return mPref.getBoolean(Global.DOWNLOAD_CELLULAR, false);
    }

    public String getBaseUrl() {
        String protocol = getServerAddress().substring(0, getServerAddress().indexOf("//")) + "//";
        String address = getServerAddress().substring(getServerAddress().indexOf("//") + 2, getServerAddress().length());
        return protocol + getServerUsername() + ":" + getServerPassword() + "@" + address + ":" + getServerPort();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }
}
