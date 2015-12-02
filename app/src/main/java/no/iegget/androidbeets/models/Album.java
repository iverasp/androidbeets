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

    @Override
    public String toString() {
        return album;
    }
}
