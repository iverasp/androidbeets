package no.iegget.androidbeets.utils;

/**
 * Created by iver on 04/12/15.
 */
public class Global {

    public static final String getPlaybackUrl(String baseUrl, int id, String bitrate) {
        if (Integer.valueOf(bitrate) == 9000) return baseUrl + "/item/" + id + "/file";
        return baseUrl + "/item/" + id + "/transcode?coding=mp3&bitrate=" + bitrate;
    }

    public static final String itunesBaseUrl = "https://itunes.apple.com";

    public static final String ALBUM_TITLE = "no.iegget.androidbeets.ALBUM_TITLE";
    public static final String ALBUM_ARTIST = "no.iegget.androidbeets.ALBUM_ARTIST";
    public static final String ARTIST = "no.iegget.androidbeets.ARTIST";
    public static final String ALBUM_ART_URL = "no.iegget.androidbeets.ALBUM_ARTWORK_URL";
    public static final String TRACK_TITLE = "no.iegget.androidbeets.TRACK_TITLE";
    public static final String TRACK_ID = "no.iegget.androidbeets.TRACK_ID";
    public static final String TRACK = "no.iegget.androidbeets.TRACK";
    public static final String PLAYLIST = "no.iegget.androidbeets.PLAYLIST";

    public final static String PREFERENCES_KEY = "no.iegget.androidbeets.preferences";
    public final static String SERVER_URL = "no.iegget.androidbeets.SERVER_URL";
    public final static String SERVER_PORT = "no.iegget.androidbeets.SERVER_PORT";
    public final static String SERVER_USERNAME = "no.iegget.androidbeets.SERVER_USERNAME";
    public final static String SERVER_PASSWORD = "no.iegget.androidbeets.SERVER_PASSWORD";
    public final static String STREAMING_QUALITY = "no.iegget.androidbeets.STREAMING_QUALITY";
    public final static String OFFLINE_QUALITY = "no.iegget.androidbeets.OFFLINE_QUALITY";
    public final static String DOWNLOAD_CELLULAR = "no.iegget.androidbeets.DOWNLOAD_CELLULAR";
}
