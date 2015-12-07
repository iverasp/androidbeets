package no.iegget.androidbeets.content;

import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.List;

import no.iegget.androidbeets.adapters.PlaylistRecyclerViewAdapter;
import no.iegget.androidbeets.models.Playlist;
import no.iegget.androidbeets.models.PlaylistTrack;
import no.iegget.androidbeets.models.Track;

public class PlaylistContent {

    private static final String TAG = "PlaylistContent";

    public List<Track> items;

    static PlaylistRecyclerViewAdapter adapter;
    Playlist playlist;

    public PlaylistContent(PlaylistRecyclerViewAdapter adapter, Playlist playlist) {
        items = new ArrayList<>();
        this.adapter = adapter;
        this.playlist = playlist;
        List<PlaylistTrack> tracks = new Select()
                .from(PlaylistTrack.class)
                .where("playlist = ?", playlist.getId())
                .execute();
        for (PlaylistTrack pt : tracks) {
            items.add(pt.getTrack());
        }
    }
}
