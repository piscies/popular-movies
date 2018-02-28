package br.com.marcosaraiva.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.marcosaraiva.popularmovies.AsyncTasks.AsyncTaskExtendedInterface;
import br.com.marcosaraiva.popularmovies.AsyncTasks.FetchMovieDetailFromMoviesDb_Task;
import br.com.marcosaraiva.popularmovies.AsyncTasks.FetchReviewsFromMoviesDb_Task;
import br.com.marcosaraiva.popularmovies.AsyncTasks.FetchTrailersFromMoviesDb_Task;
import br.com.marcosaraiva.popularmovies.Database.PopularMoviesContract;
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
    private TextView mdMovieRuntimeTextView;
    private ImageView mdMoviePosterImageView;
    private ImageButton mdFavoriteIconImageButton;

    private RecyclerView mdTrailersRecyclerView;
    private TrailerListAdapter mdTrailerListAdapter;

    private RecyclerView mdReviewsRecyclerView;
    private ReviewListAdapter mdReviewListAdapter;

    private Movie mdMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        mdMovieTitleTextView = findViewById(R.id.tv_movie_details_title);
        mdMovieOverviewTextView = findViewById(R.id.tv_movie_details_overview);
        mdMovieAverageRatingTextView = findViewById(R.id.tv_movie_details_rating);
        mdMovieReleaseDateTextView = findViewById(R.id.tv_movie_details_release_date);
        mdMoviePosterImageView = findViewById(R.id.iv_movie_details_poster);
        mdFavoriteIconImageButton = findViewById(R.id.ib_set_favorite);
        mdMovieRuntimeTextView = findViewById(R.id.tv_movie_details_runtime);

        //Finds the movie in the Intent
        mdMovie = getIntent().getParcelableExtra(Movie.INTENT_EXTRA_MOVIE);

        //If movie isn't favorite, the details weren't saved in the DB. Gotta fetch them.
        if(!mdMovie.isFavorite()){
            loadMovieDetails(mdMovie.getMovieId());
        }
        else
            setMovieDetailsInUI();

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

        setFavoriteIcon();

        //Loads the trailers and reviews list
        loadTrailers(mdMovie.getMovieId());
        loadReviews(mdMovie.getMovieId());
    }

    private void loadMovieDetails(long movieId) {

        new FetchMovieDetailFromMoviesDb_Task(this).execute(movieId);
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
            case FetchMovieDetailFromMoviesDb_Task.TASK_NAME:
                mdMovie = (Movie) result;
                setMovieDetailsInUI();
                break;
            default:
                throw new IllegalArgumentException("Unknown task name");
        }
    }

    private void setMovieDetailsInUI(){
        mdMovieTitleTextView.setText(mdMovie.getTitle());
        mdMovieOverviewTextView.setText(mdMovie.getOverview());
        mdMovieAverageRatingTextView.setText(Double.toString(mdMovie.getVoteAverage()));
        mdMovieReleaseDateTextView.setText(mdMovie.getReleaseDate());
        mdMovieRuntimeTextView.setText(Integer.toString(mdMovie.getRuntime()));

        String posterURL = NetworkUtilities.MOVIEDB_IMAGE_500_URL + mdMovie.getPosterRelativePath();
        Picasso.with(this).load(posterURL).into(mdMoviePosterImageView);
    }

    public void saveOrRemoveFavoriteMovie(View view){
        Uri contentUri = PopularMoviesContract.FavoriteMovieEntry.CONTENT_URI;

        //TODO: Needs to run asynchronously? (probably)
        if(mdMovie.isFavorite()){
            contentUri = contentUri.withAppendedPath(contentUri, String.valueOf(mdMovie.getMovieId()));
            getContentResolver().delete(contentUri, null, null);
            mdMovie.setFavorite(false);
        }
        else{
            ContentValues favoriteMovieValues = new ContentValues();
            favoriteMovieValues.put(PopularMoviesContract.FavoriteMovieEntry.COLUMN_MOVIE_ID, mdMovie.getMovieId());
            favoriteMovieValues.put(PopularMoviesContract.FavoriteMovieEntry.COLUMN_OVERVIEW, mdMovie.getOverview());
            favoriteMovieValues.put(PopularMoviesContract.FavoriteMovieEntry.COLUMN_POSTER_PATH, mdMovie.getPosterRelativePath());
            favoriteMovieValues.put(PopularMoviesContract.FavoriteMovieEntry.COLUMN_RELEASE_DATE, mdMovie.getReleaseDate());
            favoriteMovieValues.put(PopularMoviesContract.FavoriteMovieEntry.COLUMN_RUNTIME, mdMovie.getRuntime());
            favoriteMovieValues.put(PopularMoviesContract.FavoriteMovieEntry.COLUMN_TITLE, mdMovie.getTitle());
            favoriteMovieValues.put(PopularMoviesContract.FavoriteMovieEntry.COLUMN_VOTE_AVERAGE, mdMovie.getVoteAverage());

            getContentResolver().insert(contentUri, favoriteMovieValues);
            mdMovie.setFavorite(true);
        }

        setFavoriteIcon();
    }

    private void setFavoriteIcon(){
        if(mdMovie.isFavorite()){
            mdFavoriteIconImageButton.setImageResource(R.drawable.ic_favorite_star_filled);
        }
        else{
            mdFavoriteIconImageButton.setImageResource(R.drawable.ic_favorite_star_empty);
        }
    }
}
