package br.com.marcosaraiva.popularmovies;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.marcosaraiva.popularmovies.Model.Trailer;

/**
 * Created by marco on 20/02/2018.
 * This is an Adapter for the Trailer List Recycler View
 */

public class TrailerListAdapter extends RecyclerView.Adapter<TrailerListAdapter.TrailerListAdapterViewHolder> {
    //Data list of trailers that are displayed
    private List<Trailer> tlaTrailerList;
    //Interface for the method that will be called when a RecyclerView item is clicked.
    private final TrailerListAdapterOnClickHandler tlaOnClickHandler;

    //Constructor
    public TrailerListAdapter(TrailerListAdapterOnClickHandler pOnClickHandler) {
        tlaOnClickHandler = pOnClickHandler;
    }

    public void setTrailerList(List<Trailer> newList) {
        tlaTrailerList = newList;
        notifyDataSetChanged();
    }

    /*All overrides necessary for the RecyclerView.Adapter class*/
    @Override
    public TrailerListAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        //Creating a new view for the new ViewHolder
        View newTrailerListItemView = inflater.inflate(R.layout.trailer_list_item, parent, false);

        //Creating and returning the new ViewHolder
        return new TrailerListAdapterViewHolder(newTrailerListItemView);
    }

    @Override
    public void onBindViewHolder(TrailerListAdapterViewHolder holder, int position) {

        //Trailer that will fill the data in the specified ViewHolder
        Trailer currentTrailer = tlaTrailerList.get(position);

        //Trailer name
        holder.mTrailerNameTextView.setText(currentTrailer.getName());
    }

    @Override
    public int getItemCount() {

        if (tlaTrailerList != null) {
            return tlaTrailerList.size();
        } else
            return 0;
    }

    public interface TrailerListAdapterOnClickHandler {
        void onTrailerClick(Trailer clickedTrailer);
    }
    /*End of necessary overrides in RecyclerView.Adapter class*/

    /*ViewHolder class for the TrailerListAdapter*/
    public class TrailerListAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView mTrailerNameTextView;

        public TrailerListAdapterViewHolder(View view) {
            super(view);
            mTrailerNameTextView = view.findViewById(R.id.tv_trailer_name);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int currentTrailerPositionInList = getAdapterPosition();

            tlaOnClickHandler.onTrailerClick(tlaTrailerList.get(currentTrailerPositionInList));
        }
    }
}
