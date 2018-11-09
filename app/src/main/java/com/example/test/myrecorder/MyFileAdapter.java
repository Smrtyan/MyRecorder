package com.example.test.myrecorder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

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
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_file_item, null);
        MyListViewItem item = object.get(position);
        ((TextView)view.findViewById(R.id.tv_mp3_name)).setText((item.getDisplayName())+".mp3");
        ((TextView)view.findViewById(R.id.tv_mp3_duration)).setText((item.getDurationSeconds())+" s");
        ((TextView)view.findViewById(R.id.tv_mp3_date)).setText((item.getRecordedDate()));
        Button button = view.findViewById(R.id.btn_play);

        return view;
    }
}