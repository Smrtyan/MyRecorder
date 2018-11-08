package com.example.test.myrecorder;


import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;

import com.beardedhen.androidbootstrap.TypefaceProvider;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecordFragment extends Fragment {

    Chronometer chronometer;
    long tickCount = 0;
    boolean isRecording = false;
    public RecordFragment() {
        // Required empty public constructor
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
                    chronometer.stop();
                }else {
                    btn_start.setText("暂停");
                    chronometer.setBase(SystemClock.elapsedRealtime()-tickCount);
                    chronometer.start();

                }
                isRecording = !isRecording;

            }
        });
        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

        TypefaceProvider.registerDefaultIconSets();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_record, container, false);
        btn_init(view);
        return  view;
    }

}
