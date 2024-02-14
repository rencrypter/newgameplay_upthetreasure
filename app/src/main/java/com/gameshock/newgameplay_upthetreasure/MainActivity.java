package com.gameshock.newgameplay_upthetreasure;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Point;
import android.os.Bundle;
import android.view.WindowManager;

import com.gameshock.newgameplay_upthetreasure.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    Gameview gameview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Point point = new Point();
        getWindowManager().getDefaultDisplay().getSize(point);

        //set Gameview
        gameview = new Gameview(this, point.x, point.y);
        setContentView(gameview);

        //

    }

    //
    @Override
    protected void onPause() {
        super.onPause();
        gameview.pause();

    }

    @Override
    protected void onResume() {
        super.onResume();
        gameview.resume();
    }
}