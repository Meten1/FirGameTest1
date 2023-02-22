package au.edu.jcu.firgametest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

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
        bullets.add(new Bullet(screen_width,5, 300,50,50));
        bullets.add(new Bullet(screen_width,30, 50,100,30));
        enemies.add(new Enemy(screen_width,30,50,100));
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
        Thread BulletMove = new Thread(new Runnable() {
            @Override
            public void run() {
                if (bullets.size() <= 0){
                    return;
                }
                CyclicBarrier cyclicBarrier = new CyclicBarrier(1);
                for (Bullet bullet:bullets){
                    bullet.move();
                }
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException | BrokenBarrierException e){
                    e.printStackTrace();
                }
            }
        });
        Thread EnemyMove = new Thread(new Runnable() {
            @Override
            public void run() {
                CyclicBarrier cyclicBarrier = new CyclicBarrier(1);
                for (Enemy enemy:enemies){
                    enemy.move();
                }
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException | BrokenBarrierException e){
                    e.printStackTrace();
                }
            }
        });

        BulletMove.start();
        EnemyMove.start();
        checkPosition();
    }

    private void checkPosition(){
        for (Bullet bullet:bullets){
            float bulletX = bullet.getPosition().x;
            float bulletY = bullet.getPosition().y;
            float bulletBubble = bullet.getBUBBLE_SIZE();
            if (bulletX + bulletBubble >= screen_width){
                bullets.remove(bullet);
                continue;
            }
            for (Enemy enemy:enemies){
                float enemyX = enemy.getPosition().x;
                float enemyY = enemy.getPosition().y;
                float enemyBubble = enemy.getBUBBLE_SIZE();
                if (((bulletY + bulletBubble >= enemyY - enemyBubble)
                        || (bulletY - bulletBubble <= enemyY + enemyBubble))
                        && (bulletX + bulletBubble >= enemyX - enemyBubble)){
                    enemy.hit(bullet.getHarm());
                    bullets.remove(bullet);
                    if (enemy.getBlood()<=0){
                        enemies.remove(enemy);
                    }
                    break;
                }
            }
        }

    }

}



