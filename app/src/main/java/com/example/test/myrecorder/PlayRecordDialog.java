package com.example.test.myrecorder;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class PlayRecordDialog extends DialogFragment {
    MediaPlayer mediaPlayer;
    boolean pressPictureFlag = false;
    private ImageButton btn_play;
    private ImageButton btn_back;
    private TextView musicName, musicLength, musicCur;
    private SeekBar seekBar;
    private Timer timer;
    private boolean isSeekBarChanging;//互斥变量，防止进度条与定时器冲突。
    private int currentPosition;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_recorder_player, null);
        String savedName = getArguments().getString("savedName");
        mediaPlayer = MediaPlayer.create(getActivity(), Uri.parse("file://" + savedName));
        mediaPlayer.start();

//        view.findViewById(R.id.btn_record_play).setOnClickListener(v->{
//            //TODO to pause or resume
//            Button button = (Button)v;
//            if(button.getText().toString().equals("PAUSE")){
//                button.setText("RESUME");
//                mediaPlayer.pause();
//            }else {
//                button.setText("PAUSE");
//                mediaPlayer.start();
//            }
//
//        });
//        view.findViewById(R.id.btn_record_back).setOnClickListener(v -> {
//            if(mediaPlayer!=null)
//                mediaPlayer.stop();
//            dismiss();});

        seekBar = (SeekBar) view.findViewById(R.id.seekBar);
        musicLength = (TextView) view.findViewById(R.id.total);
        musicCur = (TextView) view.findViewById(R.id.progress);
        LinearLayout linearLayout = view.findViewById(R.id.ll_control_panel);

        btn_play = new ImageButton(getActivity());
        btn_play.setBackgroundColor(0xfff);
        btn_play.setImageResource(R.drawable.pause);

        btn_back = new ImageButton(getActivity());
        btn_back.setOnClickListener(v -> {
            if(mediaPlayer!=null)
                mediaPlayer.stop();
            dismiss();});
        btn_back.setImageResource(R.drawable.back);
        btn_back.setBackgroundColor(0xfff);

        linearLayout.addView(btn_play);
        linearLayout.addView(btn_back);

        seekBar.setMax(mediaPlayer.getDuration());
        seekBar.setOnSeekBarChangeListener(new MySeekBar());
        musicLength.setText(formatTime(mediaPlayer.getDuration()));
        musicCur.setText("00:00");
        //监听播放
        timer = new Timer();
        timer.schedule(new TimerTask() {
            Runnable updateUI = new Runnable() {
                @Override
                public void run() {
                    musicCur.setText(formatTime(mediaPlayer.getCurrentPosition()));
                }
            };
            @Override
            public void run() {
                if (!isSeekBarChanging) {
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    if(getActivity() == null)return;
                    else getActivity().runOnUiThread(updateUI);
                }
            }
        }, 0, 50);
        btn_play.setOnTouchListener(new View.OnTouchListener()/*按下与松手属于两个事件*/

        {
            @Override
            public boolean onTouch (View v, MotionEvent event)
            {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (pressPictureFlag == false) {
                        btn_play.setImageResource(R.drawable.play);
                        btn_play.setBackgroundColor(0xfff);
                        mediaPlayer.pause();
 /*..................
                        功能代码1*/
                        pressPictureFlag = true;
                    } else {
                        btn_play.setImageResource(R.drawable.pause);
                        btn_play.setBackgroundColor(0xfff);
                        mediaPlayer.start();
/*..................
                    功能代码2*/
                        pressPictureFlag = false;
                    }
                }
                return false;
            }
        });




        return view;
    }
    private String formatTime(int length) {
        Date date = new Date(length);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");//规定形式
        String TotalTime = simpleDateFormat.format(date);//转化为需要形式
        return TotalTime;

    }
    /*进度条处理*/
    public class MySeekBar implements SeekBar.OnSeekBarChangeListener {

        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
        }

        /*滚动时,应当暂停后台定时器*/
        public void onStartTrackingTouch(SeekBar seekBar) {
            isSeekBarChanging = true;
        }

        /*滑动结束后，重新设置值*/
        public void onStopTrackingTouch(SeekBar seekBar) {
            isSeekBarChanging = false;
            mediaPlayer.seekTo(seekBar.getProgress());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
      //  Toast.makeText(getActivity(),"detach",Toast.LENGTH_SHORT).show();
        mediaPlayer.stop();
    }
}
