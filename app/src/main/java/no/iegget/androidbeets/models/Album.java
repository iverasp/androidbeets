package no.iegget.androidbeets.models;

import java.util.List;

/**
 * Created by iver on 30/11/15.
 */
public class Album implements Comparable {

    int id;
    String album;
    String albumartist;
    int year;
    String genre;

    private transient String artworkUrl;

    public Album(String albumArtist, String albumTitle, String artworkUrl) {
        this.albumartist = albumArtist;
        this.album = albumTitle;
        this.artworkUrl = artworkUrl;
    }

    @Override
    public String toString() {
        return album;
    }

    public int getId() {
        return id;
    }

    public String getAlbum() {
        return album;
    }

    public String getAlbumartist() {
        return albumartist;
    }

    public int getYear() {
        return year;
    }

    public String getGenre() {
        return genre;
    }

    public void setArtworkUrl(String url) {
        this.artworkUrl = url;
    }

    public String getArtworkUrl(int size) {
        String base = artworkUrl.substring(0, artworkUrl.lastIndexOf("/source/"));
        return base + "/source/" + size + "x" + size + "bb.jpg";
    }

    @Override
    public int compareTo(Object another) {
        return this.album.toLowerCase().compareTo(((Album)another).album.toLowerCase());
    }

    @Override
    public boolean equals(Object other) {
        return this.album.equals(((Album)other).album);
    }
}
