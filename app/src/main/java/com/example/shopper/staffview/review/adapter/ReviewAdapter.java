package com.example.shopper.staffview.review.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopper.R;
import com.example.shopper.staffview.review.model.Review;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewHolder> {
    List<Review> data;
    private Context context;

    public ReviewAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<Review> data) {
        this.data = data;
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public ReviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review, parent, false);

        return new ReviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewHolder holder, int position) {
        Review review = data.get(position);
        if (review == null) return;

        Picasso.get().load(review.getAvatar()).into(holder.imageAvatar);
        holder.txtName.setText(review.getName());
        holder.txtTime.setText(review.getTime());
        holder.txtContent.setText(review.getContent());

        holder.rating.setRating(review.getRating());

        if (review.getImage() == null) {
            holder.imageRating.setVisibility(View.GONE);
        } else {
            Picasso.get().load(review.getImage()).into(holder.imageRating);
        }


    }

    @Override
    public int getItemCount() {
        if (data != null) return data.size();
        return 0;
    }

    public class ReviewHolder extends RecyclerView.ViewHolder {
        private ImageView imageAvatar, imageRating;
        private TextView txtName, txtTime, txtContent;
        private RatingBar rating;

        public ReviewHolder(@NonNull View item) {
            super(item);
            imageAvatar = item.findViewById(R.id.imageAvatarReview);
            txtName = item.findViewById(R.id.txtNameReview);
            txtTime = item.findViewById(R.id.txtTimeReview);

            txtContent = item.findViewById(R.id.txtContentReview);
            rating = item.findViewById(R.id.ratingReview);
            imageRating = item.findViewById(R.id.imageRatingReview);
        }
    }
}
