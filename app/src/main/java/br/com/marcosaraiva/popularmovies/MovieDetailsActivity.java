package br.com.marcosaraiva.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.marcosaraiva.popularmovies.AsyncTasks.AsyncTaskExtendedInterface;
import br.com.marcosaraiva.popularmovies.AsyncTasks.FetchReviewsFromMoviesDb_Task;
import br.com.marcosaraiva.popularmovies.AsyncTasks.FetchTrailersFromMoviesDb_Task;
import br.com.marcosaraiva.popularmovies.Model.Movie;
import br.com.marcosaraiva.popularmovies.Model.Review;
import br.com.marcosaraiva.popularmovies.Model.Trailer;
import br.com.marcosaraiva.popularmovies.Utilities.NetworkUtilities;

public class MovieDetailsActivity
        extends AppCompatActivity
        implements TrailerListAdapter.TrailerListAdapterOnClickHandler,
        ReviewListAdapter.ReviewListAdapterOnClickHandler,
        AsyncTaskExtendedInterface {

    private final String ERROR_TAG = "MOVIE_DETAILS_ACTIVITY";

    private TextView mdMovieTitleTextView;
    private TextView mdMovieOverviewTextView;
    private TextView mdMovieAverageRatingTextView;
    private TextView mdMovieReleaseDateTextView;
    private ImageView mdMoviePosterImageView;

    private RecyclerView mdTrailersRecyclerView;
    private TrailerListAdapter mdTrailerListAdapter;

    private RecyclerView mdReviewsRecyclerView;
    private ReviewListAdapter mdReviewListAdapter;

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

        //Trailer List adapter
        mdTrailerListAdapter = new TrailerListAdapter(this, this);

        //Gets the trailer list recycler view and configures it
        mdTrailersRecyclerView = findViewById(R.id.rv_trailers);
        mdTrailersRecyclerView.setAdapter(mdTrailerListAdapter);

        RecyclerView.LayoutManager trailerLayoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mdTrailersRecyclerView.setLayoutManager(trailerLayoutManager);

        //Review List adapter
        mdReviewListAdapter = new ReviewListAdapter(this, this);

        //Gets the review list recycler view and configures it
        mdReviewsRecyclerView = findViewById(R.id.rv_reviews);
        mdReviewsRecyclerView.setAdapter(mdReviewListAdapter);

        RecyclerView.LayoutManager reviewLayoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mdReviewsRecyclerView.setLayoutManager(reviewLayoutManager);

        //Loads the trailers list
        loadTrailers(detailedMovie.getMovieId());
        loadReviews(detailedMovie.getMovieId());
    }

    private void loadTrailers(long movieId) {

        new FetchTrailersFromMoviesDb_Task(this).execute(movieId);
    }

    private void loadReviews(long movieId) {

        new FetchReviewsFromMoviesDb_Task(this).execute(movieId);
    }

    @Override
    public void onTrailerClick(Trailer clickedTrailer) {
        Intent youtubeIntent = new Intent(Intent.ACTION_VIEW);

        //First tries to open video in Youtube App
        Uri youtubeAppUri = Uri.parse(String.format("vnd.youtube:%s", clickedTrailer.getKey()));
        youtubeIntent.setData(youtubeAppUri);

        if (youtubeIntent.resolveActivity(getPackageManager()) != null)
            startActivity(youtubeIntent);
        else {
            //Tries web if app is not present
            youtubeAppUri = Uri.parse(String.format("http://www.youtube.com/watch?v=%s", clickedTrailer.getKey()));
            youtubeIntent.setData(youtubeAppUri);

            if (youtubeIntent.resolveActivity(getPackageManager()) != null)
                startActivity(youtubeIntent);
            else {
                Toast toast = Toast.makeText(this, getString(R.string.toast_error_message), Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }

    @Override
    public void onReviewClick(Review clickedReview) {
        Intent webIntent = new Intent(Intent.ACTION_VIEW);

        //Tries to open the web page
        Uri reviewUri = Uri.parse(clickedReview.getUrl());
        webIntent.setData(reviewUri);

        if (webIntent.resolveActivity(getPackageManager()) != null)
            startActivity(webIntent);
        else {
            Toast toast = Toast.makeText(this, getString(R.string.toast_error_message), Toast.LENGTH_LONG);
            toast.show();
        }
    }

    @Override
    public void onTaskCompleted(Object result, String task) {
        if (result == null) {
            Toast errorToast = Toast.makeText(getApplicationContext(), R.string.toast_error_message, Toast.LENGTH_LONG);
            errorToast.show();
        }

        switch (task) {
            case FetchTrailersFromMoviesDb_Task.TASK_NAME:
                mdTrailerListAdapter.setTrailerList((List<Trailer>) result);
                break;
            case FetchReviewsFromMoviesDb_Task.TASK_NAME:
                mdReviewListAdapter.setReviewList((List<Review>) result);
                break;
            default:
                throw new IllegalArgumentException("Unknown task name");
        }
    }
}
