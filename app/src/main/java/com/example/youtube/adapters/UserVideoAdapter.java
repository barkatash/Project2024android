// UserVideoAdapter.java
package com.example.youtube.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.youtube.R;
import com.example.youtube.entities.Video;

import java.util.ArrayList;
import java.util.List;

public class UserVideoAdapter extends RecyclerView.Adapter<UserVideoAdapter.ViewHolder> {
    private List<Video> videoList;

    public UserVideoAdapter(List<Video> videoList) {
        this.videoList = videoList;
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
//        Glide.with(holder.itemView.getContext())
//                .load("http://10.0.2.2:8080/" + video.getImage())
//                .into(holder.imageViewThumbnail);
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
//            imageViewThumbnail = itemView.findViewById(R.id.imageViewThumbnail);
        }
    }
}
