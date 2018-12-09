package com.example.test.myrecorder;


import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.TypefaceProvider;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import me.itangqi.waveloadingview.WaveLoadingView;


public class RecordFragment extends Fragment {

    Chronometer chronometer;
    long tickCount = 0;
    boolean isRecording = false;
    boolean isStarted = false;
    String FILE_SAVED_DIRECTORY;
    private MediaRecorder recorder;
    String FILENAME;
    LinearLayout ll_recorder_control ;
    ImageButton imageButton_start_button;
    ImageButton imageButton_stop_button;
    int startButtonResourceId = R.drawable.play;
    public RecordFragment() {
        // Required empty public constructor
    }

    String getNamewithTime(){

        Date date = Calendar.getInstance().getTime();
        String name = android.text.format.DateFormat.format("yyyyMMddhhmmss",date).toString()+String.format("%06d",new Random().nextInt(100000));
        Log.v("mydatestring", FILE_SAVED_DIRECTORY+ name);
        FILENAME = name;
        return FILE_SAVED_DIRECTORY+name;
    }
    void recorderInit(){
        recorder = new MediaRecorder();
        recorder.reset();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        recorder.setAudioSamplingRate(44100);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        recorder.setAudioEncodingBitRate(96000);
        recorder.setOutputFile(getNamewithTime());
//        recorder.stop();
//        recorder.reset();   // You can reuse the object by going back to setAudioSource() step
//        recorder.release(); // Now the object cannot be reused
    }
    void btn_init(View v){
        chronometer = (Chronometer) v.findViewById(R.id.chronometer);
        chronometer.setFormat("%s");

//        final Button btn_start = v.findViewById(R.id.btn_start);
//        final Button btn_stop  = v.findViewById(R.id.btn_stop);
        imageButton_start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isRecording) {
                        tickCount = SystemClock.elapsedRealtime() - chronometer.getBase();
                      //  btn_start.setText("继续");
                        startButtonResourceId =R.drawable.play;
                        imageButton_start_button.setImageResource(startButtonResourceId);
                        WaveLoadingView mWaveLoadingView = (WaveLoadingView) getActivity().findViewById(R.id.waveLoadingView);
                        //stop wave loading view
                        mWaveLoadingView.setAnimDuration(0);
                    mWaveLoadingView.pauseAnimation();
                    mWaveLoadingView.resumeAnimation();
                    mWaveLoadingView.cancelAnimation();
                    mWaveLoadingView.startAnimation();
                        recorder.pause();
                        chronometer.stop();

                }else {

                    try {
                        if (startButtonResourceId == R.drawable.play && !isStarted){
                            isStarted = true;
                            startButtonResourceId =R.drawable.pause;
                            imageButton_start_button.setImageResource(startButtonResourceId);

                            recorderInit();
                            recorder.prepare();
                            recorder.start();   // Recording is now started
                            ll_recorder_control.addView(imageButton_stop_button);
                        }else {
                            startButtonResourceId =R.drawable.play;
                            imageButton_start_button.setImageResource(startButtonResourceId);
                            recorder.resume();
                        }
                       // btn_start.setText("暂停");
                        startButtonResourceId =R.drawable.pause;
                        imageButton_start_button.setImageResource(startButtonResourceId);
                        //start view loading view
                        WaveLoadingView mWaveLoadingView = (WaveLoadingView) getActivity().findViewById(R.id.waveLoadingView);
                        mWaveLoadingView.setAnimDuration(3000);
                        mWaveLoadingView.pauseAnimation();
                        mWaveLoadingView.resumeAnimation();
                        mWaveLoadingView.cancelAnimation();
                        mWaveLoadingView.startAnimation();
                        chronometer.setBase(SystemClock.elapsedRealtime()-tickCount);
                        chronometer.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
                isRecording = !isRecording;

            }
        });
        imageButton_stop_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                recorder.stop();
                isRecording = false;
                chronometer.stop();
                recorder.release(); // Now the object cannot be reused
                tickCount = 0;

                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                SaveFileDialog saveFileDialog = new SaveFileDialog();
                saveFileDialog.setStyle(saveFileDialog.STYLE_NO_FRAME, 0);
                Bundle args = new Bundle();
                args.putString("filename", FILENAME);
                String timeArray [] =chronometer.getText().toString().split(":");
                int duration = Integer.parseInt(timeArray[0])*60+Integer.parseInt(timeArray[1]);
                args.putString("durationSeconds",duration+"");
                saveFileDialog.setArguments(args);
                saveFileDialog.show(transaction, "save file dialog");
               // btn_start.setText("开始");
                startButtonResourceId =R.drawable.play;
                imageButton_start_button.setImageResource(startButtonResourceId);
                //stop wave loading view
                WaveLoadingView mWaveLoadingView = (WaveLoadingView) getActivity().findViewById(R.id.waveLoadingView);
                mWaveLoadingView.setAnimDuration(0);
                mWaveLoadingView.pauseAnimation();
                mWaveLoadingView.resumeAnimation();
                mWaveLoadingView.cancelAnimation();
                mWaveLoadingView.startAnimation();
                chronometer.setText("00:00");
                isStarted = false;
                ll_recorder_control.removeAllViews();
                ll_recorder_control.addView(imageButton_start_button);

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FILE_SAVED_DIRECTORY = "/sdcard/Android/data/"+getActivity().getPackageName()+"/files/";
        File file = new File(FILE_SAVED_DIRECTORY);
        if (!file.exists()){
            file.mkdirs();
        }
        getNamewithTime();
        TypefaceProvider.registerDefaultIconSets();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_record, container, false);

        ll_recorder_control = view.findViewById(R.id.lineagelayout_recorder_controller);
        imageButton_start_button = new ImageButton(getActivity());
        imageButton_start_button.setImageResource(R.drawable.play);
        imageButton_start_button.setBackgroundColor(0xfff);

        imageButton_stop_button = new ImageButton(getActivity());
        imageButton_stop_button.setBackgroundColor(0xfff);
        imageButton_stop_button.setImageResource(R.drawable.stop);
        ll_recorder_control.addView(imageButton_start_button);

        btn_init(view);
        return  view;
    }

}
