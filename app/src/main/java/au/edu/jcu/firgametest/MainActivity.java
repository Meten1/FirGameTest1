package au.edu.jcu.firgametest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private int screen_width;
    private OuterspaceView outerspaceView;
    private boolean isRedrawing;
    private Runnable redraw;
    private List<Bullet> bullets;
    private List<Enemy> enemies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isRedrawing = true;

        Resources resources = this.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        screen_width = dm.widthPixels;

        bullets = new ArrayList<>();
        enemies = new ArrayList<>();
        bullets.add(new Bullet(screen_width,5, 300,50));
        bullets.add(new Bullet(screen_width,30, 50,100));
        enemies.add(new Enemy(screen_width,30,50));
        Handler mainHandler = new Handler();
        outerspaceView = findViewById(R.id.outerspaceView);
        outerspaceView.setStart(bullets,"bullet");
        outerspaceView.setStart(enemies,"enemy");

        redraw = () -> {
            if (isRedrawing) {
                allMove();
                outerspaceView.invalidate();
                mainHandler.postDelayed(redraw, 24);
            }
        };
        mainHandler.post(redraw);

    }



    private void allMove() {
        for (Enemy enemy : enemies) {
            enemy.move();
            PointF position = enemy.getPosition();
            if (position.x + enemy.getBUBBLE_SIZE()/2 <= 0 ){
                bullets.remove(enemy);
            }
        }
    }

    private boolean checkPosition(PointF position1,int bubble1, PointF position2, int bubble2){
        if ((((position1.y + bubble1/2 >= position2.y - bubble2/2 || position1.y - bubble1/2 <= position2.y + bubble2/2))&&((position1.x + bubble1/2 >= position2.x - bubble2/2 || position1.x - bubble1/2 <= position2.x + bubble2/2)))){
            return true;
        }
        return false;
    }
    }



    