package au.edu.jcu.firgametest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class MainActivity extends AppCompatActivity {
    private int screen_width;
    private int screen_height;
    private OuterspaceView outerspaceView;
    private boolean isRedrawing;
    private Runnable redraw;
    private List<Bullet> bullets;
    private List<Enemy> enemies;
    private List<UpgradeBall> upgradeBalls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isRedrawing = true;

        Resources resources = this.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();

        screen_width = dm.widthPixels;
        screen_height = dm.heightPixels - 200;

        bullets = new ArrayList<>();
        enemies = new ArrayList<>();
        upgradeBalls = new ArrayList<>();
        bullets.add(new Bullet(screen_width,25, 0,50,50));
        bullets.add(new Bullet(screen_width,30, 0,100,30));
        enemies.add(new Enemy(screen_width,30,200,100));
        upgradeBalls.add(new UpgradeBall(screen_width,screen_height,100,100,-1,1));
        Handler mainHandler = new Handler();

        outerspaceView = findViewById(R.id.outerspaceView);
        outerspaceView.setStart(bullets,"bullet");
        outerspaceView.setStart(enemies,"enemy");
        outerspaceView.setStart(upgradeBalls,"upgradeBall");


        redraw = () -> {
            if (isRedrawing) {
                outerspaceView.invalidate();
                mainHandler.postDelayed(redraw, 10);
            }
        };
        mainHandler.post(redraw);

        allMove();
    }



    private void allMove() {
//        Thread BulletMove = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                if (bullets.size() <= 0){
//                    return;
//                }
//                CyclicBarrier cyclicBarrier = new CyclicBarrier(1);
//                for (Bullet bullet:bullets){
//                    bullet.move();
//                }
//                try {
//                    cyclicBarrier.await();
//                } catch (InterruptedException | BrokenBarrierException e){
//                    e.printStackTrace();
//                }
//            }
//        });
//        Thread EnemyMove = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                CyclicBarrier cyclicBarrier = new CyclicBarrier(1);
//                for (Enemy enemy:enemies){
//                    enemy.move();
//                }
//                try {
//                    cyclicBarrier.await();
//                } catch (InterruptedException | BrokenBarrierException e){
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        BulletMove.start();
//        EnemyMove.start();
//        checkPosition();

        for (Bullet bullet:bullets){
            bulletMove(bullet);
        }
        for (Enemy enemy:enemies){
            enemytMove(enemy);
        }

        for (UpgradeBall upgradeBall : upgradeBalls){
            upgradeBalltMove(upgradeBall);
        }

    }

    public void test(View view){
        bullets.remove(0);
        System.out.println("执行删除----------");

    }

    //创建子线程线程使子弹移动，该线程只检测子弹是否碰撞敌机和和边缘，敌机扣血将在这里
    private void bulletMove(Bullet bullet){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                CyclicBarrier cyclicBarrier = new CyclicBarrier(1);
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        bullet.move();
                        float bulletX= bullet.getPosition().x;
                        float bulletY= bullet.getPosition().y;
                        int bulletBubble = bullet.getBUBBLE_SIZE();
                        if (bulletX + bulletBubble >= screen_width){
                            bullets.remove(bullet);
                            timer.cancel();
                            return;
                        }
                        for (Enemy enemy:enemies){
                            float enemyX = enemy.getPosition().x;
                            float enemyY = enemy.getPosition().y;
                            int enemyBubble = enemy.getBUBBLE_SIZE();
                            if (((bulletY + bulletBubble >= enemyY - enemyBubble)
                                    || (bulletY - bulletBubble <= enemyY + enemyBubble))
                                    && (bulletX + bulletBubble >= enemyX - enemyBubble)){
                                enemy.hit(bullet.getHarm());
                                bullets.remove(bullet);
                                timer.cancel();
                                return;
                            }
                        }
                    }
                },0L,10L);
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException | BrokenBarrierException e){
                    e.printStackTrace();
                }

            }
        });
        thread.start();
    }

    //创建子线程线程使敌机移动，该线程只检测敌机是否碰撞边缘以及检测血量，敌机消失机制在这里
    private void enemytMove(Enemy enemy){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                CyclicBarrier cyclicBarrier = new CyclicBarrier(1);
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        enemy.move();
                        float enemyX= enemy.getPosition().x;
                        int enemyBubble = enemy.getBUBBLE_SIZE();
                        if (enemy.getBlood() <= 0 || enemyX - enemyBubble <= 0){
                            enemies.remove(enemy);
                            timer.cancel();
                        }
                    }
                },0L,10L);
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException | BrokenBarrierException e){
                    e.printStackTrace();
                }


            }
        });
        thread.start();
    }

    private void upgradeBalltMove(UpgradeBall upgradeBall){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                CyclicBarrier cyclicBarrier = new CyclicBarrier(1);
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        upgradeBall.move();
                        upgradeBall.checkCollide();
                        }

                },0L,10L);
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException | BrokenBarrierException e){
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }


    //暂时无用，保存以供备用
    private void checkPosition(){
        for (Bullet bullet:bullets){
            float bulletX = bullet.getPosition().x;
            float bulletY = bullet.getPosition().y;
            float bulletBubble = bullet.getBUBBLE_SIZE();
            for (Enemy enemy:enemies){
                float enemyX = enemy.getPosition().x;
                float enemyY = enemy.getPosition().y;
                float enemyBubble = enemy.getBUBBLE_SIZE();
                if (((bulletY + bulletBubble >= enemyY - enemyBubble)
                        || (bulletY - bulletBubble <= enemyY + enemyBubble))
                        && (bulletX + bulletBubble >= enemyX - enemyBubble)){
                    enemy.hit(bullet.getHarm());
                    bullets.remove(bullet);
                    break;
                }
            }
        }

    }





}



