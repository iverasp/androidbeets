package no.iegget.androidbeets.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by iver on 30/11/15.
 */
@Table(name = "playlist")
public class Playlist extends Model implements Serializable {

    @Column(name = "name")
    private String name;

    @Column(name = "offline")
    private boolean availableOffline;

    public Playlist() { super(); }

    public Playlist(String name) {
        super();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<Track> getTracks() {
        List<PlaylistTrack> playlistTracks = new Select()
                .from(PlaylistTrack.class)
                .where("id = ?", this.getId())
                .execute();
        List<Track> tracks = new ArrayList<>();
        for (PlaylistTrack pt : playlistTracks) {
            tracks.add(pt.getTrack());
        }
        return tracks;
    }

    public void addTrack(Track track) {
        PlaylistTrack pt = new PlaylistTrack(track, this);
        pt.save();
    }

    public void removeTrack(Track track) {
        PlaylistTrack pt = new Select()
                .from(PlaylistTrack.class)
                .where("track.id = ?", track.getId())
                .where("playlist.id = ?", this.getId())
                .executeSingle();
        pt.delete();
    }

    public String getArtworkUrl() {
        return null;
    }

    public boolean isAvailableOffline() {
        return true;
    }
}
