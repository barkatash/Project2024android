package com.example.youtube.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.youtube.R;
import com.example.youtube.entities.Video;

import java.util.List;

public class VideosListAdapter extends RecyclerView.Adapter<VideosListAdapter.VideoViewHolder> {
    class VideoViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvAuthor;
        private final TextView tvContent;
        private final ImageView ivPic;
        private final TextView tvDuration;
        private final TextView tvViews;
        private final TextView tvUploadDate;

        private VideoViewHolder(View itemView) {
            super(itemView);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
            tvContent = itemView.findViewById(R.id.tvContent);
            ivPic = itemView.findViewById(R.id.ivPic);
            tvDuration = itemView.findViewById(R.id.tvDuration);
            tvViews = itemView.findViewById(R.id.tvViews);
            tvUploadDate = itemView.findViewById(R.id.tvUploadDate);
        }
    }

    private final LayoutInflater mInflater;
    private List<Video> videos;

    public VideosListAdapter(Context context) { mInflater = LayoutInflater.from(context); }
    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.video_layout, parent, false);
        return new VideoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(VideoViewHolder holder, int position) {
        if (videos != null) {
            final Video current = videos.get(position);
            holder.tvAuthor.setText(current.getAuthor());
            holder.tvContent.setText(current.getContent());
            holder.ivPic.setImageResource(current.getPic());
            holder.tvDuration.setText(current.getDuration());
            holder.tvViews.setText(current.getViews());
            holder.tvUploadDate.setText(current.getUploadDate());
        }
    }

    public void setVideos(List<Video> s) {
        videos = s;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (videos != null)
            return videos.size();
        else return 0;
    }

    public List<Video> getVideos() { return videos; }
}

