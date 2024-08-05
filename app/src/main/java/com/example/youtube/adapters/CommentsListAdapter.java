package com.example.youtube.adapters;

import android.app.Application;
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
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.youtube.MyApplication;
import com.example.youtube.R;
import com.example.youtube.entities.Comment;
import com.example.youtube.entities.User;
import com.example.youtube.repositories.CommentRepository;
import com.example.youtube.repositories.UserRepository;
import com.example.youtube.repositories.VideoRepository;

import java.util.List;

public class CommentsListAdapter extends RecyclerView.Adapter<CommentsListAdapter.CommentViewHolder> {

    private Context context;
    private List<Comment> comments;
    private CommentInteractionListener listener;
    private final UserRepository userRepository;
    private CommentRepository commentRepository;
    private User loggedInUser = MyApplication.getCurrentUser();
    public interface CommentInteractionListener {
        void onDeleteComment(Comment comment);
        void onEditComment(Comment comment);
    }

    public CommentsListAdapter(Context context, CommentInteractionListener listener) {
        this.context = context;
        this.listener = listener;
        this.userRepository = UserRepository.getInstance(context.getApplicationContext());
        if (context.getApplicationContext() instanceof Application) {
            this.commentRepository = new CommentRepository((Application) context.getApplicationContext());
        }
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvUsername;
        private final TextView tvDescription;
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

            holder.tvUsername.setText(current.getUsername());
            holder.tvDescription.setText(current.getDescription());
            userRepository.getAllUsers().observe((LifecycleOwner) context, new Observer<List<User>>() {
                @Override
                public void onChanged(List<User> users) {
                    User foundUser = userRepository.getUserByUsername(current.getUsername());
                    if (foundUser != null) {
                        String imageUrl = "http://10.0.2.2:8080/" + foundUser.getImageUrl();
                        Glide.with(context)
                                .load(imageUrl)
                                .transform(new CircleCrop())
                                .into(holder.ivProfilePic);
                    } else {
                        holder.ivProfilePic.setImageResource(R.drawable.baseline_account_circle_24);
                    }
                }
            });


            holder.btnEdit.setOnClickListener(v -> {
                if (loggedInUser == null || !loggedInUser.getUsername().equals(current.getUsername())) {
                    Toast.makeText(context, "You need to be logged in and the owner of this comment to edit it.", Toast.LENGTH_SHORT).show();
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
                    listener.onEditComment(current);
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
                if (userRepository.getLoggedInUser() == null) {
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
