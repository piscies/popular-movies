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

import br.com.marcosaraiva.popularmovies.AsyncTasks.AsyncTaskExtendedInterface;
import br.com.marcosaraiva.popularmovies.AsyncTasks.FetchMoviesFromMoviesDb_Task;
import br.com.marcosaraiva.popularmovies.Database.PopularMoviesContract;
import br.com.marcosaraiva.popularmovies.Model.Movie;
import br.com.marcosaraiva.popularmovies.Utilities.PopularMoviesPreferences;
import br.com.marcosaraiva.popularmovies.Utilities.MovieDbUtilities;
import br.com.marcosaraiva.popularmovies.Utilities.MovieDisplayMode;
import br.com.marcosaraiva.popularmovies.Utilities.NetworkUtilities;
import br.com.marcosaraiva.popularmovies.Utilities.PopularMoviesUtilities;

public class MainActivity extends AppCompatActivity
        implements MovieListAdapter.MovieListAdapterOnClickHandler, AsyncTaskExtendedInterface {

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
        new FetchMoviesFromMoviesDb_Task(this, this).execute(movieDisplayMode);
    }

    @Override
    public void onMovieClick(Movie clickedMovie) {
        Intent movieDetailsIntent = new Intent(this, MovieDetailsActivity.class);
        movieDetailsIntent.putExtra(Movie.INTENT_EXTRA_MOVIE, clickedMovie);

        startActivity(movieDetailsIntent);
    }

    @Override
    public void onTaskCompleted(Object result, String task) {
        if (result == null) {
            Toast errorToast = Toast.makeText(getApplicationContext(), R.string.toast_error_message, Toast.LENGTH_LONG);
            errorToast.show();
        }

        switch (task) {
            case FetchMoviesFromMoviesDb_Task.TASK_NAME:
                mMovieListAdapter.setMovieList((List<Movie>)result);
                break;
            default:
                throw new IllegalArgumentException("Unknown task name");
        }
    }
}
