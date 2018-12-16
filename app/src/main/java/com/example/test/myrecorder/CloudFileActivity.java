package com.example.test.myrecorder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CloudFileActivity extends AppCompatActivity {
    List list = new ArrayList<CloudFileItem>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloud_file);
        ListView listView = findViewById(R.id.lv_file_item);
        CloudFileAdapter adapter = new CloudFileAdapter(this,list);
        String URL = "https://smrtyan.cn/list.php?name=admin&token=sSzN{t:2(:W";
        Ion.with(this)
                .load(URL)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {

                        try {
                            String filename;
                            String duration;
                            String recordDate;
                            boolean isDownloaded;

                            JSONArray jsonArray = new JSONArray(result);
                            list.clear();
                            for(int i = 0;i<jsonArray.length();i++){
                                JSONObject obj = (JSONObject) jsonArray.get(i);
                                filename = obj.getString("filename");
                                duration = obj.getString("duration");
                                recordDate = obj.getString("recordDate");
                                isDownloaded = RecordingsFragment.set.contains(filename);
                                list.add(new CloudFileItem(filename,duration,recordDate,isDownloaded));
                                Log.v("CFA",""+obj.getString("filename"));
                            }
                            adapter.notifyDataSetChanged();
                            Log.v("CFA",""+jsonArray.length());
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }

                    }
                });


        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, v, position, id) -> {

        });
        }
    }

