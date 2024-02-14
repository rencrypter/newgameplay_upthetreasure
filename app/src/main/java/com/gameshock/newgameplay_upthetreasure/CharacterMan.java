package com.gameshock.newgameplay_upthetreasure;

import static com.gameshock.newgameplay_upthetreasure.Gameview.screenRatioX;
import static com.gameshock.newgameplay_upthetreasure.Gameview.screenRatioY;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class CharacterMan {

    Gameview gameView;
    Bitmap flight1, flight2;
    float x, y;
    int width, height;


    CharacterMan(Gameview gameView, Resources res) {

        this.gameView = gameView;

        flight1 = BitmapFactory.decodeResource(res, R.drawable.men1_1);
        flight2 = BitmapFactory.decodeResource(res, R.drawable.men1_2);

        width = flight1.getWidth();
        height = flight1.getHeight();
        x = width / 2;
        y = height / 2;
        flight1 = Bitmap.createScaledBitmap(flight1, width, height, false);
        flight2 = Bitmap.createScaledBitmap(flight2, width, height, false);


    }

}
