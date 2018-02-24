package br.com.marcosaraiva.popularmovies;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
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

import br.com.marcosaraiva.popularmovies.Database.PopularMoviesContract;
import br.com.marcosaraiva.popularmovies.Model.Movie;
import br.com.marcosaraiva.popularmovies.Utilities.PopularMoviesPreferences;
import br.com.marcosaraiva.popularmovies.Utilities.MovieDbUtilities;
import br.com.marcosaraiva.popularmovies.Utilities.MovieDisplayMode;
import br.com.marcosaraiva.popularmovies.Utilities.NetworkUtilities;
import br.com.marcosaraiva.popularmovies.Utilities.PopularMoviesUtilities;

public class MainActivity extends AppCompatActivity implements MovieListAdapter.MovieListAdapterOnClickHandler {

    private final String ERROR_TAG = "MAIN_ACTIVITY";

    private RecyclerView mMoviesRecyclerView;
    private MovieListAdapter mMovieListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

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

        switch (item.getItemId()) {
            case R.id.action_sort_most_popular:
                PopularMoviesPreferences.setDisplayMode(MovieDisplayMode.MOST_POPULAR, this);
                break;
            case R.id.action_sort_highest_rated:
                PopularMoviesPreferences.setDisplayMode(MovieDisplayMode.HIGHEST_RATED, this);
                break;
            case R.id.action_show_favorites:
                PopularMoviesPreferences.setDisplayMode(MovieDisplayMode.SHOW_FAVORITES, this);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        loadMovies();
        return true;
    }

    private void loadMovies() {
        @MovieDisplayMode int movieDisplayMode = PopularMoviesPreferences.getDisplayMode(this);
        new FetchMoviesFromMoviesDb_Task().execute(movieDisplayMode);
    }

    @Override
    public void onMovieClick(Movie clickedMovie) {
        Intent movieDetailsIntent = new Intent(this, MovieDetailsActivity.class);
        movieDetailsIntent.putExtra(Movie.INTENT_EXTRA_MOVIE, clickedMovie);

        startActivity(movieDetailsIntent);
    }

    public class FetchMoviesFromMoviesDb_Task extends AsyncTask<Integer, Void, List<Movie>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<Movie> doInBackground(Integer... params) {

            @MovieDisplayMode int displayModeParameter = params[0];

            //MovieDb API Fetch
            if(displayModeParameter != MovieDisplayMode.SHOW_FAVORITES) {

                //Gets the correct URL to be called
                URL movieDbApiCallURL = NetworkUtilities.buildMovieDbQueryURL(displayModeParameter);

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
            else{ //Gets favorite movies from DB
                Uri favoriteMoviesUri = PopularMoviesContract.FavoriteMovieEntry.CONTENT_URI;
                Cursor cursor = getContentResolver().query(favoriteMoviesUri, null, null,
                        null, null);

                return PopularMoviesUtilities.getMovieListFromCursor(cursor);
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
