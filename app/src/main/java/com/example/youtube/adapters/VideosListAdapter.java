package com.example.youtube.adapters;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.youtube.EditVideoActivity;
import com.example.youtube.MyApplication;
import com.example.youtube.R;
import com.example.youtube.WatchVideoActivity;
import com.example.youtube.entities.User;
import com.example.youtube.entities.Video;
import com.example.youtube.repositories.VideoRepository;

import java.util.List;

public class VideosListAdapter extends RecyclerView.Adapter<VideosListAdapter.VideoViewHolder> {

    private final LayoutInflater mInflater;
    private List<Video> videos;
    private Context context;
    private User loggedInUser = MyApplication.getCurrentUser();
    private VideoRepository videoRepository;

    public VideosListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
        if (context.getApplicationContext() instanceof Application) {
            this.videoRepository = new VideoRepository((Application) context.getApplicationContext());
        }
        // Observe the local database for changes
        videoRepository.getAllVideos().observe((LifecycleOwner) context, videos -> {
            this.videos = videos;
            notifyDataSetChanged();
        });

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
            holder.tvAuthor.setText(current.getUploader());
            holder.tvContent.setText(current.getTitle());
            holder.tvDuration.setText(current.getDuration());
            holder.tvViews.setText(String.valueOf(current.getVisits()));
            holder.tvUploadDate.setText(current.getUploadDate());

            String imageUrl = "http://10.0.2.2:8080/" + current.getImage();
            Glide.with(holder.ivPic.getContext())
                    .load(imageUrl)
                    .into(holder.ivPic);
            holder.ivPic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("VideosListAdapter", "Image clicked for video ID: " + current.getId());
                    Intent intent = new Intent(context, WatchVideoActivity.class);
                    intent.putExtra("videoId", current.getId());
                    context.startActivity(intent);
                }
            });

            holder.btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (loggedInUser != null && loggedInUser.getUsername().equals(current.getUploader())) {
                        Intent intent = new Intent(context, EditVideoActivity.class);
                        intent.putExtra("videoId", current.getId());
                        context.startActivity(intent);
                    } else {
                        Toast.makeText(context ,"You need to be logged in and the owner of this video in order to edit it.", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (loggedInUser != null && loggedInUser.getUsername().equals(current.getUploader())) {
                        videoRepository.deleteVideo(loggedInUser.getToken(), loggedInUser.getUsername(), current.getId());
                        videos.remove(current);
                        notifyDataSetChanged();
                    } else {
                        Toast.makeText(context ,"You need to be logged in and the owner of this video in order to delete it.", Toast.LENGTH_SHORT).show();
                    }
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