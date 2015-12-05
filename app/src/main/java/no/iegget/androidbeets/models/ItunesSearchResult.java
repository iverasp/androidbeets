package no.iegget.androidbeets.models;

/**
 * Created by iver on 04/12/15.
 */
public class ItunesSearchResult {

    String artworkUrl60;

    public String getArtworkUrl60() {
        return artworkUrl60;
    }

    public String getArtworkUrl(int size) {
        String base = artworkUrl60.substring(0, artworkUrl60.lastIndexOf("/source/"));
        return base + "/source/" + size + "x" + size + "bb.jpg";
    }
}
