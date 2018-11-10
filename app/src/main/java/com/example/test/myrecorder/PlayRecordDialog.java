package com.example.test.myrecorder;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;

public class PlayRecordDialog extends DialogFragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_recorder_player, null);
        String savedName = getArguments().getString("savedName");
        MediaPlayer mediaPlayer = MediaPlayer.create(getActivity(), Uri.parse("file://" + savedName));
        mediaPlayer.start();
        view.findViewById(R.id.btn_record_play).setOnClickListener(v->{
            //TODO to pause or resume
            
        });
        view.findViewById(R.id.btn_record_back).setOnClickListener(v -> {dismiss();});
        return view;
    }
}
