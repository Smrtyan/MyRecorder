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
import android.widget.Button;

import java.io.File;

public class PlayRecordDialog extends DialogFragment {
    MediaPlayer mediaPlayer;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_recorder_player, null);
        String savedName = getArguments().getString("savedName");
        mediaPlayer = MediaPlayer.create(getActivity(), Uri.parse("file://" + savedName));
        mediaPlayer.start();

        view.findViewById(R.id.btn_record_play).setOnClickListener(v->{
            //TODO to pause or resume
            Button button = (Button)v;
            if(button.getText().toString().equals("PAUSE")){
                button.setText("RESUME");
                mediaPlayer.pause();
            }else {
                button.setText("PAUSE");
                mediaPlayer.start();
            }

        });
        view.findViewById(R.id.btn_record_back).setOnClickListener(v -> {
            if(mediaPlayer!=null)
                mediaPlayer.stop();
            dismiss();});
        return view;
    }
}
