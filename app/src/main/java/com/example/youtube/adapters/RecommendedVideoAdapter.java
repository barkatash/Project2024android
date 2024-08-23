package com.example.youtube.adapters;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.youtube.R;
import com.example.youtube.WatchVideoActivity;
import com.example.youtube.entities.Video;
import com.example.youtube.repositories.VideoRepository;

import java.util.List;

public class RecommendedVideoAdapter extends RecyclerView.Adapter<RecommendedVideoAdapter.VideoViewHolder> {

    private final LayoutInflater mInflater;
    private List<Video> recommendedVideos;
    private Context context;
    private VideoRepository videoRepository;

    public List<Video> getRecommendedvideos() {
        return recommendedVideos;
    }

    public void setRecommendedvideos(List<Video> recommendedVideos) {
        this.recommendedVideos = recommendedVideos;
    }

    public RecommendedVideoAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
        if (context.getApplicationContext() instanceof Application) {
            this.videoRepository = new VideoRepository((Application) context.getApplicationContext());
        }
        videoRepository.getRecommendedVideos().observe((LifecycleOwner) context, videos -> {
            this.recommendedVideos = videos;
            notifyDataSetChanged();
        });

    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.item_recommended_video, parent, false);
        return new VideoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        if (recommendedVideos != null) {
            final Video current = recommendedVideos.get(position);
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
        }
    }


    @Override
    public int getItemCount() {
        return recommendedVideos != null ? recommendedVideos.size() : 0;
    }

    public static class VideoViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvAuthor;
        private final TextView tvContent;
        private final ImageView ivPic;
        private final TextView tvDuration;
        private final TextView tvViews;
        private final TextView tvUploadDate;

        public VideoViewHolder(View itemView) {
            super(itemView);
            tvAuthor = itemView.findViewById(R.id.textViewTitleVideo);
            tvContent = itemView.findViewById(R.id.tvContent);
            ivPic = itemView.findViewById(R.id.imageViewThumbnail);
            tvDuration = itemView.findViewById(R.id.textViewDurationVideo);
            tvViews = itemView.findViewById(R.id.textViewViewsVideo);
            tvUploadDate = itemView.findViewById(R.id.textViewUploadDateVideo);
        }
    }
}