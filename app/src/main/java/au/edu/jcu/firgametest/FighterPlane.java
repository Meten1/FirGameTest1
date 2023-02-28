package au.edu.jcu.firgametest;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;

public class FighterPlane {
    private PointF position;
    private final int boundingWidth;
    private int BUBBLE_SIZE = 100;

    private int harm;
    Paint paint;

    FighterPlane(int boundingWidth, int x, int y, int harm) {
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

    void update(){
        System.out.println("-----------------------升级");
    }

    int getHarm(){
        return harm;
    }




}
