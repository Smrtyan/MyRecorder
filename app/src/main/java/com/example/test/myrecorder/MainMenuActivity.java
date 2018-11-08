package com.example.test.myrecorder;

import android.Manifest;
import android.app.FragmentTransaction;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.TypefaceProvider;
import com.github.pwittchen.swipe.library.rx2.Swipe;
import com.github.pwittchen.swipe.library.rx2.SwipeListener;

public class MainMenuActivity extends AppCompatActivity {
    Fragment mRecordfragment;
    Fragment mRecordingsfragment;
    private Swipe swipe;
    BottomNavigationView navigation;

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        swipe.dispatchTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

    void initBottomNavigatorAndFragment(){

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        android.support.v4.app.FragmentTransaction  fragmentTransaction = getSupportFragmentManager().beginTransaction();
        mRecordfragment =       getSupportFragmentManager().findFragmentById(R.id.sub_record);
        mRecordingsfragment =   getSupportFragmentManager().findFragmentById(R.id.sub_recordings);
        fragmentTransaction.hide(mRecordingsfragment).commit();
    }
    void initSwipe(){
        //check permission
        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
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
        initBottomNavigatorAndFragment();
        initSwipe();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        init();

    }

}
