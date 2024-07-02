package com.example.youtube.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.youtube.R;
import com.example.youtube.UsersManager;
import com.example.youtube.entities.Comment;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class CommentsListAdapter extends RecyclerView.Adapter<CommentsListAdapter.CommentViewHolder> {

    private Context context;
    private List<Comment> comments;
    private CommentInteractionListener listener;

    public interface CommentInteractionListener {
        void onDeleteComment(Comment comment);
    }

    public CommentsListAdapter(Context context, CommentInteractionListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvUsername;
        private final TextView tvDescription;
        private final TextView tvUploadDate;
        private final TextView tvLikes;
        private ImageButton btnLike, btnUnlike;
        private EditText etEditComment;
        private ImageButton btnEdit;
        private ImageButton btnDelete;
        private Button btnSave;
        private Button btnCancel;
        private ImageView ivProfilePic;

        public CommentViewHolder(View itemView) {
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
            ivProfilePic = itemView.findViewById(R.id.ivProfilePic);
        }
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_layout, parent, false);
        return new CommentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        if (comments != null) {
            final Comment current = comments.get(position);
            AtomicBoolean isLiked = new AtomicBoolean(false);
            AtomicBoolean isUnliked = new AtomicBoolean(false);

            if (UsersManager.getInstance().getLoggedInUser() != null) {
                isLiked.set(UsersManager.getInstance().getLoggedInUser().getLikedComments().contains(current.getId()));
                isUnliked.set(UsersManager.getInstance().getLoggedInUser().getUnLikedComments().contains(current.getId()));
            } else {
                isUnliked.set(false);
                isLiked.set(false);
            }

            holder.btnLike.setImageResource(isLiked.get() ? R.drawable.baseline_thumb_up_24 : R.drawable.baseline_thumb_up_off_alt_24);
            holder.btnUnlike.setImageResource(isUnliked.get() ? R.drawable.baseline_thumb_down_24 : R.drawable.baseline_thumb_down_off_alt_24);

            holder.tvUsername.setText(current.getUser().getUserDisplayName());
            holder.tvDescription.setText(current.getDescription());
            holder.tvUploadDate.setText(current.getUploadDate());
            holder.tvLikes.setText(String.valueOf(current.getLikes()));
            holder.ivProfilePic.setImageResource(R.drawable.baseline_account_circle_24);

             if (current.getUser().getImageUrl() != null && !current.getUser().getImageUrl().isEmpty()) {
                Glide.with(context)
                        .load(current.getUser().getImageUrl())
                        .transform(new CircleCrop())
                        .into(holder.ivProfilePic);
            }

            holder.btnLike.setOnClickListener(v -> {
                if (!UsersManager.getInstance().isLoggedIn()) {
                    Toast.makeText(context, "You need to be logged in to like a comment", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!isLiked.get()) {
                    current.setLikes(current.getLikes() + 1);
                    holder.btnLike.setImageResource(R.drawable.baseline_thumb_up_24);
                    UsersManager.getInstance().getLoggedInUser().getLikedComments().add(current.getId());
                    isLiked.set(true);

                    if (isUnliked.get()) {
                        isUnliked.set(false);
                        holder.btnUnlike.setImageResource(R.drawable.baseline_thumb_down_off_alt_24);
                        UsersManager.getInstance().getLoggedInUser().getUnLikedComments().remove(Integer.valueOf(current.getId()));
                    }
                } else {
                    isLiked.set(false);
                    current.setLikes(current.getLikes() - 1);
                    holder.btnLike.setImageResource(R.drawable.baseline_thumb_up_off_alt_24);
                    UsersManager.getInstance().getLoggedInUser().getLikedComments().remove(Integer.valueOf(current.getId()));
                }
                notifyItemChanged(position);
            });

            holder.btnUnlike.setOnClickListener(v -> {
                if (!UsersManager.getInstance().isLoggedIn()) {
                    Toast.makeText(context, "You need to be logged in to unlike a comment", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!isUnliked.get()) {
                    isUnliked.set(true);
                    holder.btnUnlike.setImageResource(R.drawable.baseline_thumb_down_24);
                    UsersManager.getInstance().getLoggedInUser().getUnLikedComments().add(current.getId());

                    if (isLiked.get()) {
                        isLiked.set(false);
                        current.setLikes(current.getLikes() - 1);
                        holder.btnLike.setImageResource(R.drawable.baseline_thumb_up_off_alt_24);
                        UsersManager.getInstance().getLoggedInUser().getLikedComments().remove(Integer.valueOf(current.getId()));
                    }
                } else {
                    holder.btnUnlike.setImageResource(R.drawable.baseline_thumb_down_off_alt_24);
                    UsersManager.getInstance().getLoggedInUser().getUnLikedComments().remove(Integer.valueOf(current.getId()));
                }
                notifyItemChanged(position);
            });

            holder.btnEdit.setOnClickListener(v -> {
                if (!UsersManager.getInstance().isLoggedIn()) {
                    Toast.makeText(context, "You need to be logged in to edit a comment", Toast.LENGTH_SHORT).show();
                    return;
                }
                holder.etEditComment.setVisibility(View.VISIBLE);
                holder.etEditComment.setText(current.getDescription());
                holder.tvDescription.setVisibility(View.GONE);
                holder.btnEdit.setVisibility(View.GONE);
                holder.btnDelete.setVisibility(View.GONE);
                holder.btnSave.setVisibility(View.VISIBLE);
                holder.btnCancel.setVisibility(View.VISIBLE);
            });

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
                if (!UsersManager.getInstance().isLoggedIn()) {
                    Toast.makeText(context, "You need to be logged in to delete a comment", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (listener != null) {
                    listener.onDeleteComment(current);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return comments != null ? comments.size() : 0;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
        notifyDataSetChanged();
    }
}
