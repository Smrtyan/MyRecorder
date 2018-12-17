package com.example.test.myrecorder;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class LocalFileAdapter extends ArrayAdapter<LocalFileItem> {
    List<LocalFileItem> object ;
    Context context;
    public LocalFileAdapter(@NonNull Context context, @NonNull List objects) {
        super(context, R.layout.layout_local_file_item, objects);
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
        View view = inflater.inflate(R.layout.layout_local_file_item, null);
        LocalFileItem item = object.get(position);
        ((TextView)view.findViewById(R.id.tv_mp3_name)).setText((item.getDisplayName())+".mp3");
        ((TextView)view.findViewById(R.id.tv_mp3_duration)).setText((item.getDurationSeconds())+" s");
        ((TextView)view.findViewById(R.id.tv_mp3_date)).setText((item.getRecordedDate()));
       // Button button = view.findViewById(R.id.btn_play);
//        ((ViewGroup)view).setTag(item.getDisplayName());
        ImageButton ib_upload =view.findViewById(R.id.ib_upload);
        ib_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String POSTURL ="https://smrtyan.cn/upload.php";

                //token should load from sharedPreference
                String token="b6HetDy$+-@2";

                SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
                SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = null;

                try {
                    date = simpleDateFormat1.parse(item.getRecordedDate());
                    final String d=simpleDateFormat2.format(date);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            File file = new File(item.getSavedName());

                            HashMap<String , String> params = new HashMap<>();
                            params.put("token", token);
                            params.put("filename", item.getSavedName());
                            params.put("duration", item.getDurationSeconds());
                            params.put("recordDate",d );
                            try {

                                final String s =FileUpload.uploadForm(params, "file", file, item.getDisplayName(), POSTURL);

                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }).start();
                    SQLiteDatabase db;
                    db = MainMenuActivity.getDB();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("isUploaded","1");
                    db.update(SimpleDBHelper.MY_RECORD_TABLE,contentValues,"savedName = '"+item.getSavedName()+"'",null);
                    db.close();
                    MainMenuActivity.updateRecordingFiles();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
        Log.v("upldt",""+item.getDisplayName()+","+item.getIsUploaded());
        if(item.getIsUploaded()==0){
            ib_upload.setClickable(true);
            ib_upload.setImageResource(R.drawable.cloud_upload);

        }else {

        }
        ib_upload.setBackgroundColor(0xfff);


        view.findViewById(R.id.imageButton_shareWechat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                   Toast.makeText(context,"share to wechat",Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}