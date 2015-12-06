package no.iegget.androidbeets.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.List;

/**
 * Created by iver on 30/11/15.
 */
@Table(name = "playlist")
public class Playlist extends Model {

    @Column(name = "name")
    private String name;
    @Column(name = "tracks")
    private List<Track> tracks;

    public Playlist() { super(); }

    public Playlist(String name) {
        super();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<Track> getTracks() {
        return tracks;
    }

    public void addTrack(Track track) {
        tracks.add(track);
    }

    public void removeTrack(Track track) {
        tracks.remove(track);
    }
}
