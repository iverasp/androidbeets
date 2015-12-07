package no.iegget.androidbeets.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by iver on 30/11/15.
 */
@Table(name = "track")
public class Track extends Model implements Comparable, Serializable {

    @Column(name = "track_id")
    @SerializedName("id")
    int trackId;

    @Column(name = "title")
    String title;
    @Column(name = "albumartist")
    String albumartist;
    @Column(name = "format")
    String format;
    @Column(name = "track")
    int track;
    @Column(name = "tracktotal")
    int tracktotal;
    @Column(name = "samplerate")
    int samplerate;
    @Column(name = "size")
    int size;
    @Column(name = "bitrate")
    int bitrate;
    @Column(name = "bitdepth")
    int bitdepth;

    @Column(name = "local_path")
    private transient String localPath;

    public Track() { super(); }

    public int getTrackId() {
        return trackId;
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

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public boolean isAvailableOffline() {
        return (this.localPath != null && !this.localPath.isEmpty());
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
