package br.com.marcosaraiva.popularmovies.Model;

import android.os.Parcel;
import android.os.Parcelable;
/**
 * This class was created to represent a movie.
 * It also implements Parcelable because it has to be sent to MovieDetailsActivity
 */

public class Movie implements Parcelable {
    public static final String INTENT_EXTRA_MOVIE = "EXTRAMOVIE";

    private String title;
    public void setTitle(String newName)
    {
        title = newName;
    }
    public String getTitle()
    {
        return title;
    }

    private String overview;
    public void setOverview(String newOveriew)
    {
        overview = newOveriew;
    }
    public String getOverview()
    {
        return overview;
    }

    private String posterRelativePath;
    public void setPosterRelativePath(String newPosterRelativePath)
    {
        posterRelativePath = newPosterRelativePath;
    }
    public String getPosterRelativePath()
    {
        return posterRelativePath;
    }

    private String backdropRelativePath;
    public void setBackdropRelativePath(String newBackdropRelativePath)
    {
        backdropRelativePath = newBackdropRelativePath;
    }
    public String getBackdropRelativePath()
    {
        return backdropRelativePath;
    }

    private double voteAverage;
    public void setVoteAverage(double newVoteAverage)
    {
        voteAverage = newVoteAverage;
    }
    public double getVoteAverage()
    {
        return voteAverage;
    }

    private String releaseDate;
    public void setReleaseDate(String newReleaseDate)
    {
        releaseDate = newReleaseDate;
    }
    public String getReleaseDate()
    {
        return releaseDate;
    }

    public Movie(){}

    /*STARTING Parcelable methods*/
    private Movie(Parcel in)
    {
        title = in.readString();
        overview = in.readString();
        posterRelativePath = in.readString();
        backdropRelativePath = in.readString();
        voteAverage = in.readDouble();
        releaseDate = in.readString();
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
        parcel.writeString(backdropRelativePath);
        parcel.writeDouble(voteAverage);
        parcel.writeString(releaseDate);
    }

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
    /*ENDING Parcelable methods*/
}
