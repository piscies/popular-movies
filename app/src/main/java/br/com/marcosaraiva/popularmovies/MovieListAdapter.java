package br.com.marcosaraiva.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.marcosaraiva.popularmovies.Model.Movie;
import br.com.marcosaraiva.popularmovies.Utilities.NetworkUtilities;

/**
 * Created by marco on 06/01/2018.
 * This is an Adapter for the Movie List Recycler View
 */

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieListAdapterViewHolder>
{
    private Context mlaContext;

    //Data list of movies that are displayed
    private List<Movie> mlaMovieList;
    public void setMovieList(List<Movie> newList)
    {
        mlaMovieList = newList;
        notifyDataSetChanged();
    }

    //Interface for the method that will be called when a RecyclerView item is clicked.
    private MovieListAdapterOnClickHandler mlaOnClickHandler;
    public interface MovieListAdapterOnClickHandler
    {
        void onMovieClick(Movie clickedMovie);
    }

    //Constructor
    public MovieListAdapter(MovieListAdapterOnClickHandler pInClickHandler, Context pContext)
    {
        mlaOnClickHandler = pInClickHandler;
        mlaContext = pContext;
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

        //Fills data from movie to layout
        holder.mMovieTitleTextView.setText(currentMovie.getTitle());

        //Movie poster
        String backdropURL = NetworkUtilities.MOVIEDB_IMAGE_500_URL + currentMovie.getBackdropRelativePath();
        Picasso.with(mlaContext).load(backdropURL).into(holder.mMovieBackdropImageView);
    }

    @Override
    public int getItemCount() {

        if(mlaMovieList != null)
        {
            return mlaMovieList.size();
        }
        else
            return 0;
    }
    /*End of necessary overrides in RecyclerView.Adapter class*/

    /*ViewHolder class for the MovieListAdapter*/
    public class MovieListAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public TextView mMovieTitleTextView;
        public ImageView mMovieBackdropImageView;

        public MovieListAdapterViewHolder(View view)
        {
            super(view);
            mMovieTitleTextView = view.findViewById(R.id.tv_movie_title);
            mMovieBackdropImageView = view.findViewById(R.id.iv_movie_backdrop);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int currentMoviePositionInList = getAdapterPosition();

            mlaOnClickHandler.onMovieClick(mlaMovieList.get(currentMoviePositionInList));
        }
    }
}