package com.example.test.myrecorder;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
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

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        swipe.dispatchTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

    void initBottomNavigatorAndFragment(){

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        android.support.v4.app.FragmentTransaction  fragmentTransaction = getSupportFragmentManager().beginTransaction();
        mRecordfragment =       getSupportFragmentManager().findFragmentById(R.id.sub_record);
        mRecordingsfragment =   getSupportFragmentManager().findFragmentById(R.id.sub_recordings);
        fragmentTransaction.hide(mRecordingsfragment).commit();
    }
    void initSwipe(){
        swipe = new Swipe();

        swipe.setListener(new SwipeListener() {

            @Override
            public void onSwipingLeft(MotionEvent event) {

            }

            @Override
            public boolean onSwipedLeft(MotionEvent event) {
                android.support.v4.app.FragmentTransaction  fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.hide(mRecordingsfragment);
                fragmentTransaction.show(mRecordfragment);
                fragmentTransaction.commit();
                return true;
            }

            @Override
            public void onSwipingRight(MotionEvent event) {

            }

            @Override
            public boolean onSwipedRight(MotionEvent event) {
                android.support.v4.app.FragmentTransaction  fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.hide(mRecordfragment);
                fragmentTransaction.show(mRecordingsfragment);
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
