package cat.flx.sprite;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

class Game {
    private Context context;
    private BitmapSet bitmapSet;
    private GameView gameView;
    private Scene scene;
    private Bonk bonk;
    private Audio audio;
    private boolean die = false;
    private MainActivity act;
    private Box box;
    private Box box2;
    private Box box3;
    private int puntuacion;
    private Box box4;
    private Box box5;
    private Box box6;
    private Box box7;
    private List<Coin> coins;
    private List<Enemy> enemyList;
    private List<Box> boxes;
    private int screenOffsetX, screenOffsetY;
    private boolean derecha=false;
    Game(Activity activity, GameView gameView) {
        this.context = activity;
        this.gameView = gameView;
        act = (MainActivity) activity;
        bitmapSet = new BitmapSet(context.getResources());
        audio = new Audio(activity);
        scene = new Scene(this);
        bonk = new Bonk(this);
        coins = new ArrayList<>();
        enemyList= new ArrayList<>();
        scene.loadFromFile(R.raw.mini);
        bonk.x = 16 * 10;
        bonk.y = 0;
        box=new Box(this);
        box.x=10 * 10;
        box.y=0;
        box2=new Box(this);
        box2.x=19 * 10;
        box2.y=-100;
        box3=new Box(this);
        box3.x=6 * 10;
        box3.y=-150;
        box4=new Box(this);
        box4.x=1 * 10;
        box4.y=+120;
        box5=new Box(this);
        box5.x=1 * 10;
        box5.y=+180;
        box6=new Box(this);
        box6.x=1 * 10;
        box6.y=+200;
        box7=new Box(this);
        box7.x=1 * 10;
        box7.y=+210;
        boxes = new ArrayList<>();
        boxes.add(box);boxes.add(box2);boxes.add(box3);boxes.add(box4);boxes.add(box5);boxes.add(box6);boxes.add(box7);
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(int puntuacion) {
        this.puntuacion = this.puntuacion+puntuacion;
        act.puntuacion.setText(this.puntuacion+"");
    }

    public boolean isDie() {
        return die;
    }

    public void setDie(boolean die) {
        this.die = die;
    }

    Context getContext() { return context; }
    Resources getResources() { return context.getResources(); }

    BitmapSet getBitmapSet() { return bitmapSet; }
    Scene getScene() { return scene; }
    Audio getAudio() { return audio; }
    Bonk getBonk() { return bonk; }

    void addCoin(Coin coin) {
        coins.add(coin);
    }
    void addEnemy(Enemy enemy) {
        enemyList.add(enemy);
    }
    void addBox(Box box) {
        boxes.add(box);
        box=box;
    }
    List<Coin> getCoins() { return coins; }

    void physics() {
        bonk.physics();
        for(Box b : boxes){
            b.physics();
            if (!die&&b.getCollisionRect().intersect(bonk.getCollisionRect())) {
                audio.die();
                die = true;
                act.reiniciar.setVisibility(View.VISIBLE);
                for(Box box : boxes){
                    box.stop();
                }
                for(Enemy enemy:enemyList) {
                    enemy.stop();
                }
            }
        }
        for(Coin coin : coins) {
            coin.physics();
        }
        for(Enemy enemy:enemyList){
            enemy.physics();
            if (!die&&enemy.getCollisionRect().intersect(bonk.getCollisionRect())) {
                audio.die();
                die = true;
                enemy.stop();
                act.reiniciar.setVisibility(View.VISIBLE);
                enemy.setStates(new int[][]{
                        {20}, // 0: standing by
                        {20},  // 1: walking left
                        {20},  // 2: walking right
                });
                for(Box box : boxes){
                    box.stop();
                }
            }
        }
    }

    private float sc;
    private int scX, scY;

    void draw(Canvas canvas) {
        if (canvas.getWidth() == 0) return;
        if (sc == 0) {
            scY = 16 * 16;
            sc = canvas.getHeight() / (float) scY;
            scX = (int) (canvas.getWidth() / sc);
        }
        screenOffsetX = Math.min(screenOffsetX, bonk.x - 100);
        screenOffsetX = Math.max(screenOffsetX, bonk.x - scX + 100);
        screenOffsetX = Math.max(screenOffsetX, 0);
        screenOffsetX = Math.min(screenOffsetX, scene.getWidth() - scX - 1);
        screenOffsetY = Math.min(screenOffsetY, bonk.y - 50);
        screenOffsetY = Math.max(screenOffsetY, bonk.y - scY + 75);
        screenOffsetY = Math.max(screenOffsetY, 0);
        screenOffsetY = Math.min(screenOffsetY, scene.getHeight() - scY);
        canvas.scale(sc, sc);
        canvas.translate(-screenOffsetX, -screenOffsetY);
        scene.draw(canvas);
        bonk.draw(canvas);
        box.draw(canvas);
        box2.draw(canvas);
        box3.draw(canvas);
        box4.draw(canvas);

        for(Coin coin : coins) {
            coin.draw(canvas);
        }
        for(Enemy enemy : enemyList) {
            enemy.draw(canvas);
        }
//        if (Math.random() > 0.95f) audio.coin();
//        if (Math.random() > 0.95f) audio.die();
//        if (Math.random() > 0.95f) audio.pause();
    }

    private int keyCounter = 0;
    private boolean keyLeft, keyRight, keyJump;
    void keyLeft(boolean down) { keyCounter = 0; if (down) keyLeft = true; }
    void keyRight(boolean down) { keyCounter = 0; if (down) keyRight = true; }
    void keyJump(boolean down) { keyCounter = 0; if (down) keyJump = true; }

    private boolean left, right, jump;
    void left(boolean down) {
        if (left && !down) left = false;
        else if (!left && down) left = true;
    }
    void right(boolean down) {
        if (right && !down) right = false;
        else if (!right && down) right = true;
    }
    void jump(boolean down) {
        if (jump && !down) jump = false;
        else if (!jump && down) jump = true;
    }

    void events() {
        if (++keyCounter > 2) {
            keyCounter = 0;
            keyLeft = keyRight = keyJump = false;
        }
        if (keyLeft || left) { bonk.left(); }
        if (keyRight || right) { bonk.right(); }
        if (keyJump || jump) { bonk.jump(); }
    }
}
