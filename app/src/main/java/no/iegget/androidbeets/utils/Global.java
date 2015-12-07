package no.iegget.androidbeets.utils;

/**
 * Created by iver on 04/12/15.
 */
public class Global {

    public static final String getPlaybackUrl(int id) {
        return BaseUrl.baseUrl + "/item/" + id + "/transcode?coding=mp3&bitrate=192";
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

    public final static String ACTION_PLAY = "no.iegget.androidbeets.PLAY";
}
