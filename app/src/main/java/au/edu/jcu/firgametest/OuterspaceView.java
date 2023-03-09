package au.edu.jcu.firgametest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;

import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class OuterspaceView extends View {
    private PointF[] starField;
    private CopyOnWriteArrayList<Bullet> bullets;
    private CopyOnWriteArrayList<Enemy> enemies;
    private CopyOnWriteArrayList<UpgradeBall> upgradeBalls;
    private CopyOnWriteArrayList<FighterPlane> fighterPlanes;

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
//        assert bullets != null;

        for (Bullet bullet : bullets) {
            bullet.draw(canvas);
        }

        for (Enemy enemy : enemies) {
            enemy.draw(canvas);
        }

        for (UpgradeBall upgradeBall : upgradeBalls){
            upgradeBall.draw(canvas);
        }

        for (FighterPlane fighterplane:fighterPlanes){
            fighterplane.draw(canvas);
        }

    }

    public <T> void setStart(T things,String type) {
        if (type.equals("bullet")){
            bullets = (CopyOnWriteArrayList<Bullet>) things;
        } else if (type.equals("enemy")){
            enemies = (CopyOnWriteArrayList<Enemy>) things;
        } else if (type.equals("upgradeBall")){
            upgradeBalls = (CopyOnWriteArrayList<UpgradeBall>) things;
        } else if (type.equals("fighterPlanes")) {
            fighterPlanes = (CopyOnWriteArrayList<FighterPlane>) things;
        }
    }
}

