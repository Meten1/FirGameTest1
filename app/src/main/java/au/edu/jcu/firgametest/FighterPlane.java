package au.edu.jcu.firgametest;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;

public class FighterPlane {
    private PointF position;
    private final int boundingWidth;
    private int BUBBLE_SIZE;

    private int harm;
    private int bulletsNum;
    Paint paint;

    FighterPlane(int boundingWidth, int BUBBLE_SIZE, float x, float y, int harm) {
        this.boundingWidth = boundingWidth;
        this.BUBBLE_SIZE = BUBBLE_SIZE;
        position = new PointF();
        position.x = x;
        position.y = y;
        this.harm = harm;
        paint = new Paint();
        paint.setColor(Color.BLACK);
        bulletsNum = 1;
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

    public int getBulletsNum() {
        return bulletsNum;
    }

    void update(){
        if (bulletsNum < 3){
            bulletsNum += 1;
        }
    }

    int getHarm(){
        return harm;
    }




}