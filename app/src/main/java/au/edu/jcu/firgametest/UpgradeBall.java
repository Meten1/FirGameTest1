package au.edu.jcu.firgametest;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;

public class UpgradeBall {
    private PointF position;
    private int dirX;
    private int dirY;
    private final int boundingWidth;
    private final int boundingHeight;
    private final int speed;
    private int BUBBLE_SIZE = 50;
    Paint paint;

    UpgradeBall(int boundingWidth, int boundingHeight, int speed, int y,int dirX, int dirY) {
        this.boundingWidth = boundingWidth;
        this.boundingHeight = boundingHeight;
        this.speed = speed;
        position = new PointF();
        position.x = boundingWidth;
        position.y = y;
        this.dirX = dirX;
        this.dirY = dirY;
        paint = new Paint();
        paint.setColor(Color.BLUE);


    }

    void draw(Canvas canvas) {
        canvas.save();
        canvas.drawCircle(position.x,position.y,BUBBLE_SIZE,paint);
        canvas.restore();
    }

    void move(){
        position.x += (speed * dirX) * 0.1;
        position.y += (speed * dirY) * 0.1;
    }

    void checkCollide(){
        float x = position.x;
        float y = position.y;
//        if (x + BUBBLE_SIZE >= boundingWidth || x - BUBBLE_SIZE <= 0){
//            dirX *= -1;
//        } else if (y + BUBBLE_SIZE >= boundingHeight || y - BUBBLE_SIZE <= 0){
//            dirY *= -1;
//        }
        if ((x - BUBBLE_SIZE <= 0 && dirX < 0) || (x + BUBBLE_SIZE >= boundingWidth && dirX > 0)) {
            dirX *= -1;
        }
        if ((y - BUBBLE_SIZE <= 0 && dirY < 0) || (y + BUBBLE_SIZE >= boundingHeight && dirY > 0)) {
            dirY *= -1;
        }
    }

}
