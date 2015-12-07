package no.iegget.androidbeets.content;

import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.List;

import no.iegget.androidbeets.adapters.PlaylistsRecyclerViewAdapter;
import no.iegget.androidbeets.models.Playlist;

public class PlaylistsContent {

    private static final String TAG = "PlaylistsContent";

    public List<Playlist> items;

    private PlaylistsRecyclerViewAdapter adapter;

    public PlaylistsContent(PlaylistsRecyclerViewAdapter adapter) {
        this.adapter = adapter;
        items = new ArrayList<>();
        updateItems();
    }

    public void updateItems() {
        List<Playlist> playlists = new Select()
                .from(Playlist.class)
                .execute();
        items.clear();
        items.addAll(playlists);
        adapter.notifyDataSetChanged();
    }

}
