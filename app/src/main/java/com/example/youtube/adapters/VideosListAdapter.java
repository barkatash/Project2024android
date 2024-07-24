package com.example.youtube.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.youtube.EditVideoActivity;
import com.example.youtube.R;
import com.example.youtube.WatchVideoActivity;
import com.example.youtube.entities.User;
import com.example.youtube.entities.Video;
import com.example.youtube.repositories.UserRepository;
import com.example.youtube.repositories.VideoRepository;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class VideosListAdapter extends RecyclerView.Adapter<VideosListAdapter.VideoViewHolder> {

    private final LayoutInflater mInflater;
    private List<Video> videos;
    private final Context context;
    private final UserRepository userRepository;
    private final VideoRepository videoRepository;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());

    public VideosListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
        this.userRepository = UserRepository.getInstance(context.getApplicationContext());
        this.videoRepository = VideoRepository.getInstance(context.getApplicationContext());
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.video_layout, parent, false);
        return new VideoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        if (videos != null) {
            final Video current = videos.get(position);
            holder.tvAuthor.setText(current.getAuthor());
            holder.tvTitle.setText(current.getTitle());

            // Load image from file path
            if (current.getImageFilePath() != null && !current.getImageFilePath().isEmpty()) {
                File imgFile = new File(current.getImageFilePath());
                if (imgFile.exists()) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    holder.ivPic.setImageBitmap(myBitmap);
                } else {
                    holder.ivPic.setImageResource(R.drawable.baseline_account_circle_24); // Set a default image if file does not exist
                }
            } else {
                holder.ivPic.setImageResource(R.drawable.baseline_account_circle_24); // Set a default image if no file path is provided
            }

            holder.tvDuration.setText(current.getDuration());
            holder.tvViews.setText(String.valueOf(current.getViews()));

            // Set the upload date
            if (current.getUploadDate() != null) {
                holder.tvUploadDate.setText(current.getUploadDate());
            } else {
                holder.tvUploadDate.setText("Unknown Date");
            }

            holder.ivPic.setOnClickListener(v -> {
                Intent intent = new Intent(context, WatchVideoActivity.class);
                intent.putExtra("videoId", current.getVideoId());
                context.startActivity(intent);
            });

            holder.btnEdit.setOnClickListener(v -> {
                User user = userRepository.getLoggedInUser();
                if (user != null) {
                    Intent intent = new Intent(context, EditVideoActivity.class);
                    intent.putExtra("videoId", current.getVideoId());
                    context.startActivity(intent);
                } else {
                    Toast.makeText(context, "You need to be logged in to edit a video.", Toast.LENGTH_SHORT).show();
                }
            });

            holder.btnDelete.setOnClickListener(v -> {
                User user = userRepository.getLoggedInUser();
                if (user != null) {
                    videoRepository.delete(current);
                    videos.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, videos.size());
                } else {
                    Toast.makeText(context, "You need to be logged in to delete a video.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return videos != null ? videos.size() : 0;
    }

    public static class VideoViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvAuthor;
        private final TextView tvTitle;
        private final ImageView ivPic;
        private final TextView tvDuration;
        private final TextView tvViews;
        private final TextView tvUploadDate;
        private final ImageButton btnDelete;
        private final ImageButton btnEdit;

        public VideoViewHolder(View itemView) {
            super(itemView);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
            tvTitle = itemView.findViewById(R.id.tvContent);
            ivPic = itemView.findViewById(R.id.ivPic);
            tvDuration = itemView.findViewById(R.id.tvDuration);
            tvViews = itemView.findViewById(R.id.tvViews);
            tvUploadDate = itemView.findViewById(R.id.tvUploadDate);
            btnDelete = itemView.findViewById(R.id.btnDeleteVideo);
            btnEdit = itemView.findViewById(R.id.btnEditVideo);
        }
    }
}
