package com.example.test.myrecorder;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.TypefaceProvider;
import com.github.pwittchen.swipe.library.rx2.Swipe;
import com.github.pwittchen.swipe.library.rx2.SwipeListener;

import me.itangqi.waveloadingview.WaveLoadingView;

public class MainMenuActivity extends AppCompatActivity{
    private static SimpleDBHelper dbHelper;
    private static int DB_VERSION = 1;
    private Swipe swipe;
    BottomNavigationView navigation;
    static RecordFragment mRecordfragment;
    static RecordingsFragment mRecordingsfragment;
    static Boolean isLoginIn = false;

    public static SQLiteDatabase getDB() {
        return dbHelper.getWritableDatabase();
    }


    private boolean checkIfAlreadyhavePermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }


    private void requestForSpecificPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.GET_ACCOUNTS, Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.FOREGROUND_SERVICE,Manifest.permission.INTERNET,Manifest.permission.READ_PHONE_STATE}, 101);
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 101:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }





    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        swipe.dispatchTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

    void initBottomNavigatorAndFragment(){

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        android.support.v4.app.FragmentTransaction  fragmentTransaction = getSupportFragmentManager().beginTransaction();
        mRecordfragment = (RecordFragment) getSupportFragmentManager().findFragmentById(R.id.sub_record);
        mRecordingsfragment = (RecordingsFragment) getSupportFragmentManager().findFragmentById(R.id.sub_recordings);
        fragmentTransaction.hide(mRecordingsfragment).commit();
    }
    void initSwipe(){
        //check permission
        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        swipe = new Swipe();

        swipe.setListener(new SwipeListener() {

            @Override
            public void onSwipingLeft(MotionEvent event) {

            }

            @Override
            public boolean onSwipedLeft(MotionEvent event) {
                android.support.v4.app.FragmentTransaction  fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.hide(mRecordfragment);
                fragmentTransaction.show(mRecordingsfragment);
                //1 means R.id.sub_recordings
                MenuItem menuItem = navigation.getMenu().getItem(1);
                menuItem.setChecked(true);
                fragmentTransaction.commit();
                return true;
            }

            @Override
            public void onSwipingRight(MotionEvent event) {

            }

            @Override
            public boolean onSwipedRight(MotionEvent event) {
                 android.support.v4.app.FragmentTransaction  fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.hide(mRecordingsfragment);
                fragmentTransaction.show(mRecordfragment);
                //0 means R.id.sub_record
                MenuItem menuItem = navigation.getMenu().getItem(0);
                menuItem.setChecked(true);
                fragmentTransaction.commit();
                return true;
            }

            @Override
            public void onSwipingUp(MotionEvent event) {

            }

            @Override
            public boolean onSwipedUp(MotionEvent event) {
                return false;
            }

            @Override
            public void onSwipingDown(MotionEvent event) {

            }

            @Override
            public boolean onSwipedDown(MotionEvent event) {
                return false;
            }
        });
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            boolean res = false;
            android.support.v4.app.FragmentTransaction  fragmentTransaction = getSupportFragmentManager().beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation_record:
                    fragmentTransaction.hide(mRecordingsfragment);
                    fragmentTransaction.show(mRecordfragment);
                    res = true;
                    break;
                case R.id.navigation_recordings:
                    fragmentTransaction.hide(mRecordfragment);
                    fragmentTransaction.show(mRecordingsfragment);
                    res = true;
                    break;
            }
            fragmentTransaction.commit();
            return res;
        }

    };
    void init(){
        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (!checkIfAlreadyhavePermission()){
            requestForSpecificPermission();
        }
        initBottomNavigatorAndFragment();
        initSwipe();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        SharedPreferencesUtils helper = new SharedPreferencesUtils(this, "setting");
         isLoginIn = helper.getBoolean("isLoginIn", false);

        dbHelper =new SimpleDBHelper(this, DB_VERSION);
        init();
        //stop wave loading view
        WaveLoadingView mWaveLoadingView = (WaveLoadingView) findViewById(R.id.waveLoadingView);
        mWaveLoadingView.setAnimDuration(0);
        mWaveLoadingView.pauseAnimation();
        mWaveLoadingView.resumeAnimation();
        mWaveLoadingView.cancelAnimation();
        mWaveLoadingView.startAnimation();
//        mWaveLoadingView.setShapeType(WaveLoadingView.ShapeType.CIRCLE);
//        mWaveLoadingView.setTopTitle("Top Title");
//        mWaveLoadingView.setCenterTitleColor(Color.GRAY);
//        mWaveLoadingView.setBottomTitleSize(18);
//        mWaveLoadingView.setProgressValue(80);
//        mWaveLoadingView.setBorderWidth(10);
//        mWaveLoadingView.setAmplitudeRatio(60);
//        mWaveLoadingView.setWaveColor(Color.GRAY);
//        mWaveLoadingView.setBorderColor(Color.GRAY);
//        mWaveLoadingView.setTopTitleStrokeColor(Color.BLUE);
//        mWaveLoadingView.setTopTitleStrokeWidth(3);

//        mWaveLoadingView.pauseAnimation();
//        mWaveLoadingView.resumeAnimation();
//      /  mWaveLoadingView.cancelAnimation();
       // mWaveLoadingView.startAnimation();



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferencesUtils helper = new SharedPreferencesUtils(this, "setting");
        helper.putValues(new SharedPreferencesUtils.ContentValue("isLoginIn", isLoginIn));


    }

    static void updateRecordingFiles(){
        mRecordingsfragment.updateListView();
    }

}
