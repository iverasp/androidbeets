package no.iegget.androidbeets.models;

/**
 * Created by iver on 30/11/15.
 */
public class Artist {

    int id;
    String name;

    public Artist(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
