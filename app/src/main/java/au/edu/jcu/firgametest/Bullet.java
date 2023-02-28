package au.edu.jcu.firgametest;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;

public class Bullet {
    private PointF position;

    private final int speed;
    private int BUBBLE_SIZE = 30;

    private int harm;
    Paint paint;

    Bullet(int speed, float x, float y, int harm) {
        this.speed = speed;
        position = new PointF();
        position.x = x;
        position.y = y;
        this.harm = harm;
        paint = new Paint();
        paint.setColor(Color.BLACK);
    }

    void draw(Canvas canvas) {

        canvas.save();
        canvas.drawCircle(position.x,position.y,BUBBLE_SIZE,paint);
        canvas.restore();
    }

    void move() {
        position.x += speed * 0.1;
    }

    PointF getPosition(){
        return position;
    }

    int getBUBBLE_SIZE() { return BUBBLE_SIZE; }

    int getHarm() { return harm; }
}
