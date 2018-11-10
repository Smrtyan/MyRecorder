package com.example.test.myrecorder;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;

public class DeleteRecordDialog extends DialogFragment {
    SQLiteDatabase db;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_delete_dialog,null);
        Button btn_delete = view.findViewById(R.id.btn_delete_file);
        Button btn_cancel = view.findViewById(R.id.btn_cancel_delete_file);
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = MainMenuActivity.getDB();
                String savedName = getArguments().getString("savedName");
//                Cursor cursor1 =db.query(SimpleDBHelper.MY_RECORD_TABLE,null,
//                        "displayName = '"+displayName.replace(".mp3","")+"'",null,
//                        null,null,null,null);
                ContentValues contentValues = new ContentValues();
                contentValues.put("isDeleted","1");
                db.update(SimpleDBHelper.MY_RECORD_TABLE,contentValues,"savedName = ?",new String[]{ savedName });
                File fileToBeDeleted = new File(savedName);
                fileToBeDeleted.delete();
                ((MainMenuActivity)getActivity()).updateRecordingFiles();
                db.close();
                dismiss();

                Toast.makeText(getActivity(),"deleted",Toast.LENGTH_SHORT).show();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return  view;
    }
}
