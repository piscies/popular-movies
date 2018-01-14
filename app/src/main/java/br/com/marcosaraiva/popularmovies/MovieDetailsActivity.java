package br.com.marcosaraiva.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import br.com.marcosaraiva.popularmovies.Model.Movie;
import br.com.marcosaraiva.popularmovies.Utilities.NetworkUtilities;

public class MovieDetailsActivity extends AppCompatActivity {

    private TextView mdMovieTitleTextView;
    private TextView mdMovieOverviewTextView;
    private TextView mdMovieAverageRatingTextView;
    private TextView mdMovieReleaseDateTextView;
    private ImageView mdMoviePosterImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        mdMovieTitleTextView = findViewById(R.id.tv_movie_details_title);
        mdMovieOverviewTextView = findViewById(R.id.tv_movie_details_overview);
        mdMovieAverageRatingTextView = findViewById(R.id.tv_movie_details_rating);
        mdMovieReleaseDateTextView = findViewById(R.id.tv_movie_details_release_date);
        mdMoviePosterImageView = findViewById(R.id.iv_movie_details_poster);

        //Finds the movie in the Intent
        Movie detailedMovie = getIntent().getParcelableExtra(Movie.INTENT_EXTRA_MOVIE);

        //Displays the details
        mdMovieTitleTextView.setText(detailedMovie.getTitle());
        mdMovieOverviewTextView.setText(detailedMovie.getOverview());
        mdMovieAverageRatingTextView.setText(Double.toString(detailedMovie.getVoteAverage()));
        mdMovieReleaseDateTextView.setText(detailedMovie.getReleaseDate());

        String posterURL = NetworkUtilities.MOVIEDB_IMAGE_500_URL + detailedMovie.getPosterRelativePath();
        Picasso.with(this).load(posterURL).into(mdMoviePosterImageView);
    }
}
