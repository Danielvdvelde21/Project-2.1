package MainMenu;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MainMenuScreen extends Application {

    private GameMenu gameMenu;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Pane root = new Pane();
        root.setPrefSize(1000,700);

        InputStream is = Files.newInputStream(Paths.get("risk1.jpg"));    //load image
        Image img = new Image(is);
        is.close();

        ImageView iv = new ImageView(img);
        iv.setFitWidth(1000);
        iv.setFitHeight(700);

        gameMenu = new GameMenu();

        root.getChildren().addAll(iv, gameMenu);

        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private class GameMenu extends Parent {
        public GameMenu() {
            VBox menu1 = new VBox(10);  //main menu
            VBox menu2 = new VBox(10);  //submenu

            menu1.setTranslateX(100);
            menu1.setTranslateY(200);
            menu2.setTranslateX(100);
            menu2.setTranslateY(200);

            MenuButton playBtn = new MenuButton("Play");
            playBtn.setOnMouseClicked(event -> {});

            MenuButton settingsBtn = new MenuButton("Settings");
            settingsBtn.setOnMouseClicked(event -> {});

            MenuButton helpBtn = new MenuButton("Help");
            helpBtn.setOnMouseClicked(event -> {});

            menu1.getChildren().addAll(playBtn, settingsBtn, helpBtn);

            Rectangle r = new Rectangle(1000, 700); //background for the menu
            r.setFill(Color.GREY);
            r.setOpacity(0.4);

            getChildren().addAll(r, menu1);
        }
    }

    private static class MenuButton extends StackPane {
        private Text text;

        public MenuButton(String name) {
            text = new Text(name);
            text.setFont(text.getFont().font(20));
            text.setFill(Color.WHITE);

            Rectangle r = new Rectangle(250, 30);   //background rectangle
            r.setOpacity(0.6);
            r.setFill(Color.BLACK);
            r.setEffect(new GaussianBlur(3.5));

            setAlignment(Pos.CENTER_LEFT);
            setRotate(-0.5);
            getChildren().addAll(r, text);  //add text over background

            setOnMouseEntered(event -> {    //effect when entering the button area
                r.setTranslateX(10);    //move element in X direction
                text.setTranslateX(10);
                r.setFill(Color.WHITE);
                text.setFill(Color.BLACK);
            });

            setOnMouseExited(event -> { //reverting the effect when exiting the area
                r.setTranslateX(0);
                text.setTranslateX(0);
                r.setFill(Color.BLACK);
                text.setFill(Color.WHITE);
            });

            DropShadow ds = new DropShadow(50, Color.WHITE);
            ds.setInput(new Glow());

            setOnMousePressed(event -> setEffect(ds));  //effects when pressing and releasing
            setOnMouseReleased(event -> setEffect(null));
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
