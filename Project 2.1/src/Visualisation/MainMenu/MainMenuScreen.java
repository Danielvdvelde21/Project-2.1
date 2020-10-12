package Visualisation.MainMenu;

import BackEndStructure.Game.MainGameLoop;
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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class MainMenuScreen extends Application {

    private String[] playerNames;
    private Stage ps;

    @Override
    public void start(Stage primaryStage) throws Exception {
        ps = primaryStage;
        Pane root = new Pane();
        root.setPrefSize(1000,700);
        InputStream is = Files.newInputStream(Paths.get("src\\resources\\risk1.jpg"));    //load image
        Image img = new Image(is);
        is.close();

        ImageView iv = new ImageView(img);
        iv.setFitWidth(1000);
        iv.setFitHeight(700);

        GameMenu gameMenu = new GameMenu();

        root.getChildren().addAll(iv, gameMenu);

        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private class GameMenu extends Parent {

        public boolean emptyString(String[] array) {
            for ( String s : array ) {
                if (s.length() == 0) {
                    return true;
                }
            }
            return false;
        }

        public boolean duplicateString(String[] array) {
            Set<String> set = new HashSet<>();
            for(String s : array) {
                if(!set.add(s)) {
                    return true;
                }
            }
            return false;
        }

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
            exitBtn.setOnMouseClicked(event -> System.exit(0));

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
                //getting players' names from TextInput objects
                int count = menu4.getChildren().size();
                String[] pm = new String[]{name1.tf.getText(), name2.tf.getText(), name3.tf.getText(), name4.tf.getText(), name5.tf.getText(), name6.tf.getText()};
                playerNames = Arrays.copyOf(pm, count-2);

                if(emptyString(playerNames)) {  //name input restrictions
                    startBtn.addWarning("[ENTER NAMES]");
                }
                else if(duplicateString(playerNames)) {
                    startBtn.addWarning("[DUPLICATE NAMES]");
                }
                else {
                    //System.out.println("Names entered: " + Arrays.toString(playerNames));
                    ps.hide();  //hide menu
                    MainGameLoop mainGameLoop = new MainGameLoop(playerNames.length, playerNames);
                }
            });

            MenuButton back2Btn = new MenuButton("Back");
            back2Btn.setOnMouseClicked(event -> {    //transition to menu3
                startBtn.removeWarning();
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
            });
            MenuButton players3Btn = new MenuButton("3 Players");
            players3Btn.setOnMouseClicked(event -> {
                menu4.getChildren().clear();
                menu4.getChildren().addAll(name1, name2, name3, startBtn, back2Btn);
                getChildren().add(menu4);
                getChildren().remove(menu3);
            });
            MenuButton players4Btn = new MenuButton("4 Players");
            players4Btn.setOnMouseClicked(event -> {
                menu4.getChildren().clear();
                menu4.getChildren().addAll(name1, name2, name3, name4, startBtn, back2Btn);
                getChildren().add(menu4);
                getChildren().remove(menu3);
            });
            MenuButton players5Btn  = new MenuButton("5 Players");
            players5Btn.setOnMouseClicked(event -> {
                menu4.getChildren().clear();
                menu4.getChildren().addAll(name1, name2, name3, name4, name5, startBtn, back2Btn);
                getChildren().add(menu4);
                getChildren().remove(menu3);
            });
            MenuButton players6Btn = new MenuButton("6 Players");
            players6Btn.setOnMouseClicked(event -> {
                menu4.getChildren().clear();
                menu4.getChildren().addAll(name1, name2, name3, name4, name5, name6, startBtn, back2Btn);
                getChildren().add(menu4);
                getChildren().remove(menu3);
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
        private Text w = new Text();

        private void comboBoxStyle() {    //makes button appear pressed
            r.setTranslateX(10);    //move element in X direction
            text.setTranslateX(10);
            r.setFill(Color.WHITE);
            text.setFill(Color.BLACK);

            setOnMouseEntered(event -> {    //effect when entering the button area
                r.setTranslateX(10);    //move element in X direction
                text.setTranslateX(10);
                r.setFill(Color.WHITE);
                text.setFill(Color.BLACK);
            });

            setOnMouseExited(event -> { //reverting the effect when exiting the area
                r.setTranslateX(0);
                text.setTranslateX(0);
                r.setFill(Color.WHITE);
                text.setFill(Color.BLACK);
            });
        }

        public void addWarning(String warning) {
            w.setText(warning);
            w.setFont(Font.font(15));
            w.setFill(Color.RED);
            w.setTranslateX(100);
            if(!getChildren().contains(w)) {
                getChildren().add(w);
            }
        }

        public void removeWarning() {
            getChildren().remove(w);
        }

        public MenuButton(String name) {
            text = new Text(name);
            text.setFont(Font.font(23));
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

        public TextInput(String name) {
            tf = new TextField();
            tf.setBackground(Background.EMPTY);
            tf.setPrefColumnCount(10);
            tf.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");

            Label label1 = new Label(name);
            label1.setFont(Font.font(18));
            label1.setTextFill(Color.GREY);
            label1.setTranslateY(5);    //fixing position

            HBox hb = new HBox();
            hb.getChildren().addAll(label1, tf);
            hb.setSpacing(10);

            Rectangle r = new Rectangle(300, 35);   //background rectangle
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