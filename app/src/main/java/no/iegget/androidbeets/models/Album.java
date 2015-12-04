package no.iegget.androidbeets.models;

import java.util.List;

/**
 * Created by iver on 30/11/15.
 */
public class Album {

    int id;
    String album;
    String albumartist;
    //List<Track> tracks;

    public Album(String albumArtist, String albumTitle) {
        this.albumartist = albumArtist;
        this.album = albumTitle;
    }

    @Override
    public String toString() {
        return album;
    }

    public int getId() {
        return id;
    }

    public String getAlbum() {
        return album;
    }

    public String getAlbumartist() {
        return albumartist;
    }
}
