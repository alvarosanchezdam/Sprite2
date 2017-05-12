package cat.flx.sprite;

import android.graphics.Rect;

class Bonk extends Character {
    private int dir;
    private boolean jumping, willJump;

    private int[][] states = {
            { 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 13, 13, 14, 14, 14, 13, 13 }, // 0: standing by
            { 6, 7, 8, 9 },  // 1: walking left
            { 0, 1, 2, 3 },
            { 51 }, // 2: walking right
    };
    int[][] getStates() { return states; }

    public void setStates(int[][] states) {
        this.states = states;
    }

    Bonk(Game game) {
        super(game);
        x = 0; y = 0;
        padLeft = 2;
        padTop = 0;
        colWidth = 20;
        colHeight = 32;
    }

    void left() { dir = -10; }
    void right() { dir = 10; }
    void stop() { dir = 0; }

    void jump() { if (!jumping) willJump = true; }

    @Override
    void physics() {
        vx = dir;dir=0;
        if (willJump) { vy = -11; jumping = false; willJump = false; }

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
        // detect ceiling
        if (vy < 0) {
            int c1 = (newx + padLeft) / 16;
            int c2 = (newx + padLeft + colWidth) / 16;
            int row = (newy + padTop) / 16;
            for (int col = c1; col <= c2; col++) {
                if (scene.isWall(row, col)) {
                    newy = (row + 1) * 16 - padTop;
                    vy = 0;
                    jumping = true;
                    break;
                }
            }
        }

        // apply resulting physics
        x = newx;
        y = newy;

        // screen limits
        Rect colR = getCollisionRect();
        x = Math.max(x, -padLeft);
        x = Math.min(x, game.getScene().getWidth() - colR.width());
        y = Math.min(y, game.getScene().getHeight() - colR.height());

        // state change


        if (vx == 0) state = 0;
        else if (vx < 0) state = 1;
        else state = 2;


    }
}
