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
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class AlbumContent {

    private static final String TAG = "AlbumContent";

    public List<Track> items;

    Retrofit retrofitBeats;
    static AlbumRecyclerViewAdapter adapter;
    Album album;

    public AlbumContent(AlbumRecyclerViewAdapter adapter, Album album) {
        items = new ArrayList<>();
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
                    addItem(track);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.w(TAG, t.getLocalizedMessage());
            }
        });
    }

    private void addItem(final Track item) {
        Log.w(TAG, "added " + item.getTitle());
        if (!items.contains(item)) {
            items.add(item);
            Collections.sort(items);
            adapter.notifyDataSetChanged();
        }
    }
}
