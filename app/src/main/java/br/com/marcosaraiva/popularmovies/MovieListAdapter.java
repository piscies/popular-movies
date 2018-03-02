package br.com.marcosaraiva.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.marcosaraiva.popularmovies.Model.Movie;
import br.com.marcosaraiva.popularmovies.Utilities.NetworkUtilities;

import static android.content.res.Configuration.ORIENTATION_PORTRAIT;

/**
 * Created by marco on 06/01/2018.
 * This is an Adapter for the Movie List Recycler View
 */

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieListAdapterViewHolder> {
    private final Context mlaContext;

    //Data list of movies that are displayed
    private List<Movie> mlaMovieList;
    //Interface for the method that will be called when a RecyclerView item is clicked.
    private final MovieListAdapterOnClickHandler mlaOnClickHandler;

    //Constructor
    public MovieListAdapter(MovieListAdapterOnClickHandler pInClickHandler, Context pContext) {
        mlaOnClickHandler = pInClickHandler;
        mlaContext = pContext;
    }

    public void setMovieList(List<Movie> newList) {
        mlaMovieList = newList;
        notifyDataSetChanged();
    }

    /*All overrides necessary for the RecyclerView.Adapter class*/
    @Override
    public MovieListAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        //Creating a new view for the new ViewHolder
        View newMovieListItemView = inflater.inflate(R.layout.movie_list_item, parent, false);

        //Creating and returning the new ViewHolder
        return new MovieListAdapterViewHolder(newMovieListItemView);
    }

    @Override
    public void onBindViewHolder(MovieListAdapterViewHolder holder, int position) {

        //Movie that will fill the data in the specified ViewHolder
        Movie currentMovie = mlaMovieList.get(position);

        //Movie poster
        String posterBaseURL;

        if(mlaContext.getResources().getConfiguration().orientation == ORIENTATION_PORTRAIT)
            posterBaseURL = NetworkUtilities.MOVIEDB_IMAGE_780_URL;
        else
            posterBaseURL = NetworkUtilities.MOVIEDB_IMAGE_500_URL;

        String backdropURL = posterBaseURL + currentMovie.getPosterRelativePath();
        Picasso.with(mlaContext).load(backdropURL).into(holder.mMoviePosterImageView);

        if(currentMovie.isFavorite())
            holder.mFavoriteStarImageView.setVisibility(View.VISIBLE);
        else
            holder.mFavoriteStarImageView.setVisibility(View.INVISIBLE);
    }

    @Override
    public int getItemCount() {

        if (mlaMovieList != null) {
            return mlaMovieList.size();
        } else
            return 0;
    }

    public interface MovieListAdapterOnClickHandler {
        void onMovieClick(Movie clickedMovie);
    }
    /*End of necessary overrides in RecyclerView.Adapter class*/

    /*ViewHolder class for the MovieListAdapter*/
    public class MovieListAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final ImageView mMoviePosterImageView;
        public final ImageView mFavoriteStarImageView;

        public MovieListAdapterViewHolder(View view) {
            super(view);
            mMoviePosterImageView = view.findViewById(R.id.iv_movie_poster);
            mFavoriteStarImageView = view.findViewById(R.id.iv_main_list_favorite_star);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int currentMoviePositionInList = getAdapterPosition();

            mlaOnClickHandler.onMovieClick(mlaMovieList.get(currentMoviePositionInList));
        }
    }
}
