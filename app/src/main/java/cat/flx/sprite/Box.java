package cat.flx.sprite;

class Box extends Character {

    private static int[][] states = {
            {38}
    };
    int[][] getStates() { return states; }

    Box(Game game) {
        super(game);
        padLeft = padTop = 0;
        colWidth = colHeight = 12;
        frame = (int)(Math.random() * 5);
    }

    void physics() {
        this.y+=10;
        if(y>250){
            int random = (int)(Math.random() * 400);
            x=random;
            y=-20;
        }
    }
}
