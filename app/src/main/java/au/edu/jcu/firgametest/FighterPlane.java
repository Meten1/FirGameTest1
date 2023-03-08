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
    private int bulletsNum;
    Paint paint;
    private double harmNum = 1.0;

    FighterPlane(int boundingWidth, int x, int y, int harm) {
        this.boundingWidth = boundingWidth;
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

    void update(String type){
        if (type.equals("BulletsNum") && bulletsNum < 3){
            bulletsNum += 1;
        } else if (type.equals("Harm")){
            harm *= 1.1;
        }
    }

    int getHarm(){
        return harm;
    }




}
