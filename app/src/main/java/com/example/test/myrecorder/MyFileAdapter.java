package com.example.test.myrecorder;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MyFileAdapter  extends ArrayAdapter<MyListViewItem> {
    List<MyListViewItem> object ;
    Context context;
    public MyFileAdapter(@NonNull Context context,@NonNull List objects) {
        super(context, R.layout.layout_file_item, objects);
        this.object =objects;
        this.context = context;
    }

    @Override
    public int getCount() {
        return object.size();
    }

    @NonNull
    @Override
    public View getView(int position,  @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_file_item, null);
        MyListViewItem item = object.get(position);
        ((TextView)view.findViewById(R.id.tv_mp3_name)).setText((item.getDisplayName())+".mp3");
        ((TextView)view.findViewById(R.id.tv_mp3_duration)).setText((item.getDurationSeconds())+" s");
        ((TextView)view.findViewById(R.id.tv_mp3_date)).setText((item.getRecordedDate()));
       // Button button = view.findViewById(R.id.btn_play);
//        ((ViewGroup)view).setTag(item.getDisplayName());

        view.findViewById(R.id.imageButton_shareWechat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //upload file
//                SQLiteDatabase db;
//                db = MainMenuActivity.getDB();
//                Cursor cursor1 =db.query(SimpleDBHelper.MY_RECORD_TABLE,null,
//                        "displayName = '"+displayName.replace(".mp3","")+"' and isDeleted = 0",null,
//                        null,null,null,null);
//                String savedName,recordID;
//                if (cursor1.moveToFirst()) {
//                    savedName = cursor1.getString(cursor1.getColumnIndex("savedName"));
//                    recordID = cursor1.getString(cursor1.getColumnIndex("id"));
//
//                }
//                db.close();

                String POSTURL ="https://smrtyan.cn/upload.php";

                //token should load from sharedPreference
                String token="b6HetDy$+-@2";
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        File file = new File("/sdcard/lab1_sensors.pdf");
//                        // 普通参数
//                        HashMap<String , String> params = new HashMap<>();
//                        params.put("token", token);
//                        params.put("filename", item.getSavedName());
//                        params.put("duration", item.getDurationSeconds());
//                        item.getRecordedDate(
//                        params.put("recordDate", ));
//                        try {
//
//                            final String s =FileUpload.uploadForm(params, "file", file, "2.pdf", POSTURL);
//
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                }).start();
              //  SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
              //  Date date = simpleDateFormat.format(item.getRecordedDate());
                Toast.makeText(context,"share to wechat",Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}