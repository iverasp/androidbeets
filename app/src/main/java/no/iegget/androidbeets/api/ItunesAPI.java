package no.iegget.androidbeets.api;

import no.iegget.androidbeets.models.ItunesSearch;
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by iver on 04/12/15.
 */
public interface ItunesAPI {

    @GET("/search?limit=1")
    Call<ItunesSearch> searchItunes(@Query("term") String term);

    @GET("/search?limit=1&entity=album")
    Call<ItunesSearch> searchItunesForAlbum(@Query(value = "term", encoded = true) String term);
}
