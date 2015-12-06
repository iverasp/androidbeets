package no.iegget.androidbeets.models;

/**
 * Created by iver on 30/11/15.
 */
public class Artist implements Comparable {

    int id;
    String name;

    private transient String artworkUrl;

    public Artist(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setArtworkUrl(String artworkUrl) {
        this.artworkUrl = artworkUrl;
    }

    public String getArtworkUrl() {
        return this.artworkUrl;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int compareTo(Object another) {
        return this.name.toLowerCase().compareTo(((Artist)another).name.toLowerCase());
    }

    @Override
    public boolean equals(Object other) {
        return this.name.equals(((Artist)other).name);
    }
}
