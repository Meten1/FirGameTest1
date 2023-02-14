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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isRedrawing = true;

        Resources resources = this.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        screen_width = dm.widthPixels;

        bullets = new ArrayList<>();
        addBullet(5, 300,50);
        addBullet(30, 50,100);
        Handler mainHandler = new Handler();
        outerspaceView = findViewById(R.id.outerspaceView);
        outerspaceView.setBullet(bullets);

        redraw = () -> {
            if (isRedrawing) {
                allMove();
                outerspaceView.invalidate();
                mainHandler.postDelayed(redraw, 24);
            }
        };
        mainHandler.post(redraw);




    }

    private void addBullet(int speed,int x, int y){
        bullets.add(new Bullet(screen_width,speed,x,y));
    }

    private void allMove() {
        for (Bullet bullet : bullets) {
            bullet.move();
            PointF position = bullet.getPosition();
            if (position.x + 50 >= screen_width ){
                bullets.remove(bullet);
            }
        }
    }




}