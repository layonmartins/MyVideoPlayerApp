package com.layonf.myvideoplayerapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.layonf.myvideoplayerapp.R;
import com.layonf.myvideoplayerapp.VideoPlayerActivity;
import com.layonf.myvideoplayerapp.interfaces.ClickListener;
import com.layonf.myvideoplayerapp.models.VideoModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VideoItemAdapter extends RecyclerView.Adapter{
    List<VideoModel> videoModels = new ArrayList<>();
    Context context;
    ClickListener clickListener;

    public VideoItemAdapter(List<VideoModel> videoModels, Context context, ClickListener clickListener) {
        this.videoModels = videoModels;
        this.context = context;
        this.clickListener = clickListener;
    }

    private class VideoHolder extends RecyclerView.ViewHolder{
        private TextView title;
        private TextView duration;
        private TextView filePath;
        private ImageView thumb;

        private VideoHolder(View view){
            super(view);
            title=view.findViewById(R.id.title);
            duration=view.findViewById(R.id.duration);
            filePath=view.findViewById(R.id.filePath);
            thumb=view.findViewById(R.id.thumb);
        }

    }

    public List<VideoModel> getVideoModels() {
        return videoModels;
    }

    public void setVideoModels(List<VideoModel> videoModels) {
        this.videoModels = videoModels;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public ClickListener getClickListener() {
        return clickListener;
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent, false);
        return new VideoHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        VideoHolder videoHolder = (VideoHolder) holder;
        final VideoModel videoModel=videoModels.get(position);

        videoHolder.title.setText(videoModel.getFileName());
        videoHolder.filePath.setText(videoModel.getFilePath());

        MediaPlayer mediaPlayer = MediaPlayer.create(context, Uri.fromFile(new File(videoModel.getFilePath())));
        double msec=0;
        if(mediaPlayer!=null){
            msec=mediaPlayer.getDuration();
        }

        double minutes=(msec%3600)/60;

        videoHolder.duration.setText("" + String.format("%.2f", minutes));
        Glide.with(context)
                .load(videoModel.getFilePath())
                .into(videoHolder.thumb);

        final double finalMsec = msec;
        videoHolder.thumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(finalMsec == 0){
                    Toast.makeText(context, "Invalid Video", Toast.LENGTH_SHORT).show();
                } else {
                    clickListener.onClickItem(videoModel.getFilePath());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return videoModels.size();
    }
}
