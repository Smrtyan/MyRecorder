package com.example.test.myrecorder;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.io.File;
import java.util.Calendar;

public class SaveFileDialog extends DialogFragment {
    SQLiteDatabase db;

    @Nullable
    @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.dialog_save_file,null);



        String name = getArguments().getString("filename");
        if (name== null)name = "";
        String durationSeconds = getArguments().getString("durationSeconds");
        final String fileName = name;
        EditText editText =view.findViewById(R.id.et_filename_to_save);
        editText.setText(name);
        Button btn_cancel =view.findViewById(R.id.btn_cancel_save);
        Button btn_save = view.findViewById(R.id.btn_save_file);
        btn_cancel.setOnClickListener(v->{
            File fileToBeDeleted = new File("/sdcard/Android/data/"+getActivity().getPackageName()+"/files/", fileName);
            fileToBeDeleted.delete();
            dismiss();
        });
        btn_save.setOnClickListener(v->{
            db = MainMenuActivity.getDB();
            String finalName =editText.getText().toString();
            ContentValues values = new ContentValues();
            values.put("savedName", "/sdcard/Android/data/"+getActivity().getPackageName()+"/files/"+fileName);
            values.put("displayName", finalName);
            values.put("isUploaded",0);
            values.put("durationSeconds",durationSeconds);
            values.put("recordedDate",Calendar.getInstance().getTime().toString());
            values.put("isDeleted",0);
            db.insert(SimpleDBHelper.MY_RECORD_TABLE,null,values);
            db.close();
            ((MainMenuActivity)getActivity()).updateRecordingFiles();
            dismiss();
        });
        return view;
    }
}
