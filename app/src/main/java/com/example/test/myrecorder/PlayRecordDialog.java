package com.example.test.myrecorder;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
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
    private ObjectAnimator discObjectAnimator,neddleObjectAnimator;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_recorder_player, null);
        String savedName = getArguments().getString("savedName");
        mediaPlayer = MediaPlayer.create(getActivity(), Uri.parse("file://" + savedName));
        mediaPlayer.start();
        String name =getArguments().getString("name");
        TextView textView_name = view.findViewById(R.id.tv_name);
        textView_name.setText(name+".mp3");
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
                        discObjectAnimator.pause();
 /*..................
                        功能代码1*/
                        pressPictureFlag = true;
                    } else {
                        btn_play.setImageResource(R.drawable.pause);
                        btn_play.setBackgroundColor(0xfff);
                        mediaPlayer.start();
                        discObjectAnimator.resume();
/*..................
                    功能代码2*/
                        pressPictureFlag = false;
                    }
                }
                return false;
            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                discObjectAnimator.pause();

            }
        });

        //最外部的半透明边线
        OvalShape ovalShape0 = new OvalShape();
        ShapeDrawable drawable0 = new ShapeDrawable(ovalShape0);
        drawable0.getPaint().setColor(0x10000000);
        drawable0.getPaint().setStyle(Paint.Style.FILL);
        drawable0.getPaint().setAntiAlias(true);

        //黑色唱片边框
        RoundedBitmapDrawable drawable1 = RoundedBitmapDrawableFactory.create(getResources(), BitmapFactory.decodeResource(getResources(), R.drawable.disc));
        drawable1.setCircular(true);
        drawable1.setAntiAlias(true);

        //内层黑色边线
        OvalShape ovalShape2 = new OvalShape();
        ShapeDrawable drawable2 = new ShapeDrawable(ovalShape2);
        drawable2.getPaint().setColor(Color.BLACK);
        drawable2.getPaint().setStyle(Paint.Style.FILL);
        drawable2.getPaint().setAntiAlias(true);

        //最里面的图像
        RoundedBitmapDrawable drawable3 = RoundedBitmapDrawableFactory.create(getResources(), BitmapFactory.decodeResource(getResources(), R.drawable.image3));
        drawable3.setCircular(true);
        drawable3.setAntiAlias(true);

        Drawable[] layers = new Drawable[4];
        layers[0] = drawable0;
        layers[1] = drawable1;
        layers[2] = drawable2;
        layers[3] = drawable3;

        LayerDrawable layerDrawable = new LayerDrawable(layers);

        int width = 10;
        //针对每一个图层进行填充，使得各个圆环之间相互有间隔，否则就重合成一个了。
        //layerDrawable.setLayerInset(0, width, width, width, width);
        layerDrawable.setLayerInset(1, width , width, width, width );
        layerDrawable.setLayerInset(2, width * 11, width * 11, width * 11, width * 11);
        layerDrawable.setLayerInset(3, width * 12, width * 12, width * 12, width * 12);

        final View discView = view.findViewById(R.id.myView);
        discView.setBackgroundDrawable(layerDrawable);



        discObjectAnimator = ObjectAnimator.ofFloat(discView, "rotation", 0, 360);
        discObjectAnimator.setDuration(20000);
        //使ObjectAnimator动画匀速平滑旋转
        discObjectAnimator.setInterpolator(new LinearInterpolator());
        //无限循环旋转
        discObjectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        discObjectAnimator.setRepeatMode(ValueAnimator.INFINITE);
        discObjectAnimator.start();
        discObjectAnimator.pause();
        discObjectAnimator.start();


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
