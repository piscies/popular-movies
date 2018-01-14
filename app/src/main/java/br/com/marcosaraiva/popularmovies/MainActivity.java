package br.com.marcosaraiva.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONException;

import java.net.URL;
import java.util.List;

import br.com.marcosaraiva.popularmovies.Model.Movie;
import br.com.marcosaraiva.popularmovies.Utilities.MovieDbUtilities;
import br.com.marcosaraiva.popularmovies.Utilities.MovieSortByEnum;
import br.com.marcosaraiva.popularmovies.Utilities.NetworkUtilities;

public class MainActivity extends AppCompatActivity implements MovieListAdapter.MovieListAdapterOnClickHandler {

    private final String ERROR_TAG = "MAIN_ACTIVITY";

    private final String PREFERENCE_SORTBY = "PREF_SORTBY";

    private RecyclerView mMoviesRecyclerView;
    private MovieListAdapter mMovieListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        //Handling shared preference for OrderBy
        SharedPreferences preference = getPreferences(MODE_PRIVATE);
        if (!preference.contains(PREFERENCE_SORTBY)) {
            SharedPreferences.Editor editor = preference.edit();

            //Default sort by is "By Most Popular".
            editor.putInt(PREFERENCE_SORTBY, MovieSortByEnum.toInteger(MovieSortByEnum.MostPopular));
            editor.apply();
        }

        //Movie List adapter
        mMovieListAdapter = new MovieListAdapter(this, this);

        //Gets the main recycler view and configures it
        mMoviesRecyclerView = findViewById(R.id.rv_movies_list);
        mMoviesRecyclerView.setAdapter(mMovieListAdapter);

        RecyclerView.LayoutManager layoutManager
                = new GridLayoutManager(this, 3);

        mMoviesRecyclerView.setLayoutManager(layoutManager);

        //Loads the movie list
        loadMovies();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.movie_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        switch (item.getItemId()) {
            case R.id.action_sort_most_popular:
                editor.putInt(PREFERENCE_SORTBY, MovieSortByEnum.toInteger(MovieSortByEnum.MostPopular));
                editor.apply();
                loadMovies();
                return true;
            case R.id.action_sort_highest_rated:
                editor.putInt(PREFERENCE_SORTBY, MovieSortByEnum.toInteger(MovieSortByEnum.HighestRated));
                editor.apply();
                loadMovies();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void loadMovies() {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        MovieSortByEnum selectedSortBy = MovieSortByEnum.fromInteger(preferences.getInt(PREFERENCE_SORTBY, 0));
        new FetchMoviesFromMoviesDb_Task().execute(selectedSortBy);
    }

    @Override
    public void onMovieClick(Movie clickedMovie) {
        Intent movieDetailsIntent = new Intent(this, MovieDetailsActivity.class);
        movieDetailsIntent.putExtra(Movie.INTENT_EXTRA_MOVIE, clickedMovie);

        startActivity(movieDetailsIntent);
    }

    public class FetchMoviesFromMoviesDb_Task extends AsyncTask<MovieSortByEnum, Void, List<Movie>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<Movie> doInBackground(MovieSortByEnum... params) {
            MovieSortByEnum sortByParameter;

            //If there are no parameters, calls the SortyBy Popularity by default
            if (params.length == 0) {
                sortByParameter = MovieSortByEnum.MostPopular;
            } else
                sortByParameter = params[0];

            //Gets the correct URL to be called
            URL movieDbApiCallURL = NetworkUtilities.buildMovieDbQueryURL(sortByParameter);

            //If there was a problem during URL creation...
            if (movieDbApiCallURL == null)
                return null;

            try {
                String jsonMoviesRawResponse = NetworkUtilities
                        .getResponseFromHttpUrl(movieDbApiCallURL);

                return MovieDbUtilities
                        .getListOfMoviesFromAPIJSONResponse(jsonMoviesRawResponse);
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
        protected void onPostExecute(List<Movie> movieList) {
            if (movieList != null) {
                mMovieListAdapter.setMovieList(movieList);
            } else {
                Toast errorToast = Toast.makeText(getApplicationContext(), R.string.toast_error_message, Toast.LENGTH_LONG);
                errorToast.show();
            }
        }
    }
}
