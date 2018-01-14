package br.com.marcosaraiva.popularmovies.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * This class was created to represent a movie.
 * It also implements Parcelable because it has to be sent to MovieDetailsActivity
 */

public class Movie implements Parcelable {
    public static final String INTENT_EXTRA_MOVIE = "EXTRAMOVIE";
    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel parcel) {
            return new Movie(parcel);
        }

        @Override
        public Movie[] newArray(int i) {
            return new Movie[i];
        }
    };
    private String title;
    private String overview;
    private String posterRelativePath;
    private double voteAverage;
    private String releaseDate;

    public Movie() {
    }

    /*STARTING Parcelable methods*/
    private Movie(Parcel in) {
        title = in.readString();
        overview = in.readString();
        posterRelativePath = in.readString();
        voteAverage = in.readDouble();
        releaseDate = in.readString();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String newName) {
        title = newName;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String newOveriew) {
        overview = newOveriew;
    }

    public String getPosterRelativePath() {
        return posterRelativePath;
    }

    public void setPosterRelativePath(String newPosterRelativePath) {
        posterRelativePath = newPosterRelativePath;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double newVoteAverage) {
        voteAverage = newVoteAverage;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String newReleaseDate) {
        releaseDate = newReleaseDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(overview);
        parcel.writeString(posterRelativePath);
        parcel.writeDouble(voteAverage);
        parcel.writeString(releaseDate);
    }
    /*ENDING Parcelable methods*/
}
