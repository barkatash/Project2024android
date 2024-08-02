// UserVideoAdapter.java
package com.example.youtube.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.youtube.R;
import com.example.youtube.WatchVideoActivity;
import com.example.youtube.entities.Video;

import java.util.ArrayList;
import java.util.List;

public class UserVideoAdapter extends RecyclerView.Adapter<UserVideoAdapter.ViewHolder> {
    private List<Video> videoList;
    private Context context;

    public UserVideoAdapter(Context context, List<Video> videoList) {
        this.videoList = videoList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Video video = videoList.get(position);
        holder.textViewTitle.setText(video.getTitle());
        String imageUrl = "http://10.0.2.2:8080/" + video.getImage();
        Glide.with(holder.imageViewThumbnail.getContext())
                .load(imageUrl)
                .into(holder.imageViewThumbnail);
        holder.textViewDuration.setText(video.getDuration());
        holder.textViewViews.setText(String.valueOf(video.getVisits()));
        holder.textViewUploadDate.setText(video.getUploadDate());
        holder.imageViewThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WatchVideoActivity.class);
                intent.putExtra("videoId", video.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }
    public void updateVideoList(List<Video> newVideoList) {
        this.videoList = newVideoList != null ? newVideoList : new ArrayList<>();
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewTitle;
        public ImageView imageViewThumbnail;
        public TextView textViewDuration;
        public TextView textViewViews;
        public TextView textViewUploadDate;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            imageViewThumbnail = itemView.findViewById(R.id.imageViewThumbnail);
            textViewDuration = itemView.findViewById(R.id.textViewDuration);
            textViewViews = itemView.findViewById(R.id.textViewViews);
            textViewUploadDate = itemView.findViewById(R.id.textViewUploadDate);
        }
    }
}
