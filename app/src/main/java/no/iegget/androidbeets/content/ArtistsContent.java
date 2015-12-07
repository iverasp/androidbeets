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
import no.iegget.androidbeets.models.Artist;
import no.iegget.androidbeets.models.Artists;
import no.iegget.androidbeets.models.ItunesSearch;
import no.iegget.androidbeets.utils.BaseUrl;
import no.iegget.androidbeets.utils.Global;
import no.iegget.androidbeets.utils.PreferencesManager;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class ArtistsContent {

    private static final String TAG = "ArtistContent";

    public static List<Artist> items;

    //public static final List<String> ITEM_MAP = new ArrayList<>();

    Retrofit retrofitBeats;
    static Retrofit retrofitItunes;
    static ArtistsRecyclerViewAdapter adapter;

    public ArtistsContent(ArtistsRecyclerViewAdapter adapter) {
        items  = new ArrayList<>();
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
        Call<Artists> artists = beetsAPI.loadArtists();
        artists.enqueue(new Callback<Artists>() {
            @Override
            public void onResponse(Response<Artists> response, Retrofit retrofit) {
                for (String artist : response.body().getResults()) {
                    if (!artist.equals("")) addItem(new Artist(artist));
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.w(TAG, t.getLocalizedMessage());
            }
        });
    }

    private static void addItem(final Artist item) {
        ItunesAPI itunesAPI = retrofitItunes.create(ItunesAPI.class);
        Call<ItunesSearch> itunesSearchCall = itunesAPI.searchItunes(item.getName());
        itunesSearchCall.enqueue(new Callback<ItunesSearch>() {
            @Override
            public void onResponse(Response<ItunesSearch> response, Retrofit retrofit) {
                String url = null;
                try {
                    url = response.body().getResults().get(0).getArtworkUrl60();
                } catch (NullPointerException e) {
                    Log.w(TAG, "response is null!");
                } catch (IndexOutOfBoundsException e) {
                    Log.w(TAG, "out of bounds");
                }

                if (url != null) item.setArtworkUrl(url);
                //Log.w(TAG, "added " + item.name);
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
