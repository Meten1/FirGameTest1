package au.edu.jcu.firgametest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CyclicBarrier;

public class MainActivity extends AppCompatActivity {



    private int screen_width;
    private int screen_height;
    private OuterspaceView outerspaceView;
    private boolean isRedrawing;
    private Runnable redraw;
    private CopyOnWriteArrayList<Bullet> bullets;
    private CopyOnWriteArrayList<Enemy> enemies;
    private CopyOnWriteArrayList<UpgradeBall> upgradeBalls;
    private CopyOnWriteArrayList<FighterPlane> fighterPlanes;
    int lastX = 0;
    int lastY = 0;



    private int speed;

    private Random rd;

    private int EnemyNum = 0; //该阶段敌军数量，用于推动游戏难度升级

    private int EnemyGenNum = 1; //每次生成的敌军的数量上限
    private int diffcultyNum = 1; //难度系数，随敌军生成量提高

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isRedrawing = true;

        Resources resources = this.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();

        screen_width = dm.widthPixels;
        screen_height = dm.heightPixels - 200;

        speed = 100;
        rd = new Random();

        bullets = new CopyOnWriteArrayList<>();
        enemies = new CopyOnWriteArrayList<>();
        upgradeBalls = new CopyOnWriteArrayList<>();
        fighterPlanes = new CopyOnWriteArrayList<>();


        fighterPlanes.add(new FighterPlane(screen_width,80,600,200,35));
        Handler mainHandler = new Handler();

        outerspaceView = findViewById(R.id.outerspaceView);
        outerspaceView.setStart(bullets,"bullet");
        outerspaceView.setStart(enemies,"enemy");
        outerspaceView.setStart(upgradeBalls,"upgradeBall");
        outerspaceView.setStart(fighterPlanes,"fighterPlanes");


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
        for (Bullet bullet:bullets){
            bulletMove(bullet);
        }

        for (Enemy enemy:enemies){
            enemytMove(enemy);
        }

        for (UpgradeBall upgradeBall : upgradeBalls){
            upgradeBalltMove(upgradeBall);
        }

