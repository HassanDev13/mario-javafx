

import java.util.ArrayList;

import javafx.scene.image.Image;

public class Player {

    private int x;
    private int y;
    private int floor; // minimum y he can reach
    private boolean jumping; // true if he is jumping
    private boolean falling; // true when he end jump and he is falling down
    private int moveLength; // how long does take one step
    private int jumpHeight;
    private Image[] imageRight = new Image[5]; // images have same size
    private Image[] imageLeft = new Image[5];
    private int i, j; // how long does take one animation
    private boolean gameOver;
    private int counter;
    private int lvl;

    public Player() {
        super();
        this.lvl = 0;
        for (int k = 0; k < 5; k++) {
            imageRight[k] = new Image(("file:images/player/marioRight" + k + "Lvl" + lvl + ".png"));
            imageLeft[k] = new Image(("file:images/player/marioLeft" + k + "Lvl" + lvl + ".png"));
        }
        this.x = 100;
        this.floor = (int) (550 - 85 - imageRight[0].getHeight()); // 85 is height of the ground
        this.y = floor;
        this.i = 0;
        this.j = 0;
        this.moveLength = 5;
        this.jumping = false;
        this.falling = false;
        this.jumpHeight = 5;
        this.gameOver = false;
        this.counter = 0;
    }

    int getY() {
        return y;
    }

    int getX() {
        return x;
    }

    void setX(int x) {
        this.x = x;
    }

    int getMoveLength() {
        return moveLength;
    }

    Image getImage() {
        if (i < 10)
            return imageRight[0];
        else if (i < 20)
            return imageRight[1];
        else if (i < 30)
            return imageRight[2];
        else if (i < 40)
            return imageRight[3];
        else if (i < 50)
            return imageRight[4];
        else if (j < 10)
            return imageLeft[0];
        else if (j < 20)
            return imageLeft[1];
        else if (j < 30)
            return imageLeft[2];
        else if (j < 40)
            return imageLeft[3];
        else
            return imageLeft[4];
    }

    boolean getFalling() {
        return falling;
    }

    boolean getGameOver() {
        return gameOver;
    }

    void setFloor(int floor) {
        this.floor = floor;
    }

    void move(ArrayList<String> input) {
        if (input.contains("LEFT") && x > 0) {
            x -= moveLength;
            i = 50;
            j++;
            if (j >= 50)
                j = 10;
        }
        if (input.contains("RIGHT") && x < 800 - imageRight[0].getWidth()) {
            x += moveLength;
            j = 50;
            i++;
            if (i >= 50)
                i = 10;
        }
        if (input.contains("UP") && !jumping && !falling) {
            jumping = true;
        }
        if (input.isEmpty()) {
            if (j == 50) // last move was in right side
                i = 0;
            else // last move was in left side
                j = 0;
        }
        if (input.contains("DOWN")) {
            counter = 0;
            floor = (int) (550 - 85 - imageRight[0].getHeight());
            y = floor;
        }
    }

    void checkJumping() {
        for (int i = 0; i < 2; i++) {
            if (jumping) {
                y -= jumpHeight;
                counter++;
                if (counter >= 62) {
                    jumping = false;
                    falling = true;
                }
            } else if (falling) {
                counter = 0;
                y += jumpHeight;
                if (y >= floor) {
                    falling = false;
                }
            }
        }
    }

    
}
