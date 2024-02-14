package com.gameshock.newgameplay_upthetreasure;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.StrictMode;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.widget.Toast;

import java.security.SecureRandom;
import java.util.Random;

public class Gameview extends SurfaceView implements Runnable {

    int screenX, screenY;
    int lavaBoundry;
    public static float screenRatioX, screenRatioY;
    public float angle = 0;

    boolean isPlaying = false, isMovingCharacter = false, isCharacterRevolve = false, isCharacterShoot = false;
    //
    private Background background1;
    private CharacterMan character;
    private Mountains[] mountains;
    private Paint paint;

    Thread thread;
    Random random;
    Matrix matrix;
    private int radius = 100;
    float characterXPos, characterYPos;
    private int collidingMountainIndex = 0;

    public Gameview(Context context, int ScreenX, int ScreenY) {
        super(context);
        matrix = new Matrix();//matrix
        this.screenX = ScreenX;
        this.screenY = ScreenY;
        //
        screenRatioX = 1920f / screenX;
        screenRatioY = 1080f / screenY;

        background1 = new Background(screenX, screenY, getResources());
        character = new CharacterMan(this, getResources());

        //paint
        paint = new Paint();
        paint.setTextSize(128);
        paint.setColor(Color.WHITE);

        mountains = new Mountains[5];


        for (int i = 0; i < 5; i++) {
            mountains[i] = new Mountains(getResources());
            mountains[i].x = screenX / 2 - mountains[i].width / 2;
            mountains[i].y = (i + 1) * ScreenY / (5 + 1); // Initial positions above screen// 5 is the max mountains
        }


        random = new Random();
    }

    @Override
    public void run() {

        while (isPlaying) {
            draw();
            update();
            sleep();

        }
    }

    private void draw() {
        if (getHolder().getSurface().isValid()) {

            Canvas canvas = getHolder().lockCanvas();
            canvas.drawBitmap(background1.background, background1.x, background1.y, paint);

            if (!isCharacterRevolve) {
                revolveAroundTheMountain(canvas);
            } else {
                canvas.drawBitmap(character.flight1, characterXPos, characterYPos, paint);
            }
            //
            for (Mountains mountain : mountains)
                canvas.drawBitmap(mountain.getmountain(), mountain.x, mountain.y, paint);

            lavaBoundry = canvas.getHeight() - background1.lavaHeight;
            canvas.drawBitmap(background1.lava, background1.x, canvas.getHeight() - background1.lavaHeight, paint);
            getHolder().unlockCanvasAndPost(canvas);

        }
    }

    private void revolveAroundTheMountain(Canvas canvas) {

        // Character revolving around the first mountain
        if (collidingMountainIndex >= 0) {
            float mountainX = mountains[collidingMountainIndex].x + mountains[collidingMountainIndex].width / 2;
            float mountainY = mountains[collidingMountainIndex].y + mountains[collidingMountainIndex].height / 2;


            // Create a rotation matrix around the mountain's center
            characterXPos = mountainX + (int) (radius * Math.cos(angle)) - character.width / 2;
            characterYPos = mountainY + (int) (radius * Math.sin(angle)) - character.height / 2;

            // Update the angle for the next frame
            angle += 0.1f;

            // Apply rotation to the character drawable
            matrix.reset();
            matrix.postTranslate(characterXPos, characterYPos);
            matrix.postRotate((float) Math.toDegrees(angle), characterXPos + character.width / 2, characterYPos + character.height / 2);

            canvas.drawBitmap(character.flight1, matrix, paint);
        } else if (collidingMountainIndex == -1) {

        }
        //

    }


    private void update() {

        for (int i = 0; i < 5; i++) {
            mountains[i].y += mountains[i].speed;

            if (mountains[i].y > screenY) { // Change the condition to check if it's off the bottom
                mountains[i].y = -mountains[i].height; // Reset position above the screen
                mountains[i].x = screenX / 2 - getRandomX(mountains[i].width);
            }
        }


        //
        // Check if the character is out of bounds
        if (characterXPos < 0 || characterXPos > screenX || characterYPos < 0 || characterYPos > screenY || characterYPos > lavaBoundry) {
            gameOver();
        }


    }