        allFire();
        generateRun();
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
                                    && (bulletY - bulletBubble <= enemyY + enemyBubble))
                                    && (bulletX + bulletBubble >= enemyX - enemyBubble)){
                                enemy.hit(bullet.getHarm());
                                bullets.remove(bullet);
                                timer.cancel();
                                break;
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
                        float enemyY= enemy.getPosition().y;
                        int enemyBubble = enemy.getBUBBLE_SIZE();
                        if (enemy.getBlood() <= 0 || enemyX - enemyBubble <= 0){
                            enemies.remove(enemy);
                            timer.cancel();
                        }
                        for (FighterPlane fighterPlane:fighterPlanes){
                            float x = fighterPlane.getPosition().x;
                            float y = fighterPlane.getPosition().y;
                            int bubble = fighterPlane.getBUBBLE_SIZE();
                            if ((enemyX + enemyBubble > x - bubble && enemyX - enemyBubble < x + bubble)
                                    && (enemyY + enemyBubble > y - bubble && enemyY - enemyBubble < y + bubble)){
                                fighterPlanes.remove(fighterPlane);
                                enemies.remove(enemy);
                                timer.cancel();
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

    //创建子线程使升级球移动，并触发升级球的检测碰撞反弹机制，升级球与战舰的碰撞机制也在这里
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
                        for (FighterPlane fighterPlane:fighterPlanes){
                            float planeX = fighterPlane.getPosition().x;
                            float planeY = fighterPlane.getPosition().y;
                            int planeBubble = fighterPlane.getBUBBLE_SIZE();
                            float x = upgradeBall.getPosition().x;
                            float y = upgradeBall.getPosition().y;
                            int bubble = upgradeBall.getBUBBLE_SIZE();
                            if ((planeX + planeBubble > x - bubble && planeX - planeBubble < x + bubble)
                                    && (planeY + planeBubble > y - bubble && planeY - planeBubble < y + bubble)){
                                String updateType = upgradeBall.getType();
                                if (updateType.equals("AddNew")){
                                    addNewPlane();
                                } else {
                                    fighterPlane.update(updateType);
                                }
                                upgradeBalls.remove(upgradeBall);
                                timer.cancel();
                                break;
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

    private void generateRun(){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                CyclicBarrier cyclicBarrier = new CyclicBarrier(1);
                generateEnemy();
                int num = rd.nextInt(10);
                if (num >= 3){
                    generateUpdateBall();
                }
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException | BrokenBarrierException e){
                    e.printStackTrace();
                }
            }
        },0L,3000L);
    }

    private void generateEnemy() {
        if (EnemyNum >= 5){/////////////////////////////////////////////////////////////////////////////////////
        diffcultyNum += 1;
        EnemyNum = 0;
        }
        int num = rd.nextInt((int) (diffcultyNum))+1;
        Timer timer = new Timer();
        final int[] count = {0};

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (count[0] < num){
                    int y = rd.nextInt(screen_height);
                    Enemy enemy = new Enemy(screen_width, (int) (30*(1+(diffcultyNum / 10))),y,100);
                    enemytMove(enemy);
                    enemies.add(enemy);
                    count[0] += 1;
                    EnemyNum +=1;
                } else {
                    timer.cancel();
                }

            }
        },0L,50L);
    }

    private void generateUpdateBall(){
        int num = rd.nextInt(3);
        int y = rd.nextInt(screen_height);
        UpgradeBall upgradeBall = new UpgradeBall(screen_width,screen_height,100,100);
        upgradeBalltMove(upgradeBall);
        upgradeBalls.add(upgradeBall);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY() - 200;
        //限制触摸区域，Y轴暂时失效，待改良
//        for (Fighterplane fighterPlane:fighterPlanes){
//            PointF position = fighterPlane.getPosition();
//            int bubble = fighterPlane.getBUBBLE_SIZE();
//            if ((x > position.x + bubble || x < position.x - bubble)
//                    || (y > position.y + bubble || y < position.y - bubble)){
//                System.out.println("-----------------------");
//                return false;
//            }
//        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = x;
                lastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                // 计算偏移量
                int offsetX = x - lastX;
                int offsetY = y - lastY;
                lastX = x;
                lastY = y;
                for (FighterPlane fighterPlane:fighterPlanes){
                    fighterPlane.move(offsetX,offsetY);
                }
                break;
        }
        return true;
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

    //全部开火，调用后遍历全部战机，并为其调用开火方法
    private void allFire(){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                CyclicBarrier cyclicBarrier = new CyclicBarrier(1);
                for (FighterPlane fighterPlane:fighterPlanes){
                    fire(fighterPlane);
                }
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException | BrokenBarrierException e){
                    e.printStackTrace();
                }
            }
        },0L,1200L);
    }

    //开火，调用后以0.2秒间隔根据战机子弹数目发射1-3枚子弹
    private void fire(FighterPlane fighterPlane){
        Timer timer = new Timer();
        int bulletsNum = fighterPlane.getBulletsNum();
        final int[] count = {0};
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (count[0] < bulletsNum) {
                    float x = fighterPlane.getPosition().x;
                    float y = fighterPlane.getPosition().y;
                    int harm = fighterPlane.getHarm();
                    Bullet bullet = new Bullet(speed, x, y, harm);
                    bulletMove(bullet);
                    bullets.add(bullet);
                    count[0] += 1;
                } else {
                    timer.cancel();
                }
            }
        },0L,200L);
    }

    //添加新的战机，大小为50，伤害为25，添加至战机后方
    private void addNewPlane(){
        if (fighterPlanes.size()<5){
            FighterPlane oldPlane = fighterPlanes.get(fighterPlanes.size() -1);
            float x = oldPlane.getPosition().x ;
            float y = oldPlane.getPosition().y + oldPlane.getBUBBLE_SIZE() + 50 + 10;
            FighterPlane newPlane = new FighterPlane(screen_width,50,x,y,25);
            fighterPlanes.add(newPlane);
        }
    }







}




