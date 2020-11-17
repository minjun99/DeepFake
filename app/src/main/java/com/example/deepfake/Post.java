package com.example.deepfake;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class Post extends AppCompatActivity {
    private String postid;
    private VideoView postimage;
    private String description;
    private String publisher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_item);
        postimage = findViewById(R.id.post_image);
        Uri videoUri = Uri.parse("https://storage.googleapis.com/fakebook-posts/test7.mp4");
        postimage.setMediaController(new MediaController(this));
        postimage.setVideoURI(videoUri);
        postimage.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                postimage.start();
            }
        });

    }

    @Override
    protected void onPause(){
        super.onPause();
        if(postimage!=null&&postimage.isPlaying()) postimage.pause();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        if(postimage!=null) postimage.stopPlayback();
    }

    public Post(String postid, VideoView postimage, String description, String publisher) {
        this.postid = postid;
        this.postimage = postimage;
        this.description = description;
        this.publisher = publisher;
    }

    public Post() {
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public VideoView getPostimage() {
        return postimage;
    }

    public void setPostimage(VideoView postimage) {
        this.postimage = postimage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
}
