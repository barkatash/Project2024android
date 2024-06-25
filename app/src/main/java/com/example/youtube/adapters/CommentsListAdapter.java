package com.example.youtube.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
        ImageButton btnLike, btnUnlike;
        private EditText etEditComment;
        private ImageButton btnEdit;
        private ImageButton btnDelete;
        private Button btnSave;
        private Button btnCancel;

        private CommentViewHolder(View itemView) {

            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvUploadDate = itemView.findViewById(R.id.tvUploadDateComment);
            tvLikes = itemView.findViewById(R.id.tvLikes);
            btnLike = itemView.findViewById(R.id.tvLike);
            btnUnlike = itemView.findViewById(R.id.tvDislikes);
            etEditComment = itemView.findViewById(R.id.etEditComment);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnSave = itemView.findViewById(R.id.btnSave);
            btnCancel = itemView.findViewById(R.id.btnCancel);
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

            holder.btnLike.setOnClickListener(v -> {
                if (current.isLiked()) {
                    current.setLiked(false);
                    current.setLikes(current.getLikes() - 1);
                } else {
                    current.setLiked(true);
                    current.setLikes(current.getLikes() + 1);
                }
                notifyItemChanged(position);
            });

            holder.btnUnlike.setOnClickListener(v -> {
                if (current.isUnliked()) {
                    current.setUnliked(false);
                    current.setUnlikes(current.getUnlikes() - 1);
                } else {
                    current.setUnliked(true);
                    current.setUnlikes(current.getUnlikes() + 1);
                }
                notifyItemChanged(position);
            });

            holder.btnEdit.setOnClickListener(v -> {
                holder.etEditComment.setVisibility(View.VISIBLE);
                holder.etEditComment.setText(current.getDescription());
                holder.tvDescription.setVisibility(View.GONE);
                holder.btnEdit.setVisibility(View.GONE);
                holder.btnDelete.setVisibility(View.GONE);
                holder.btnSave.setVisibility(View.VISIBLE);
                holder.btnCancel.setVisibility(View.VISIBLE);
            });
            // Save button click listener
            holder.btnSave.setOnClickListener(v -> {
                String editedCommentText = holder.etEditComment.getText().toString().trim();
                if (!TextUtils.isEmpty(editedCommentText)) {
                    current.setDescription(editedCommentText);
                    notifyDataSetChanged();
                }
                holder.etEditComment.setVisibility(View.GONE);
                holder.tvDescription.setVisibility(View.VISIBLE);
                holder.btnEdit.setVisibility(View.VISIBLE);
                holder.btnDelete.setVisibility(View.VISIBLE);
                holder.btnSave.setVisibility(View.GONE);
                holder.btnCancel.setVisibility(View.GONE);
            });

            holder.btnCancel.setOnClickListener(v -> {
                holder.etEditComment.setVisibility(View.GONE);
                holder.tvDescription.setVisibility(View.VISIBLE);
                holder.btnEdit.setVisibility(View.VISIBLE);
                holder.btnDelete.setVisibility(View.VISIBLE);
                holder.btnSave.setVisibility(View.GONE);
                holder.btnCancel.setVisibility(View.GONE);
            });

            holder.btnDelete.setOnClickListener(v -> {
                comments.remove(current);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, getItemCount());

            });

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
