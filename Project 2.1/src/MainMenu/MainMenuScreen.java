package MainMenu;

import BackEndStructure.MainGameLoop;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MainMenuScreen extends Application {

    private GameMenu gameMenu;
    private String[] players;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Pane root = new Pane();
        root.setPrefSize(1000,700);
        InputStream is = Files.newInputStream(Paths.get("src\\resources\\risk1.jpg"));    //load image
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
            VBox menu1 = new VBox(13);  //main menu
            VBox menu2 = new VBox(13);  //submenu1 Single or Multi Player
            VBox menu3 = new VBox(13);  //submenu2 num of Players
            VBox menu4 = new VBox(13);  //submenu3 names of Players

            menu1.setTranslateX(350);
            menu1.setTranslateY(250);
            menu2.setTranslateX(350);
            menu2.setTranslateY(250);
            menu3.setTranslateX(350);
            menu3.setTranslateY(250);
            menu4.setTranslateX(350);
            menu4.setTranslateY(250);

            // Menu 1
            MenuButton playBtn = new MenuButton("Play");
            playBtn.setOnMouseClicked(event -> {    //transition to menu2
                getChildren().add(menu2);
                getChildren().remove(menu1);
            });

            MenuButton settingsBtn = new MenuButton("Settings");
            settingsBtn.setOnMouseClicked(event -> {});

            MenuButton helpBtn = new MenuButton("Help");
            helpBtn.setOnMouseClicked(event -> {});

            MenuButton exitBtn = new MenuButton("Exit");
            exitBtn.setOnMouseClicked(event -> {System.exit(0);});

            // Menu 2
            MenuButton singlePlayerBtn = new MenuButton("Single Player");
            singlePlayerBtn.setOnMouseClicked(event -> {
                getChildren().add(menu3);   //transition menu3
                getChildren().remove(menu2);
            });

            MenuButton multiPlayerBtn = new MenuButton("Multi-Player");
            multiPlayerBtn.setOnMouseClicked(event -> {});

            MenuButton backBtn = new MenuButton("Back");
            backBtn.setOnMouseClicked(event -> {    //transition to menu1
                getChildren().add(menu1);
                getChildren().remove(menu2);
            });

            // Menu 4
            TextInput name1 = new TextInput("Your name:");
            TextInput name2 = new TextInput("P2's name:");
            TextInput name3 = new TextInput("P3's name:");
            TextInput name4 = new TextInput("P4's name:");
            TextInput name5 = new TextInput("P5's name:");
            TextInput name6 = new TextInput("P6's name:");

            MenuButton startBtn = new MenuButton("Start");
            startBtn.setOnMouseClicked(event -> {
                // TODO add restrictions for the names entered
                MainGameLoop mainGameLoop = new MainGameLoop(players.length, players);
            });

            MenuButton back2Btn = new MenuButton("Back");
            back2Btn.setOnMouseClicked(event -> {    //transition to menu3
                getChildren().add(menu3);
                getChildren().remove(menu4);
            });

            // Menu 3
            MenuButton singlePlayerOptionBtn = new MenuButton("Single Player:");
            singlePlayerOptionBtn.comboBoxStyle();
            singlePlayerOptionBtn.setOnMouseClicked(event -> {  //transition to menu2
                getChildren().add(menu2);
                getChildren().remove(menu3);
            });

            MenuButton players2Btn = new MenuButton("2 Players");
            players2Btn.setOnMouseClicked(event -> {
                menu4.getChildren().clear();
                menu4.getChildren().addAll(name1, name2, startBtn, back2Btn);
                getChildren().add(menu4);
                getChildren().remove(menu3);
                players = new String[]{name1.getString(), name2.getString()};
            });
            MenuButton players3Btn = new MenuButton("3 Players");
            players3Btn.setOnMouseClicked(event -> {
                menu4.getChildren().clear();
                menu4.getChildren().addAll(name1, name2, name3, startBtn, back2Btn);
                getChildren().add(menu4);
                getChildren().remove(menu3);
                players = new String[]{name1.getString(), name2.getString(), name3.getString()};
            });
            MenuButton players4Btn = new MenuButton("4 Players");
            players4Btn.setOnMouseClicked(event -> {
                menu4.getChildren().clear();
                menu4.getChildren().addAll(name1, name2, name3, name4, startBtn, back2Btn);
                getChildren().add(menu4);
                getChildren().remove(menu3);
                players = new String[]{name1.getString(), name2.getString(), name3.getString(), name4.getString()};
            });
            MenuButton players5Btn  = new MenuButton("5 Players");
            players5Btn.setOnMouseClicked(event -> {
                menu4.getChildren().clear();
                menu4.getChildren().addAll(name1, name2, name3, name4, name5, startBtn, back2Btn);
                getChildren().add(menu4);
                getChildren().remove(menu3);
                players = new String[]{name1.getString(), name2.getString(), name3.getString(), name4.getString(), name5.getString()};
            });
            MenuButton players6Btn = new MenuButton("6 Players");
            players6Btn.setOnMouseClicked(event -> {
                menu4.getChildren().clear();
                menu4.getChildren().addAll(name1, name2, name3, name4, name5, name6, startBtn, back2Btn);
                getChildren().add(menu4);
                getChildren().remove(menu3);
                players = new String[]{name1.getString(), name2.getString(), name3.getString(), name4.getString(), name5.getString(), name6.getString()};
            });

            menu1.getChildren().addAll(playBtn, settingsBtn, helpBtn, exitBtn);
            menu2.getChildren().addAll(singlePlayerBtn, multiPlayerBtn, backBtn);
            menu3.getChildren().addAll(singlePlayerOptionBtn, players2Btn, players3Btn, players4Btn, players5Btn, players6Btn);

            Rectangle r = new Rectangle(1000, 700); //background for the menu
            r.setFill(Color.GREY);
            r.setOpacity(0.25);

            getChildren().addAll(r, menu1);
        }
    }

    private static class MenuButton extends StackPane {

        private Text text;
        private Rectangle r;

        private void comboBoxStyle() {    //makes button appear pressed
            text.setFont(text.getFont().font(23));
            text.setFill(Color.BLACK);
            r.setFill(Color.WHITE);
            r.setTranslateX(-10);    //move element in X direction
            text.setTranslateX(-10);

            setOnMouseEntered(event -> {    //effect when entering the button area
                r.setTranslateX(0);
                text.setTranslateX(0);
            });

            setOnMouseExited(event -> { //reverting the effect when exiting the area
                r.setTranslateX(-10);
                text.setTranslateX(-10);
                r.setFill(Color.WHITE);
                text.setFill(Color.BLACK);
            });
        }

        public MenuButton(String name) {
            text = new Text(name);
            text.setFont(text.getFont().font(23));
            text.setFill(Color.WHITE);

            r = new Rectangle(300, 35);   //background rectangle
            r.setOpacity(0.65);
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

    private static class TextInput extends StackPane {

        private TextField tf;
        private Rectangle r;
        private Label label1;

        public String getString() {
            return this.tf.getText();
        }

        public TextInput(String name) {
            tf = new TextField();
            tf.setBackground(Background.EMPTY);
            tf.setPrefColumnCount(10);
            tf.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");

            label1 = new Label(name);
            label1.setFont(Font.font(18));
            label1.setTextFill(Color.GREY);
            label1.setTranslateY(5);    //fixing position

            HBox hb = new HBox();
            hb.getChildren().addAll(label1, tf);
            hb.setSpacing(10);

            r = new Rectangle(300, 35);   //background rectangle
            r.setOpacity(0.65);
            r.setFill(Color.BLACK);
            r.setEffect(new GaussianBlur(3.5));

            setAlignment(Pos.CENTER_LEFT);
            setRotate(-0.5);
            getChildren().addAll(r,  hb);  //add hBox (label and textField) over background

        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
