package br.com.marcosaraiva.popularmovies.Model;

/**
 * Created by marco on 20/02/2018.
 * This class will represent a Youtube Trailer for the movie
 */

public class Trailer {

    private String id;
    private String key;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
