package com.example.test.myrecorder;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecordingsFragment extends Fragment {


    public RecordingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recordings, container, false);
        Button btn_share =view.findViewById(R.id.btn_share);
        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyTool.shareMusic(getActivity());
               // MyTool.shareText(getActivity(),"hello");
            }
        });
        return view;


    }

}
