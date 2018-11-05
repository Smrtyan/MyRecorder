package com.example.test.myrecorder;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class MainMenuActivity extends AppCompatActivity {
    Fragment mRecordfragment;
    Fragment mRecordingsfragment;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        android.support.v4.app.FragmentTransaction  fragmentTransaction = getSupportFragmentManager().beginTransaction();
        mRecordfragment =       getSupportFragmentManager().findFragmentById(R.id.sub_record);
        mRecordingsfragment =   getSupportFragmentManager().findFragmentById(R.id.sub_recordings);
        fragmentTransaction.hide(mRecordingsfragment).commit();

    }

}
