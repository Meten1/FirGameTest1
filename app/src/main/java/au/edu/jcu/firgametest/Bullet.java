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
    private final int boundingWidth;
    private int xDir;
    private final int speed;
    private int BUBBLE_SIZE = 100;
    Paint paint;

    Bullet(int boundingWidth,int speed, int x, int y) {
        this.boundingWidth = boundingWidth;
        this.speed = speed;
        position = new PointF();
        position.x = x;
        position.y = y;
        paint = new Paint();
        paint.setColor(Color.BLACK);
    }

    void draw(Canvas canvas) {

        canvas.save();
        canvas.drawCircle(position.x,position.y,BUBBLE_SIZE,paint);
        canvas.restore();
    }

    void move() {
        position.x +=  speed;
    }

    PointF getPosition(){
        return position;
    }
}
