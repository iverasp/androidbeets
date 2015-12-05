package no.iegget.androidbeets.content;

import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import no.iegget.androidbeets.adapters.ArtistsRecyclerViewAdapter;
import no.iegget.androidbeets.api.BeetsAPI;
import no.iegget.androidbeets.api.ItunesAPI;
import no.iegget.androidbeets.models.Artists;
import no.iegget.androidbeets.models.ItunesSearch;
import no.iegget.androidbeets.utils.BaseUrl;
import no.iegget.androidbeets.utils.Global;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class ArtistsContent {

    private static final String TAG = "ArtistContent";

    public static final List<ArtistItem> ITEMS = new ArrayList<>();

    //public static final List<String> ITEM_MAP = new ArrayList<>();

    Retrofit retrofitBeats;
    static Retrofit retrofitItunes;
    static ArtistsRecyclerViewAdapter adapter;

    public ArtistsContent(ArtistsRecyclerViewAdapter adapter) {
        this.adapter = adapter;
        OkHttpClient client = new OkHttpClient();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        client.interceptors().add(interceptor);
        retrofitBeats = new Retrofit.Builder()
                .baseUrl(BaseUrl.baseUrl)
                //.client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitItunes = new Retrofit.Builder()
                .baseUrl(Global.itunesBaseUrl)
                //.client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        BeetsAPI beetsAPI = retrofitBeats.create(BeetsAPI.class);
        Call<Artists> artists = beetsAPI.loadArtists();
        artists.enqueue(new Callback<Artists>() {
            @Override
            public void onResponse(Response<Artists> response, Retrofit retrofit) {
                for (String artist : response.body().getResults()) {
                    if (artist != "") addItem(createArtistItem(artist));
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.w(TAG, t.getLocalizedMessage());
            }
        });
    }

    private static void addItem(final ArtistItem item) {
        ItunesAPI itunesAPI = retrofitItunes.create(ItunesAPI.class);
        Call<ItunesSearch> itunesSearchCall = itunesAPI.searchItunes(item.name);
        itunesSearchCall.enqueue(new Callback<ItunesSearch>() {
            @Override
            public void onResponse(Response<ItunesSearch> response, Retrofit retrofit) {
                String url = null;
                try {
                    url = response.body().getResults().get(0).getArtworkUrl60();
                } catch (NullPointerException e) {
                    Log.w(TAG, "response is null!");
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

    private static ArtistItem createArtistItem(String artist) {
        return new ArtistItem(artist);
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    public static class ArtistItem implements Comparable {
        public final String name;
        public String artworkUrl;

        public ArtistItem(String name) {
            this.name = name;
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
            return this.name.toLowerCase().compareTo(((ArtistItem)another).name.toLowerCase());
        }

        @Override
        public boolean equals(Object other) {
            return this.name.equals(((ArtistItem)other).name);
        }
    }
}
