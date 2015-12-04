package no.iegget.androidbeets.api;

import no.iegget.androidbeets.models.Album;
import no.iegget.androidbeets.models.AlbumSearch;
import no.iegget.androidbeets.models.AlbumTracks;
import no.iegget.androidbeets.models.Albums;
import no.iegget.androidbeets.models.AllTracks;
import no.iegget.androidbeets.models.ArtistAlbumsSearch;
import no.iegget.androidbeets.models.Artists;
import no.iegget.androidbeets.models.Stats;
import no.iegget.androidbeets.models.Track;
import no.iegget.androidbeets.models.TrackSearch;
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by iver on 30/11/15.
 */
public interface BeetsAPI {

    @GET("/item/{id}")
    Call<Track> loadTrack(@Path("id") String id);

    @GET("/item/")
    Call<AllTracks> loadAllTracks();

    @GET("/item/query/{query}")
    Call<TrackSearch> searchTracks(@Path("query") String query);

    @GET("/album/query/{query}")
    Call<AlbumSearch> searchAlbums(@Path("query") String query);

    @GET("/album/{id}")
    Call<Album> loadAlbum(@Path("id") String id);

    @GET("item/query/albumartist:{artist}/album:{album}")
    Call<AlbumTracks> loadAlbumTracks(@Path("artist") String artist, @Path("album") String album);

    @GET("/artist/")
    Call<Artists> loadArtists();

    @GET("/album/artist/{query}")
    Call<ArtistAlbumsSearch> searchArtistAlbums(@Path("query") String query);

    @GET("/stats")
    Call<Stats> loadStats();

    @GET("/album/query/albumartist:{artist}")
    Call<Albums> loadAlbumsByArtist(@Path("artist") String artist);

}
