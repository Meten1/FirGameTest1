package au.edu.jcu.firgametest;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;
import java.util.Random;

public class OuterspaceView extends View {
    private PointF[] starField;
    private List<Bullet> bullets;
    private List<Enemy> enemies;

    public OuterspaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setBackgroundColor(Color.WHITE);


    }

    @Override
    protected void onSizeChanged(int newWidth, int newHeight,
                                 int oldWidth, int oldHeight) {
        super.onSizeChanged(newWidth, newHeight, oldWidth, oldHeight);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // Draw foreground elements
        drawAliens(canvas);
    }

    private void drawAliens(Canvas canvas) {
        assert bullets != null;

        for (Bullet bullet : bullets) {
            bullet.draw(canvas);
        }

        for (Enemy enemy : enemies) {
            enemy.draw(canvas);
        }
    }

    public <T> void setStart(T things,String type) {
        if (type.equals("bullet")){
            bullets = (List<Bullet>) things;
        } else if (type.equals("enemy")){
            enemies = (List<Enemy>) things;
        }
    }
}

