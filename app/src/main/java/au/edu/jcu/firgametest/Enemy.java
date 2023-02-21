package au.edu.jcu.firgametest;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;

public class Enemy {
    private PointF position;
    private final int boundingWidth;
    private final int speed;
    private int BUBBLE_SIZE = 100;
    Paint paint;

    Enemy(int boundingWidth, int speed, int y) {
        this.boundingWidth = boundingWidth;
        this.speed = speed;
        position = new PointF();
        position.x = boundingWidth;
        position.y = y;
        paint = new Paint();
        paint.setColor(Color.YELLOW);
    }

    void draw(Canvas canvas) {
        canvas.save();
        canvas.drawCircle(position.x,position.y,BUBBLE_SIZE,paint);
        canvas.restore();
    }

    void move() {
        position.x -=  speed;
    }

    PointF getPosition(){
        return position;
    }

    int getBUBBLE_SIZE() { return BUBBLE_SIZE; }
}

