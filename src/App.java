import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class App extends Application implements PlayerII {
    ArrayList<String> input = new ArrayList<>();
    List<Enemy> enemies = new ArrayList<>();
    List<Box> boxes = new ArrayList<>();
    List<Wall> walls = new ArrayList<>();
    List<Pipe> pipes = new ArrayList<>();
    List<Coin> coins = new ArrayList<>();
    Random rand = new Random();
    int counter = 0;
    Player player = new Player(this);
    private Text coinCountText = new Text("Coins: " + counter);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        try {
            Group root = new Group();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Mario Game");

            Canvas canvas = new Canvas(800, 550);
            root.getChildren().add(canvas);
            coinCountText.setX(10);
            coinCountText.setY(20);
            root.getChildren().add(coinCountText);

            scene.setOnKeyPressed(e -> {
                String code = e.getCode().toString();

                // only add once... prevent duplicates
                if (!input.contains(code)) {
                    input.add(code);
                }
            });

            scene.setOnKeyReleased(e -> {
                String code = e.getCode().toString();
                input.remove(code);

                // add Enemy
                if (code.equals("E")) {
                    Enemy enemy = new Enemy();
                    enemies.add(enemy);
                }

                // add Box || collision box - wall, box, coin
                if (code.equals("B")) {
                    Box box = new Box(rand.nextInt(800), 250);
                    for (int i = 0; i < boxes.size() || i < walls.size() || i < coins.size(); i++) {
                        if (i < boxes.size()) {
                            if (box.collision(boxes.get(i))) {
                                i = -1;
                                box.setX(rand.nextInt(800));
                            }
                        }
                        if (i >= 0 && i < walls.size()) {
                            if (box.collision(walls.get(i))) {
                                i = -1;
                                box.setX(rand.nextInt(800));
                            }
                        }
                        if (i >= 0 && i < coins.size()) {
                            if (box.collision(coins.get(i))) {
                                i = -1;
                                box.setX(rand.nextInt(800));
                            }
                        }
                    }
                    boxes.add(box);
                }

                // add Wall (brick) || collision wall - box, wall, coin
                if (code.equals("W")) {
                    Wall wall = new Wall(rand.nextInt(800), 250);
                    for (int i = 0; i < walls.size() || i < boxes.size() || i < coins.size(); i++) {
                        if (i < walls.size()) {
                            if (wall.collision(walls.get(i))) {
                                i = -1;
                                wall.setX(rand.nextInt(800));
                            }
                        }
                        if (i >= 0 && i < boxes.size()) {
                            if (wall.collision(boxes.get(i))) {
                                i = -1;
                                wall.setX(rand.nextInt(800));
                            }
                        }
                        if (i >= 0 && i < coins.size()) {
                            if (wall.collision(coins.get(i))) {
                                i = -1;
                                wall.setX(rand.nextInt(800));
                            }
                        }
                    }
                    walls.add(wall);
                }

                // add small or big Pipe || collision pipe - enemy, pipe,
                // coin (pipe - coin not working well)
                if (code.equals("O") || code.equals("P")) {
                    Image image;
                    if (code.equals("O"))
                        image = new Image("file:images/pipe/pipeSmall.png");
                    else
                        image = new Image("file:images/pipe/pipeBig.png");
                    Pipe pipe = new Pipe(rand.nextInt(760), image);
                    for (int i = 0; i < enemies.size() || i < pipes.size() || i < coins.size(); i++) {
                        if (i < enemies.size()) {
                            if (pipe.collision(enemies.get(i))) {
                                i = -1;
                                pipe.setX(rand.nextInt(760));
                            }
                        }
                        if (i >= 0 && i < pipes.size()) {
                            if (pipe.collision(pipes.get(i))) {
                                i = -1;
                                pipe.setX(rand.nextInt(760));
                            }
                        }
                        if (i >= 0 && i < coins.size()) {
                            if (pipe.collision(coins.get(i))) {
                                i = -1;
                                pipe.setX(rand.nextInt(760));
                            }
                        }
                    }
                    pipes.add(pipe);
                }

                // add Coin || collision coin - box, wall, coin, pipe
                if (code.equals("C")) {
                    Coin coin = new Coin(rand.nextInt(800), rand.nextInt(550));
                    for (int i = 0; i < boxes.size() || i < walls.size() || i < coins.size()
                            || i < pipes.size(); i++) {
                        if (i < boxes.size()) {
                            if (coin.collision(boxes.get(i))) {
                                i = -1;
                                coin.setX(rand.nextInt(800));
                            }
                        }
                        if (i >= 0 && i < walls.size()) {
                            if (coin.collision(walls.get(i))) {
                                i = -1;
                                coin.setX(rand.nextInt(800));
                            }
                        }
                        if (i >= 0 && i < coins.size()) {
                            if (coin.collision(coins.get(i))) {
                                i = -1;
                                coin.setX(rand.nextInt(800));
                            }
                        }
                        if (i >= 0 && i < pipes.size()) {
                            if (coin.collision(pipes.get(i))) {
                                i = -1;
                                coin.setX(rand.nextInt(800));
                            }
                        }
                        if (coin.getY() > 550 - 85 - coin.getImage().getHeight()) {
                            i = -1;
                            coin.setY(rand.nextInt(800));
                        }

                    }
                    coins.add(coin);
                }

                // Clear area
                if (code.equals("Z")) {
                    boxes.clear();
                    enemies.clear();
                    walls.clear();
                    pipes.clear();
                    coins.clear();
                    player.setFloor((int) (550 - 85 - player.getImage().getHeight()));
                    player.checkJumping();
                }

            });

            GraphicsContext gc = canvas.getGraphicsContext2D();

            Image background = new Image("file:images/background.png");

            new AnimationTimer() {
                public void handle(long currentNanoTime) {

                    // moving screen
                    if (player.getX() >= 500) {
                        player.setX(500 - player.getMoveLength());
                        coins.forEach(coin -> coin.setX(coin.getX() - player.getMoveLength()));
                        boxes.forEach(box -> {
                            box.setX(box.getX() - player.getMoveLength());
                            if (box.getBoxCoin() != null) {
                                box.getBoxCoin().setX(box.getBoxCoin().getX() - player.getMoveLength());
                            }
                            if (box.getPowerUp() != null) {
                                box.getPowerUp().setX(box.getPowerUp().getX() - player.getMoveLength());
                            }
                        });
                        walls.forEach(wall -> wall.setX(wall.getX() - player.getMoveLength()));
                        pipes.forEach(pipe -> pipe.setX(pipe.getX() - player.getMoveLength()));
                        enemies.forEach(enemy -> enemy.setX(enemy.getX() - player.getMoveLength()));
                    }
                    player.checkJumping();
                    // when game is over game stopped and player is doing one
                    // jump, player don't stack in any object
                    if (player.getGameOver()) {

                    } else {
                        player.move(input);
                        // move enemy and check collisions player - enemy, enemy
                        for (int i = 0; i < enemies.size(); i++) {
                            Enemy enemy = enemies.get(i);
                            enemy.move();
                            player.collision(enemy);
                            for (int j = i + 1; j < enemies.size(); j++) {
                                enemy.collision(enemies.get(j));
                            }
                            // collision enemy - pipe
                            pipes.forEach(enemy::collision);
                        }
                        // collision player - pipe
                        pipes.forEach(player::collision);
                        // collision player - box, animate box
                        boxes.forEach(box -> {
                            box.animate();
                            player.collision(box);
                            // animate BoxCoin
                            if (box.getBoxCoin() != null) {
                                box.getBoxCoin().animate();
                            }
                            // collision player - PowerUp and move PowerUp
                            if (box.getPowerUp() != null) {
                                PowerUp powerUp = box.getPowerUp();
                                // falling PowerUp if not collision with Box
                                if (!powerUp.collision(box) && !powerUp.getFalling() && box.getY() > powerUp.getY()) {
                                    powerUp.setFalling(true);
                                }
                                box.getPowerUp().move();
                                pipes.forEach(pipe -> box.getPowerUp().collision(pipe));
                                player.collision(box.getPowerUp());
                            }
                        });
                        // collision player - wall
                        walls.forEach(player::collision);
                        // collision player - coin, animate coin
                        coins.forEach(coin -> {
                            player.collision(coin);
                            coin.animate();
                        });
                        // if x < 0 it's like toDelete
                        // removing walls
                        for (int i = 0; i < walls.size(); i++)
                            if (walls.get(i).getX() < 0)
                                walls.remove(i);
                        // removing coins
                        for (int i = 0; i < coins.size(); i++)
                            if (coins.get(i).getX() < 0)
                                coins.remove(i);
                        // removing enemies
                        for (int i = 0; i < enemies.size(); i++)
                            if (enemies.get(i).getX() < 0)
                                enemies.remove(i);
                        // removing BoxCoins
                        for (Box boxe : boxes) {
                            if (boxe.getBoxCoin() != null && boxe.getBoxCoin().getX() < 0) {
                                boxe.setBoxCoin(null);
                            }
                            if (boxe.getPowerUp() != null && boxe.getPowerUp().getX() < 0) {
                                boxe.setPowerUp(null);
                            }
                        }
                    }

                    gc.drawImage(background, 0, 0);
                    coins.forEach(coin -> gc.drawImage(coin.getImage(), coin.getX(), coin.getY()));
                    pipes.forEach(pipe -> gc.drawImage(pipe.getImage(), pipe.getX(), pipe.getY()));
                    walls.forEach(wall -> gc.drawImage(wall.getImage(), wall.getX(), wall.getY()));
                    boxes.forEach(box -> {
                        gc.drawImage(box.getImage(), box.getX(), box.getY());
                        if (box.getBoxCoin() != null) {
                            gc.drawImage(box.getBoxCoin().getImage(), box.getBoxCoin().getX(), box.getBoxCoin().getY());
                        }
                        if (box.getPowerUp() != null) {
                            gc.drawImage(box.getPowerUp().getImage(), box.getPowerUp().getX(), box.getPowerUp().getY());
                        }
                    });

                    enemies.forEach(enemy -> gc.drawImage(enemy.getImage(), enemy.getX(), enemy.getY()));
                    gc.drawImage(player.getImage(), player.getX(), player.getY());
                }

            }.start();

            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void collisionWithCoin() {
        counter++;
        System.out.println("Coins: " + counter);
        coinCountText.setText("Coins: " + counter);
    }

    private void restartGame() {
        player = new Player(this);
    }

    @Override
    public void gameOverRestartGame() {
        // TODO Auto-generated method stub
        counter = 0;
        System.out.println("Coins: " + counter);
        coinCountText.setText("Coins: " + counter);
        restartGame();
    }

}