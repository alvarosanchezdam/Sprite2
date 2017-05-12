package cat.flx.sprite;

import android.graphics.Rect;

class Enemy extends Character {
    private int dir;
    private int move = 5;
    private boolean jumping, willJump;
    private int[][] states = {
            { 16, 17, 18, 19, 20 }
    };
    int[][] getStates() { return states; }

    public void setStates(int[][] states) {
        this.states = states;
    }

    Enemy(Game game) {
        super(game);
        padLeft = padTop = 0;
        colWidth = colHeight = 12;
        frame = (int)(Math.random() * 4);
    }
    void stop() { move = 0; }
    void physics() {
        vx = dir; dir = 0;
        if (willJump) { vy = -11; jumping = true; willJump = false; }

        if(x==360){
            move = -5;
        } else if(x==50){
            move = 5;
        }
        x=x+move;
        Scene scene = game.getScene();

        // detect wall to right
        int newx = x + vx;
        int newy = y + vy;
        if (vx > 0) {
            int col = (newx + padLeft + colWidth) / 16;
            int r1 = (newy + padTop) / 16;
            int r2 = (newy + padTop + colHeight - 1) / 16;
            for (int row = r1; row <= r2; row++) {
                if (scene.isWall(row, col)) {
                    newx = col * 16 - padLeft - colWidth;
                    break;
                }
            }
        }
        if (vx < 0) {
            int col = (newx + padLeft) / 16;
            int r1 = (newy + padTop) / 16;
            int r2 = (newy + padTop + colHeight - 1) / 16;
            for (int row = r1; row <= r2; row++) {
                if (scene.isWall(row, col)) {
                    newx = (col + 1) * 16 - padLeft;
                    break;
                }
            }
        }

        // detect ground
        // physics (try fall and detect ground)
        vy++; if (vy > 11) vy = 10;
        newy = y + vy;
        if (vy >= 0) {
            int c1 = (newx + padLeft) / 16;
            int c2 = (newx + padLeft + colWidth) / 16;
            int row = (newy + padTop + colHeight) / 16;
            for (int col = c1; col <= c2; col++) {
                if (scene.isGround(row, col)) {
                    newy = row * 16 - padTop - colHeight;
                    vy = 0;
                    jumping = false;
                    break;
                }
            }
        }

        x++;

        // apply resulting physics
        x = newx;
        y = newy;

    }
}
