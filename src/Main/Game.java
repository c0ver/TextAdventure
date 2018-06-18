package Main;

import Plot.Plot;
import Quests.Quest;
import Things.Entities.Mobile;

import Things.Place;
import Things.Thing;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static Main.DisplayEvent.windowHeight;
import static Main.DisplayEvent.windowWidth;
import static Main.DisplayEvent.window;

public class Game extends Application {

    private static final Font titleFont = new Font("Arial", 90);

    public static boolean debug = true;

    // main map stuff
    private static final String MAIN_MAP_NAME = "mainMap";
    private static final int MAP_ID = 1000;
    private static final int MAIN_MAP_SIZE = 32;
    public static Place mainMap;

    public static final String PLAYER_ICON_FILE = "assets/playerIcon.jpeg";
    public static Image playerIcon;

    /* Main.Game stuff */
    public static Mobile me;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        DisplayEvent.setStage(primaryStage);
        createMainMenu();
        initializeGame();
    }

    private static void createCharacter() {
        TextField enterName = new TextField("Enter your name");
        enterName.setFont(titleFont);
        enterName.setBackground(Background.EMPTY);
        enterName.setAlignment(Pos.CENTER);

        Button nextButton = new Button("Next");
        nextButton.setOnAction(e -> {
            me = new Mobile(enterName.getText(), 0, mainMap);

            // intro scene
            DisplayEvent.initialize();
            DisplayEvent.show(Quest.getQuest(0).doQuest());
        });
        nextButton.setPrefWidth(windowWidth / 5);
        nextButton.setPrefHeight(windowHeight / 8);

        VBox layout = new VBox(windowHeight / 2, enterName, nextButton);
        layout.setAlignment(Pos.CENTER);
        layout.setBackground(Background.EMPTY);

        Scene nameScene = new Scene(layout, windowWidth, windowHeight);
        nameScene.setFill(Color.BLACK);

        window.setScene(nameScene);
    }

    /**
     * Creates the mainMenu and creates everything needed for the game
     */
    private static void createMainMenu() {
        System.err.println("createMainMenu");
        window.setTitle("GAME");

        // create the title
        Text gameTitle = new Text("Adventure");
        gameTitle.setFont(titleFont);
        gameTitle.setTextAlignment(TextAlignment.CENTER);
        gameTitle.setFill(Color.FORESTGREEN);

        // create the start button
        Button startButton = new Button("Begin");
        startButton.setOnAction(e -> {
            createCharacter();
        });
        startButton.setPrefWidth(windowWidth / 5);
        startButton.setPrefHeight(windowHeight / 8);

        // create the layout
        VBox layout = new VBox(windowHeight / 2, gameTitle, startButton);
        layout.setAlignment(Pos.CENTER);
        layout.setBackground(Background.EMPTY);

        // create the scene
        Scene titleScene = new Scene(layout, windowWidth, windowHeight);
        titleScene.setFill(Color.BLACK);

        window.setScene(titleScene);
    }

    private static void initializeGame() {
        Plot.parseTileText();

        // mainMap must be created first before villages can be added to it
        mainMap = new Place(MAIN_MAP_NAME, MAIN_MAP_SIZE, MAP_ID);
        Thing.createThings();

        // needs the list of places to be made before inputting images
        Plot.inputImages();
        try {
            playerIcon = new Image(new FileInputStream(PLAYER_ICON_FILE));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Quest.parseQuest();
    }
}

/*
 * Null pointer exception when not enough memory
 */