package no.iegget.androidbeets.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by iver on 07/12/15.
 */
@Table(name = "playlist_track")
public class PlaylistTrack extends Model {

    @Column(name = "track")
    private Track track;

    @Column(name = "playlist")
    private Playlist playlist;

    @Column(name = "position")
    private int position;

    public PlaylistTrack() { super(); }

    public PlaylistTrack(Track track, Playlist playlist) {
        this.track = track;
        this.playlist = playlist;
    }

    public Track getTrack() {
        return track;
    }

    public Playlist getPlaylist() {
        return playlist;
    }
}
