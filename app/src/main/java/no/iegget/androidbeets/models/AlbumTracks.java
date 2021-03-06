package no.iegget.androidbeets.models;

import java.util.List;

/**
 * Created by iver on 04/12/15.
 */
public class AlbumTracks {

    List<Track> results;

    public List<Track> getResults() {
        return results;
    }

    @Override
    public String toString() {
        String result = "";
        for (Track track : results) {
            result += track.toString() + "\n";
        }
        return result;
    }
}
