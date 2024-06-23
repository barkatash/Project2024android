package com.example.youtube.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.youtube.R;
import com.example.youtube.entities.Comment;
import java.util.List;

public class CommentsListAdapter extends RecyclerView.Adapter<CommentsListAdapter.CommentViewHolder>{
    class CommentViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvUsername;
        private final TextView tvDescription;
        private final TextView tvUploadDate;
        private final TextView tvLikes;
        private final TextView tvUnlikes;

        private CommentViewHolder(View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvUploadDate = itemView.findViewById(R.id.tvUploadDateComment);
            tvLikes = itemView.findViewById(R.id.tvLikes);
            tvUnlikes = itemView.findViewById(R.id.tvUnlikes);
        }
    }

    private final LayoutInflater mInflater;
    private List<Comment> comments;

    public CommentsListAdapter(Context context) { mInflater = LayoutInflater.from(context); }
    @Override
    public CommentsListAdapter.CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.comment_layout, parent, false);
        return new CommentsListAdapter.CommentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CommentsListAdapter.CommentViewHolder holder, int position) {
        if (comments != null) {
            final Comment current = comments.get(position);
            holder.tvUsername.setText(current.getAuthor());
            holder.tvDescription.setText(current.getDescription());
            holder.tvUploadDate.setText(current.getUploadDate());
            holder.tvLikes.setText(String.valueOf(current.getLikes()));
            holder.tvUnlikes.setText(String.valueOf(current.getDislikes()));
        }

    }


    @SuppressLint("NotifyDataSetChanged")
    public void setComments(List<Comment> s) {
        comments = s;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (comments != null)
            return comments.size();
        else return 0;
    }

    public List<Comment> getComments() { return comments; }
}
