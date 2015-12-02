package no.iegget.androidbeets.models;

/**
 * Created by iver on 30/11/15.
 */
public class Track {

    int id;
    String title;
    String albumartist;
    String format;
    int track;
    int tracktotal;
    int samplerate;
    int size;
    int bitrate;
    int bitdepth;

    @Override
    public String toString() {
        return "<" + title + ", " + albumartist + ">";
    }
}
