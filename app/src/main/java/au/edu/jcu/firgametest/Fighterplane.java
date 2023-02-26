package au.edu.jcu.firgametest;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;

public class Fighterplane {
    private PointF position;
    private final int boundingWidth;
    private int BUBBLE_SIZE = 100;

    private int harm;
    Paint paint;

    Fighterplane(int boundingWidth,int x, int y, int harm) {
        this.boundingWidth = boundingWidth;
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

    void move(int xAdd, int yAdd) {
        position.x += xAdd;
        position.y += yAdd;
    }

    PointF getPosition(){
        return position;
    }

    int getBUBBLE_SIZE() { return BUBBLE_SIZE; }




}
