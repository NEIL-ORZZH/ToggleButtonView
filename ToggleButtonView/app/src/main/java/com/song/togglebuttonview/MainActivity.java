package com.song.togglebuttonview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.song.libs.ToggleButtonView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((ToggleButtonView)findViewById(R.id.toggleButton)).setToggleButton(true);
        ((ToggleButtonView)findViewById(R.id.toggleButton)).setOnToggleButtonChangedListener(new ToggleButtonView.OnToggleButtonChanged() {
            @Override
            public void onToggle(boolean toggle) {
                Log.i(TAG, toggle + "");
            }
        });
    }

}
