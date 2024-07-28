package com.example.youtube.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.youtube.entities.Comment;
import com.example.youtube.api.CommentAPI;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentViewModel extends AndroidViewModel {
    private CommentAPI commentRepository;
    private final MutableLiveData<List<Comment>> commentsLiveData;

    public CommentViewModel(@NonNull Application application) {
        super(application);
        // Pass application context to CommentRemoteRepository
        //commentRepository = new CommentAPI(application.getApplicationContext());
        commentsLiveData = new MutableLiveData<>();
        loadComments();
    }

    private void loadComments() {
        commentRepository.getCommentsForVideo(1, new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                if (response.isSuccessful()) {
                    commentsLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                // Handle failure
            }
        });
    }

    public LiveData<List<Comment>> getAllComments() {
        return commentsLiveData;
    }

    public void addComment(Comment comment) {
        commentRepository.addComment(comment, new Callback<Comment>() {
            @Override
            public void onResponse(Call<Comment> call, Response<Comment> response) {
                if (response.isSuccessful()) {
                    loadComments(); // Reload comments after adding a new one
                }
            }

            @Override
            public void onFailure(Call<Comment> call, Throwable t) {
                // Handle failure
            }
        });
    }

    public void deleteComment(int commentId) {
        commentRepository.deleteComment(commentId, new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    loadComments(); // Reload comments after deleting one
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Handle failure
            }
        });
    }
}
