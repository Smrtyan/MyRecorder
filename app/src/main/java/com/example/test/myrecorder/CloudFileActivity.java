package com.example.test.myrecorder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CloudFileActivity extends AppCompatActivity {
    static List list = new ArrayList<CloudFileItem>();
    CloudFileAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloud_file);
        ListView listView = findViewById(R.id.lv_file_item);
        TextView tv_username =findViewById(R.id.tv_username);
        SharedPreferencesUtils helper = new SharedPreferencesUtils(this, "setting");
        String name = helper.getString("name");
        String token =  helper.getString("token");

        tv_username.setText(name);

        String URL = "https://smrtyan.cn/list.php?name="+name+"&token="+token;
        Log.v("upldt1",URL);
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
                            updateListView();
                            Log.v("CFA",""+jsonArray.length());
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }

                    }
                });

        adapter = new CloudFileAdapter(this,list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, v, position, id) -> {

        });
        }

    public void btn_logout(View view) {
        MainMenuActivity.isLoginIn = false;
        Toast.makeText(this,"Log out!",Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
   void updateListView(){
       adapter.notifyDataSetChanged();
   }
}

