import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.util.ArrayList;

public class App extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Group root = new Group();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Mario Game");

        Canvas canvas = new Canvas(800, 550);
        root.getChildren().add(canvas);
        Player player = new Player();
        ArrayList<String> input = new ArrayList<>();
        GraphicsContext gc = canvas.getGraphicsContext2D();
        Image background = new Image("file:images/background.png");
        gc.drawImage(background, 0, 0);

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
        });

        new AnimationTimer() {
            public void handle(long currentNanoTime) {
                try {
                    player.checkJumping();

                    player.move(input);
                    gc.drawImage(background, 0, 0);

                    gc.drawImage(player.getImage(), player.getX(), player.getY());

                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }.start();

        stage.show();
    }
}