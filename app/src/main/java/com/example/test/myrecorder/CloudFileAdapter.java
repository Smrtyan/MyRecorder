package com.example.test.myrecorder;

        import android.content.ContentValues;
        import android.content.Context;
        import android.database.sqlite.SQLiteDatabase;
        import android.provider.ContactsContract;
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

        import com.koushikdutta.async.future.FutureCallback;
        import com.koushikdutta.ion.Ion;

        import java.io.File;
        import java.util.Calendar;
        import java.util.List;

public class CloudFileAdapter extends ArrayAdapter<CloudFileItem> {
    List<CloudFileItem> object;
    Context context;

    public CloudFileAdapter( @NonNull Context context,  @NonNull List<CloudFileItem> objects) {
        super(context, R.layout.layout_cloud_file_item, objects);
        this.object = objects;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position,  @Nullable View convertView,  @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_cloud_file_item, null);
        CloudFileItem item = object.get(position);
        TextView tv_mp3name = view.findViewById(R.id.tv_mp3_name);
        TextView tv_duration = view.findViewById(R.id.tv_mp3_duration);
        TextView tv_recordedDate = view.findViewById(R.id.tv_mp3_date);
        ImageButton ib_download = view.findViewById(R.id.ib_download);
        tv_mp3name.setText(item.getDisplayName());
        tv_duration.setText(item.getDurationSeconds()+" s");
        tv_recordedDate.setText(item.getRecordedDate());
        if(!item.getIsDownloaded()){
            ib_download.setClickable(true);
            ib_download.setImageResource(R.drawable.cloud_download);
            ib_download.setBackgroundColor(0xfff);
            ib_download.setOnClickListener(v->{
                Log.v("CFA","click");
                Toast.makeText(context,"Downloading:"+item.getDisplayName(),Toast.LENGTH_SHORT).show();
                String FILE_SAVED_DIRECTORY = "/sdcard/Android/data/"+context.getPackageName()+"/files/";
                File file = new File(FILE_SAVED_DIRECTORY);
                if (!file.exists()){
                    file.mkdirs();
                }
                SQLiteDatabase db = MainMenuActivity.getDB();
                String filename =item.getDisplayName();
                ContentValues values = new ContentValues();
                values.put("savedName", "/sdcard/Android/data/"+context.getPackageName()+"/files/"+filename);
                values.put("displayName", filename);
                values.put("isUploaded",1);
                values.put("durationSeconds",item.getDurationSeconds());
                values.put("recordedDate",item.getRecordedDate());
                values.put("isDeleted",0);
                db.insert(SimpleDBHelper.MY_RECORD_TABLE,null,values);
                db.close();
                MainMenuActivity.updateRecordingFiles();
                SharedPreferencesUtils helper = new SharedPreferencesUtils(context, "setting");
                String token =  helper.getString("token");
                Ion.with(context)
                        .load("https://smrtyan.cn/share.php?filename="+item.getDisplayName()+"&token="+token)
                        .write(new File(FILE_SAVED_DIRECTORY+filename))
                        .setCallback(new FutureCallback<File>() {
                            @Override
                            public void onCompleted(Exception e, File file) {
                                Toast.makeText(context,"Downloaded:"+item.getDisplayName(),Toast.LENGTH_LONG).show();
                                item.isDownloaded =true;
                                ((CloudFileActivity)context).updateListView();
                            }
                        });

            });
        }
//        return super.getView(position, convertView, parent);
        return view;
    }
}
