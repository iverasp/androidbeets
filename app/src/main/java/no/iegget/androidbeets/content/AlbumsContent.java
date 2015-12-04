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
import no.iegget.androidbeets.models.Artists;
import no.iegget.androidbeets.models.ItunesSearch;
import no.iegget.androidbeets.utils.Global;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class AlbumsContent {

    private static final String TAG = "ArtistContent";

    public static List<AlbumItem> ITEMS;

    //public static final List<String> ITEM_MAP = new ArrayList<>();

    Retrofit retrofitBeats;
    static Retrofit retrofitItunes;
    static AlbumsRecyclerViewAdapter adapter;

    public AlbumsContent(AlbumsRecyclerViewAdapter adapter, String artist) {
        ITEMS = new ArrayList<>();
        this.adapter = adapter;
        OkHttpClient client = new OkHttpClient();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        client.interceptors().add(interceptor);
        retrofitBeats = new Retrofit.Builder()
                .baseUrl(Global.baseUrl)
                //.client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitItunes = new Retrofit.Builder()
                .baseUrl(Global.itunesBaseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        BeetsAPI beetsAPI = retrofitBeats.create(BeetsAPI.class);
        Call<Albums> artists = beetsAPI.loadAlbumsByArtist(artist);
        artists.enqueue(new Callback<Albums>() {
            @Override
            public void onResponse(Response<Albums> response, Retrofit retrofit) {
                for (Album album : response.body().getResults()) {
                    addItem(createAlbumItem(album.getAlbum(), album.getAlbumartist()));
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.w(TAG, t.getLocalizedMessage());
            }
        });
    }

    private static void addItem(final AlbumItem item) {
        ItunesAPI itunesAPI = retrofitItunes.create(ItunesAPI.class);
        Call<ItunesSearch> itunesSearchCall = itunesAPI.searchItunesForAlbum(item.name + "+" + item.albumArtist);
        itunesSearchCall.enqueue(new Callback<ItunesSearch>() {
            @Override
            public void onResponse(Response<ItunesSearch> response, Retrofit retrofit) {
                String url = null;
                try {
                    url = response.body().getResults().get(0).getArtworkUrl60();
                } catch (NullPointerException e) {
                    Log.w(TAG, "response is null!");
                } catch (IndexOutOfBoundsException e) {
                    Log.w(TAG, e.getStackTrace().toString());
                }

                if (url != null) item.setArtworkUrl(url);
                //Log.w(TAG, "added " + item.name);
                if (!ITEMS.contains(item)) {
                    ITEMS.add(item);
                    Collections.sort(ITEMS);
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Throwable t) {
                Log.w(TAG, "FAILURE!");
                ITEMS.add(item);
                Collections.sort(ITEMS);
                adapter.notifyDataSetChanged();
            }
        });


    }

    private static AlbumItem createAlbumItem(String album, String albumArtist) {
        return new AlbumItem(album, albumArtist);
    }

    public static class AlbumItem implements Comparable {
        public final String name;
        public String artworkUrl;
        public final String albumArtist;

        public AlbumItem(String name, String albumArtist) {
            this.name = name;
            this.albumArtist = albumArtist;
        }

        public void setArtworkUrl(String artworkUrl) {
            this.artworkUrl = artworkUrl;
        }

        @Override
        public String toString() {
            return name;
        }

        @Override
        public int compareTo(Object another) {
            return this.name.toLowerCase().compareTo(((AlbumItem)another).name.toLowerCase());
        }

        @Override
        public boolean equals(Object other) {
            return this.name.equals(((AlbumItem)other).name);
        }
    }
}
