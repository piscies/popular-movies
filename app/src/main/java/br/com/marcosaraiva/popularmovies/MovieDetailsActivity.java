package br.com.marcosaraiva.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.net.URL;
import java.util.List;

import br.com.marcosaraiva.popularmovies.Model.Movie;
import br.com.marcosaraiva.popularmovies.Model.Trailer;
import br.com.marcosaraiva.popularmovies.Utilities.MovieDbUtilities;
import br.com.marcosaraiva.popularmovies.Utilities.NetworkUtilities;

public class MovieDetailsActivity extends AppCompatActivity implements TrailerListAdapter.TrailerListAdapterOnClickHandler {

    private final String ERROR_TAG = "MOVIE_DETAILS_ACTIVITY";

    private TextView mdMovieTitleTextView;
    private TextView mdMovieOverviewTextView;
    private TextView mdMovieAverageRatingTextView;
    private TextView mdMovieReleaseDateTextView;
    private ImageView mdMoviePosterImageView;

    private RecyclerView mdTrailersRecyclerView;
    private TrailerListAdapter mdTrailerListAdapter;

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

        RecyclerView.LayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mdTrailersRecyclerView.setLayoutManager(layoutManager);

        //Loads the movie list
        loadTrailers(detailedMovie.getMovieId());
    }

    private void loadTrailers(long movieId) {

        new FetchTrailersFromMoviesDb_Task().execute(movieId);
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

    public class FetchTrailersFromMoviesDb_Task extends AsyncTask<Long, Void, List<Trailer>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<Trailer> doInBackground(Long... params) {
            long movieId = 0;

            //If there are no parameters, calls the SortyBy Popularity by default
            if (params.length > 0)
                movieId = params[0];
            else //If no movieId was given...
                return null;

            //Gets the correct URL to be called
            URL movieDbApiCallURL = NetworkUtilities.buildMovieDbTrailerURL(movieId);

            //If there was a problem during URL creation...
            if (movieDbApiCallURL == null)
                return null;

            try {
                String jsonTrailersRawResponse = NetworkUtilities
                        .getResponseFromHttpUrl(movieDbApiCallURL);

                return MovieDbUtilities
                        .getListOfTrailersFromAPIJSONResponse(jsonTrailersRawResponse);
            } catch (RuntimeException e) {
                Log.e(ERROR_TAG, e.getMessage());
                return null;
            } catch (java.io.IOException e) {
                Log.e(ERROR_TAG, e.getMessage());
                return null;
            } catch (JSONException e) {
                Log.e(ERROR_TAG, e.getMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Trailer> trailerList) {
            if (trailerList != null) {
                mdTrailerListAdapter.setTrailerList(trailerList);
            } else {
                Toast errorToast = Toast.makeText(getApplicationContext(), R.string.toast_error_message, Toast.LENGTH_LONG);
                errorToast.show();
            }
        }
    }
}