    private int getCollidingMountainIndex() {
        // Find the index of the mountain with which the projectile collides
        // You can use more specific logic based on your game requirements

        for (int i = 0; i < mountains.length; i++) {
            Rect projectileRect = new Rect(
                    (int) characterXPos,
                    (int) characterYPos,
                    (int) (characterXPos + character.width),
                    (int) (characterYPos + character.height)
            );

            Rect mountainRect = new Rect(
                    (int) mountains[i].x,
                    (int) mountains[i].y,
                    (int) (mountains[i].x + mountains[i].width),
                    (int) (mountains[i].y + mountains[i].height)
            );

            if (Rect.intersects(projectileRect, mountainRect)) {
                // Collision detected, return the index of the colliding mountain
                return i;
            }
        }

        // No collision detected, return -1
        return -1;
    }

    private boolean checkProjectileMountainCollision() {
        // Assuming you have a projectile, adjust as needed
        // Check if the projectile's position overlaps with any Mountain object's boundaries
        // You can use simple rectangle overlapping checks or more advanced collision detection methods

        // Example simple rectangle overlapping check
        Rect projectileRect = new Rect(
                (int) characterXPos,
                (int) characterYPos,
                (int) (characterXPos + character.width),
                (int) (characterYPos + character.height)
        );

        for (int i = 0; i < mountains.length; i++) {
            Rect mountainRect = new Rect(
                    (int) mountains[i].x,
                    (int) mountains[i].y,
                    (int) (mountains[i].x + mountains[i].width),
                    (int) (mountains[i].y + mountains[i].height)
            );

            if (Rect.intersects(projectileRect, mountainRect)) {
                // Collision detected
                return true;
            }
        }

        // No collision detected
        return false;
    }


    private void gameOver() {
        // Stop the game
        isPlaying = false;

        // Show a toast message on the UI thread
        post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(), "Game Over", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public int getRandomX(int maxWidth) {
        // Check for invalid input to avoid errors
        if (maxWidth <= 0) {
            throw new IllegalArgumentException("maxWidth must be positive");
        }

        // Use Random or SecureRandom based on security requirements

        // Ensure the random number is within the valid range
        return random.nextInt(maxWidth);
    }

    private void sleep() {
        try {
            Thread.sleep(17);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    // pause and resume
    public void resume() {
        isPlaying = true;
        thread = new Thread(this);
        thread.start();

    }


    public void pause() {

        try {
            isPlaying = false;
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //ontouch Listener
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isMovingCharacter) {
            isCharacterRevolve = true;
            isMovingCharacter = true;
            moveCharacTowardsTangent(event.getX(), event.getY());
            angle = 0;
        }

        return true;
    }

    private void moveCharacTowardsTangent(float targetX, float targetY) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                float speed = 30; // Adjust the speed here
                // Calculate the tangent angle
                float dx = characterXPos - mountains[0].x - mountains[0].width / 2;
                float dy = characterYPos - mountains[0].y - mountains[0].height / 2;
                float tangentAngle = (float) Math.atan2(dy, dx);

                float moveX = (float) (speed * Math.cos(tangentAngle));
                float moveY = (float) (speed * Math.sin(tangentAngle));

                while (characterXPos > 0 && characterXPos < screenX && characterYPos > 0 && characterYPos < screenY) {
                    characterXPos += moveX;
                    characterYPos += moveY;
                    draw();

                    if(getCollidingMountainIndex() != collidingMountainIndex) {
                        if (checkProjectileMountainCollision()) {
                            // Collision detected, trigger character revolving

                            angle = 0;
                            // Store the colliding mountain's index for reference during revolving
                            collidingMountainIndex = getCollidingMountainIndex();
                            break;
                        }
                    }

                    try {
                        Thread.sleep(20); // Adjust the sleep time for smoother animation
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                isCharacterRevolve = false;
                angle = 0;
                isMovingCharacter = false;
            }
        }).start();
    }


}
