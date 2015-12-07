package no.iegget.androidbeets.content;

import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import no.iegget.androidbeets.adapters.AlbumsRecyclerViewAdapter;
import no.iegget.androidbeets.api.BeetsAPI;
import no.iegget.androidbeets.api.ItunesAPI;
import no.iegget.androidbeets.models.Album;
import no.iegget.androidbeets.models.Albums;
import no.iegget.androidbeets.models.ItunesSearch;
import no.iegget.androidbeets.utils.BaseUrl;
import no.iegget.androidbeets.utils.Global;
import no.iegget.androidbeets.utils.PreferencesManager;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class AlbumsContent {

    private static final String TAG = "AlbumsContent";

    public static List<Album> items;
    Retrofit retrofitBeats;
    static Retrofit retrofitItunes;
    static AlbumsRecyclerViewAdapter adapter;

    public AlbumsContent(AlbumsRecyclerViewAdapter adapter, String artist) {
        items = new ArrayList<>();
        this.adapter = adapter;
        OkHttpClient client = new OkHttpClient();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        client.interceptors().add(interceptor);
        retrofitBeats = new Retrofit.Builder()
                .baseUrl(PreferencesManager.getInstance().getBaseUrl())
                //.client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitItunes = new Retrofit.Builder()
                .baseUrl(Global.itunesBaseUrl)
                //.client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        BeetsAPI beetsAPI = retrofitBeats.create(BeetsAPI.class);
        Call<Albums> artists = beetsAPI.loadAlbumsByArtist(artist);
        artists.enqueue(new Callback<Albums>() {
            @Override
            public void onResponse(Response<Albums> response, Retrofit retrofit) {
                for (Album album : response.body().getResults()) addItem(album);
            }

            @Override
            public void onFailure(Throwable t) {
                Log.w(TAG, t.getLocalizedMessage());
            }
        });
    }

    private static void addItem(final Album item) {
        ItunesAPI itunesAPI = retrofitItunes.create(ItunesAPI.class);
        Call<ItunesSearch> itunesSearchCall = itunesAPI.searchItunesForAlbum(item.getAlbum() + "+" + item.getAlbumartist());
        itunesSearchCall.enqueue(new Callback<ItunesSearch>() {
            @Override
            public void onResponse(Response<ItunesSearch> response, Retrofit retrofit) {
                String url = null;
                try {
                    url = response.body().getResults().get(0).getArtworkUrl(200);
                } catch (Exception e) {
                    Log.w(TAG, e.getMessage());
                }

                if (url != null) item.setArtworkUrl(url);
                if (!items.contains(item)) {
                    items.add(item);
                    Collections.sort(items);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.w(TAG, "FAILURE!");
                items.add(item);
                Collections.sort(items);
                adapter.notifyDataSetChanged();
            }
        });


    }
}
