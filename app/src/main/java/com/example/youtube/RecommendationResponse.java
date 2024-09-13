package com.example.youtube;

import com.example.youtube.entities.Video;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RecommendationResponse {
    @SerializedName("recommendations")
    private List<Video> recommendations;

    public List<Video> getRecommendations() {
        return recommendations;
    }
    public void setRecommendations(List<Video> recommendations) {
        this.recommendations = recommendations;
    }
}
