package com.gameshock.newgameplay_upthetreasure;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class Mountains {
    Bitmap mountain1, mountain2, mountain3, mountain4, mountain5;
    int x = 0, y, width, height, mountainCounter = 1;

    int speed = 3;

    Mountains(Resources res) {
        mountain1 = BitmapFactory.decodeResource(res, R.drawable.tile);
        mountain2 = BitmapFactory.decodeResource(res, R.drawable.tile);
        mountain3 = BitmapFactory.decodeResource(res, R.drawable.tile);
        mountain4 = BitmapFactory.decodeResource(res, R.drawable.tile);
        mountain5 = BitmapFactory.decodeResource(res, R.drawable.tile);

        //
        width = mountain1.getWidth();
        height = mountain1.getHeight();

        mountain1 = Bitmap.createScaledBitmap(mountain1, width, height, false);
        mountain2 = Bitmap.createScaledBitmap(mountain2, width, height, false);
        mountain3 = Bitmap.createScaledBitmap(mountain3, width, height, false);
        mountain4 = Bitmap.createScaledBitmap(mountain4, width, height, false);
        mountain5 = Bitmap.createScaledBitmap(mountain5, width, height, false);

        //
        y = -height;
    }


    Bitmap getmountain() {

        if (mountainCounter == 1) {
            mountainCounter++;
            return mountain1;
        }

        if (mountainCounter == 2) {
            mountainCounter++;
            return mountain2;
        }

        if (mountainCounter == 3) {
            mountainCounter++;
            return mountain3;
        }
        if (mountainCounter == 4) {
            mountainCounter++;
            return mountain4;
        }

        mountainCounter = 1;

        return mountain5;
    }

    Rect getCollisionShape() {
        return new Rect(x, y, x + width, y + height);
    }
}
