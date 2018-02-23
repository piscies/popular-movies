package br.com.marcosaraiva.popularmovies.Model;

/**
 * Created by marco on 21/02/2018.
 * A class that represents a Movie Review
 */

public class Review {
    private String author;
    private String url;
    private String reviewText;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }
}
