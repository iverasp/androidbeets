package no.iegget.androidbeets.models;

/**
 * Created by iver on 30/11/15.
 */
public class Track implements Comparable {

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

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAlbumartist() {
        return albumartist;
    }

    public String getFormat() {
        return format;
    }

    public int getTrack() {
        return track;
    }

    public int getTracktotal() {
        return tracktotal;
    }

    public int getSamplerate() {
        return samplerate;
    }

    public int getSize() {
        return size;
    }

    public int getBitrate() {
        return bitrate;
    }

    public int getBitdepth() {
        return bitdepth;
    }

    @Override
    public int compareTo(Object another) {
        return this.track - ((Track)another).track;
    }

    @Override
    public boolean equals(Object other) {
        return this.title.equals(((Track)other).title);
    }

    @Override
    public String toString() {
        return "<" + title + ", " + albumartist + ">";
    }
}
