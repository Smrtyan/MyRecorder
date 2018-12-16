package com.example.test.myrecorder;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class RecordingsFragment extends Fragment {
    public static Set set = new HashSet<String>();
    List list = new ArrayList<LocalFileItem>();
    ListView listView;
    LocalFileAdapter localFileAdapter;
    public RecordingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recordings, container, false);
//        Button btn_share =view.findViewById(R.id.btn_share);
//        btn_share.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                MyTool.shareMusic(getActivity());
//               // MyTool.shareText(getActivity(),"hello");
//            }
//        });
        listView =view.findViewById(R.id.lv_file_item);

        localFileAdapter = new LocalFileAdapter(getActivity(),list);

        listView.setAdapter(localFileAdapter);
        listView.setOnItemClickListener((parent, v, position, id) -> {
            SQLiteDatabase db;
            db = MainMenuActivity.getDB();
            String displayName =((LocalFileItem)list.get(position)).getDisplayName();
            Cursor cursor1 =db.query(SimpleDBHelper.MY_RECORD_TABLE,null,
                    "displayName = '"+displayName.replace(".mp3","")+"'",null,
                    null,null,null,null);
            String savedName;
            if (cursor1.moveToFirst()) {
                savedName = cursor1.getString(cursor1.getColumnIndex("savedName"));
                Bundle args = new Bundle();
                args.putString("savedName",savedName);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                PlayRecordDialog dialog = new PlayRecordDialog();
                dialog.setArguments(args);
                dialog.show(transaction,"recorder player dialog");
            }else {
                Toast.makeText(getActivity(),"no file selected",Toast.LENGTH_SHORT);

            }
            db.close();

        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                SQLiteDatabase db;
                db = MainMenuActivity.getDB();
                String displayName =((LocalFileItem)list.get(position)).getDisplayName();
                Cursor cursor1 =db.query(SimpleDBHelper.MY_RECORD_TABLE,null,
                        "displayName = '"+displayName.replace(".mp3","")+"' and isDeleted = 0",null,
                        null,null,null,null);
                String savedName,recordID;
                if (cursor1.moveToFirst()) {
                    savedName = cursor1.getString(cursor1.getColumnIndex("savedName"));
                    recordID = cursor1.getString(cursor1.getColumnIndex("id"));
                    Bundle args = new Bundle();
                    args.putString("savedName",savedName);
                    args.putString("id",recordID);
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    DeleteRecordDialog dialog = new DeleteRecordDialog();
                    dialog.setArguments(args);
                    dialog.show(transaction,"recorder delete dialog");
                }else {
                    Toast.makeText(getActivity(),"no file selected",Toast.LENGTH_SHORT).show();

                }
                db.close();
                return true;
            }
        });

        ImageButton ib_upload = view.findViewById(R.id.ib_upload);
        ib_upload.setOnClickListener(v->{
            Intent intent = new Intent(getActivity(),CloudFileActivity.class);
            startActivity(intent);
        });

        return view;


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        updateListView();
    }
    void updateListView(){
        SQLiteDatabase db;
        db = MainMenuActivity.getDB();
        Cursor cursor =db.query(SimpleDBHelper.MY_RECORD_TABLE, null, "isDeleted = ? ",
                new String[]{ "0" }, null, null, "recordedDate", "");
        if (cursor.moveToFirst()) {
            list.clear();
            set.clear();
            do {

                String displayName = cursor.getString(cursor.getColumnIndex("displayName"));
                String durationSeconds = cursor.getString(cursor.getColumnIndex("durationSeconds"));
                String recordedDate = cursor.getString(cursor.getColumnIndex("recordedDate"));
                String savedName = cursor.getString(cursor.getColumnIndex("savedName"));
                list.add(new LocalFileItem(savedName,displayName,durationSeconds,recordedDate));
                set.add(displayName);
            } while (cursor.moveToNext());
            localFileAdapter.notifyDataSetChanged();
            cursor.close();

        }
        db.close();
    }

}
