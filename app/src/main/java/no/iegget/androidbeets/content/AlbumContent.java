package no.iegget.androidbeets.content;

import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import no.iegget.androidbeets.adapters.AlbumRecyclerViewAdapter;
import no.iegget.androidbeets.api.BeetsAPI;
import no.iegget.androidbeets.models.Album;
import no.iegget.androidbeets.models.AlbumTracks;
import no.iegget.androidbeets.models.Track;
import no.iegget.androidbeets.utils.BaseUrl;
import no.iegget.androidbeets.utils.Global;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class AlbumContent {

    private static final String TAG = "AlbumContent";

    public List<TrackItem> ITEMS;

    Retrofit retrofitBeats;
    static AlbumRecyclerViewAdapter adapter;
    Album album;

    public AlbumContent(AlbumRecyclerViewAdapter adapter, Album album) {
        ITEMS = new ArrayList<>();
        this.adapter = adapter;
        this.album = album;
        OkHttpClient client = new OkHttpClient();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        client.interceptors().add(interceptor);
        retrofitBeats = new Retrofit.Builder()
                .baseUrl(BaseUrl.baseUrl)
                //.client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        BeetsAPI beetsAPI = retrofitBeats.create(BeetsAPI.class);
        Call<AlbumTracks> tracksCall = beetsAPI.loadAlbumTracks(album.getAlbumartist(), album.getAlbum());
        tracksCall.enqueue(new Callback<AlbumTracks>() {
            @Override
            public void onResponse(Response<AlbumTracks> response, Retrofit retrofit) {
                for (Track track : response.body().getResults()) {
                    addItem(createTrackItem(track));
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.w(TAG, t.getLocalizedMessage());
            }
        });
    }

    private void addItem(final TrackItem item) {
        Log.w(TAG, "added " + item.title);
        if (!ITEMS.contains(item)) {
            ITEMS.add(item);
            Collections.sort(ITEMS);
            adapter.notifyDataSetChanged();
        }
    }

    private static TrackItem createTrackItem(Track track) {
        return new TrackItem(track.getTitle(), track.getTrack(), track.getId());
    }

    public static class TrackItem implements Comparable {
        public final String title;
        public final int number;
        public final int id;

        public TrackItem(String name, int number, int id) {
            this.title = name;
            this.number = number;
            this.id = id;
        }

        @Override
        public String toString() {
            return title;
        }

        @Override
        public int compareTo(Object another) {
            return this.number - ((TrackItem)another).number;
        }

        @Override
        public boolean equals(Object other) {
            return this.title.equals(((TrackItem)other).title);
        }
    }
}
