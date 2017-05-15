package cat.flx.sprite;

class Box extends Character {
    private Game g;
    private static int[][] states = {
            {38}
    };
    int[][] getStates() { return states; }

    Box(Game game) {
        super(game);
        g = game;
        padLeft = padTop = 0;
        colWidth = colHeight = 12;
        frame = (int)(Math.random() * 5);
    }

    void physics() {
        this.y+=10;
        if(y>250){
            int random = (int)(Math.random() * 500);
            int random2 = (int)(Math.random() * 20+20);
            x=random;
            y=-random2;
            g.setPuntuacion(1);
        }
    }
}
