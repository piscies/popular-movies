package br.com.marcosaraiva.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

import br.com.marcosaraiva.popularmovies.AsyncTasks.AsyncTaskExtendedInterface;
import br.com.marcosaraiva.popularmovies.AsyncTasks.FetchMoviesFromMoviesDb_Task;
import br.com.marcosaraiva.popularmovies.Model.Movie;
import br.com.marcosaraiva.popularmovies.Utilities.PopularMoviesPreferences;
import br.com.marcosaraiva.popularmovies.Utilities.MovieDisplayMode;

import static android.content.res.Configuration.ORIENTATION_PORTRAIT;

public class MainActivity extends AppCompatActivity
        implements MovieListAdapter.MovieListAdapterOnClickHandler, AsyncTaskExtendedInterface {

    private final String ERROR_TAG = "MAIN_ACTIVITY";

    private MovieListAdapter mMovieListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        //Movie List adapter
        mMovieListAdapter = new MovieListAdapter(this, this);

        //Gets the main recycler view and configures it
        RecyclerView mMoviesRecyclerView = findViewById(R.id.rv_movies_list);
        mMoviesRecyclerView.setAdapter(mMovieListAdapter);

        //Differences between PORTRAIT and LANDSCAPE
        int layoutSpanCount;

        if(getResources().getConfiguration().orientation == ORIENTATION_PORTRAIT)
            layoutSpanCount = 2;
        else
            layoutSpanCount = 5;

        RecyclerView.LayoutManager layoutManager
                = new GridLayoutManager(this, layoutSpanCount);

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
                mMovieListAdapter.setMovieList((List<Movie>) result);
                break;
            default:
                throw new IllegalArgumentException("Unknown task name");
        }
    }
}
