package com.example.youtube.adapters;

import android.content.Context;
import android.content.Intent;
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
import com.example.youtube.repositories.UserRepository;
import com.example.youtube.repositories.VideoRepository;
import com.example.youtube.WatchVideoActivity;
import com.example.youtube.entities.Video;

import java.util.List;

public class VideosListAdapter extends RecyclerView.Adapter<VideosListAdapter.VideoViewHolder> {

    private final LayoutInflater mInflater;
    private List<Video> videos;
    private Context context;
    private UserRepository userRepository; // Added repository field
    private VideoRepository videoRepository; // Added repository field

    public VideosListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
        this.userRepository = UserRepository.getInstance(context.getApplicationContext()); // Initialized repository
        this.videoRepository = VideoRepository.getInstance(context.getApplicationContext()); // Initialized repository
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
            holder.tvContent.setText(current.getContent());

            if (current.getImageBitMap() != null) {
                holder.ivPic.setImageBitmap(current.getImageBitMap());
            } else {
                holder.ivPic.setImageResource(current.getPic());
            }

            holder.tvDuration.setText(current.getDuration());
            holder.tvViews.setText(current.getViews());
            holder.tvUploadDate.setText(current.getUploadDate());

            holder.ivPic.setOnClickListener(v -> {
                Intent intent = new Intent(context, WatchVideoActivity.class);
                intent.putExtra("videoId", current.getId());
                context.startActivity(intent);
            });

            holder.btnEdit.setOnClickListener(v -> {
                if (userRepository.getLoggedInUser().getValue() != null) {
                    Intent intent = new Intent(context, EditVideoActivity.class);
                    intent.putExtra("videoId", current.getId());
                    context.startActivity(intent);
                } else {
                    Toast.makeText(context, "You need to be logged in to edit a video.", Toast.LENGTH_SHORT).show();
                }
            });

            holder.btnDelete.setOnClickListener(v -> {
                if (userRepository.getLoggedInUser().getValue() != null) {
                    videoRepository.delete(current);
                    videos.remove(current);
                    notifyDataSetChanged();
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
        private final TextView tvContent;
        private final ImageView ivPic;
        private final TextView tvDuration;
        private final TextView tvViews;
        private final TextView tvUploadDate;
        private final ImageButton btnDelete;
        private final ImageButton btnEdit;

        public VideoViewHolder(View itemView) {
            super(itemView);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
            tvContent = itemView.findViewById(R.id.tvContent);
            ivPic = itemView.findViewById(R.id.ivPic);
            tvDuration = itemView.findViewById(R.id.tvDuration);
            tvViews = itemView.findViewById(R.id.tvViews);
            tvUploadDate = itemView.findViewById(R.id.tvUploadDate);
            btnDelete = itemView.findViewById(R.id.btnDeleteVideo);
            btnEdit = itemView.findViewById(R.id.btnEditVideo);
        }
    }
}
