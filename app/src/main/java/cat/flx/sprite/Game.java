package cat.flx.sprite;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.List;

class Game {
    private Context context;
    private boolean muerto;
    private BitmapSet bitmapSet;
    private Scene scene;
    private Bonk bonk;
    private Audio audio;
    private boolean die = false;
    private Box box;
    private Box box2;
    private Box box3;
    private Box box4;
    private List<Coin> coins;
    private List<Enemy> enemyList;
    private List<Box> boxes;
    private int screenOffsetX, screenOffsetY;
    private boolean derecha=false;
    Game(Activity activity) {
        this.context = activity;
        bitmapSet = new BitmapSet(context.getResources());
        audio = new Audio(activity);
        scene = new Scene(this);
        bonk = new Bonk(this);
        coins = new ArrayList<>();
        enemyList= new ArrayList<>();
        scene.loadFromFile(R.raw.mini);
        bonk.x = 16 * 10;
        bonk.y = 0;
        muerto = false;
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
        box4.y=+130;
        boxes = new ArrayList<>();
    }

    public boolean isMuerto() {
        return muerto;
    }

    public void setMuerto(boolean muerto) {
        this.muerto = muerto;
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
        box.physics();
        box2.physics();
        box3.physics();
        box4.physics();
        for(Coin coin : coins) {
            coin.physics();
        }
        for(Enemy enemy:enemyList){
            enemy.physics();
            if (!die&&enemy.getCollisionRect().intersect(bonk.getCollisionRect())) {
                audio.die();
                die = true;
                enemy.stop();
                enemy.setStates(new int[][]{
                        {20}, // 0: standing by
                        {20},  // 1: walking left
                        {20},  // 2: walking right
                });
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
