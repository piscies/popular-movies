package br.com.marcosaraiva.popularmovies;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.marcosaraiva.popularmovies.Model.Review;

/**
 * Created by marco on 222/02/2018.
 * This is an Adapter for the Reeviews List Recycler View
 */

public class ReviewListAdapter extends RecyclerView.Adapter<ReviewListAdapter.ReviewListAdapterViewHolder> {
    //Data list of reviews that are displayed
    private List<Review> rlaReviewList;
    //Interface for the method that will be called when a RecyclerView item is clicked.
    private final ReviewListAdapterOnClickHandler rlaOnClickHandler;

    //Constructor
    public ReviewListAdapter(ReviewListAdapterOnClickHandler pOnClickHandler) {
        rlaOnClickHandler = pOnClickHandler;
    }

    public void setReviewList(List<Review> newList) {
        rlaReviewList = newList;
        notifyDataSetChanged();
    }

    /*All overrides necessary for the RecyclerView.Adapter class*/
    @Override
    public ReviewListAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        //Creating a new view for the new ViewHolder
        View newReviewListItemView = inflater.inflate(R.layout.review_list_item, parent, false);

        //Creating and returning the new ViewHolder
        return new ReviewListAdapterViewHolder(newReviewListItemView);
    }

    @Override
    public void onBindViewHolder(ReviewListAdapterViewHolder holder, int position) {

        //Review that will fill the data in the specified ViewHolder
        Review currentReview = rlaReviewList.get(position);

        //Review properties
        holder.vhAuthorTextView.setText(currentReview.getAuthor());
        holder.vhReviewContentTextView.setText(currentReview.getReviewText());
    }

    @Override
    public int getItemCount() {

        if (rlaReviewList != null) {
            return rlaReviewList.size();
        } else
            return 0;
    }

    public interface ReviewListAdapterOnClickHandler {
        void onReviewClick(Review clickedReview);
    }
    /*End of necessary overrides in RecyclerView.Adapter class*/

    /*ViewHolder class for the TrailerListAdapter*/
    public class ReviewListAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView vhAuthorTextView;
        public final TextView vhReviewContentTextView;

        public ReviewListAdapterViewHolder(View view) {
            super(view);
            vhAuthorTextView = view.findViewById(R.id.tv_review_author);
            vhReviewContentTextView = view.findViewById(R.id.tv_review_text);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int currentReviewPositionInList = getAdapterPosition();

            rlaOnClickHandler.onReviewClick(rlaReviewList.get(currentReviewPositionInList));
        }
    }
}
