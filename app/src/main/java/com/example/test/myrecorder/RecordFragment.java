package com.example.test.myrecorder;


import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;

import com.beardedhen.androidbootstrap.TypefaceProvider;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecordFragment extends Fragment {

    Chronometer chronometer;
    long tickCount = 0;
    boolean isRecording = false;
    String FILE_SAVED_DIRECTORY;
    private MediaRecorder recorder;
    public RecordFragment() {
        // Required empty public constructor
    }

    String getNamewithTime(){

        Date date = Calendar.getInstance().getTime();
        String name = FILE_SAVED_DIRECTORY+android.text.format.DateFormat.format("yyyy-MM-dd-hh_mm_ss",date).toString()+".m4a";
        Log.v("mydatestring",  name);
        return name;
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

        final Button btn_start = v.findViewById(R.id.btn_start);
        final Button btn_stop  = v.findViewById(R.id.btn_stop);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isRecording) {
                        tickCount = SystemClock.elapsedRealtime() - chronometer.getBase();
                        btn_start.setText("继续");
                        recorder.pause();
                        chronometer.stop();

                }else {

                    try {
                        if (btn_start.getText().equals("开始")){
                            recorderInit();
                            recorder.prepare();
                            recorder.start();   // Recording is now started
                        }else {
                            recorder.resume();
                        }
                        btn_start.setText("暂停");
                        chronometer.setBase(SystemClock.elapsedRealtime()-tickCount);
                        chronometer.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
                isRecording = !isRecording;

            }
        });
        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recorder.stop();
                isRecording = false;
                chronometer.stop();
                tickCount = 0;
                btn_start.setText("开始");

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
        btn_init(view);
        return  view;
    }

}
