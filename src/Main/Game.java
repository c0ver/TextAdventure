package Main;

import Events.Event;
import Plot.Plot;
import Quests.Quest;
import Things.Entities.Entity;
import Things.Entities.Mobile;
import Things.Item;

import Things.Place;
import Things.Plottable;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;


public class Game extends Application {

    private static final Font titleFont = new Font("Arial", 90);
    private static final Font storyFont = new Font("Arial", 20);
    private static final Font infoFont = new Font("Arial", 30);

    /* increase space between map and mainText */
    private static final int MAIN_TEXT_PADDING = 50;

    private static Stage window;
    public static int windowWidth;
    public static int windowHeight;

    public static boolean debug = true;

    /* Main.Game stuff */
    public static Mobile me;

    /* map stuff */
    private static final String MAIN_MAP_NAME = "mainMap";
    private static final int MAP_ID = 1000;
    private static final int MAIN_MAP_SIZE = 32;
    private static final int SQUARE_SIZE = 64;
    public static final int SQUARES_SHOWN = 5; // needs to be odd
    public static Place mainMap;

    private static final String PLAYER_ICON_FILE = "assets/playerIcon.jpeg";
    public static Image playerIcon;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // get the size of the main monitor
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        windowWidth = (int) screenBounds.getWidth();
        windowHeight = (int) screenBounds.getHeight();

        // shallow copy
        window = primaryStage;
        window.setFullScreen(true);
        window.show();

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
            displayEvent(Quest.getQuest(0).doQuest());
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
        Plottable.createPlottables();
        Item.createItems();

        Plot.inputImages();
        try {
            playerIcon = new Image(new FileInputStream(PLAYER_ICON_FILE));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Quest.parseQuest();
    }

    /*
     * Displays the given event onto the game screen
     */
    public static void displayEvent(Event event) {

        // update event in case it wasn't completely made when initially created
        event.validate();

        if(debug) {
            System.err.println(event.getTitle());
        }

        /* The story (center) */
        Text mainText = new Text(event.getText());
        mainText.setFont(storyFont);
        mainText.setWrappingWidth(windowWidth -
               (SQUARES_SHOWN * SQUARE_SIZE * 2) - MAIN_TEXT_PADDING);
        mainText.setTextAlignment(TextAlignment.JUSTIFY);
        StackPane centerText = new StackPane(mainText);
        centerText.setMinWidth(windowWidth - SQUARES_SHOWN * SQUARE_SIZE * 2);
        StackPane.setMargin(mainText, new Insets(20));

        ScrollPane textWindow = new ScrollPane(centerText);
        textWindow.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);


        /* Player information/stats (left) */
        VBox playerInfoWindow = new VBox();
        Text playerInfo = new Text(me.getInfo());
        playerInfo.setFont(infoFont);
        playerInfoWindow.getChildren().add(playerInfo);
        playerInfoWindow.setBackground(Background.EMPTY);
        playerInfoWindow.setMinWidth(SQUARES_SHOWN * SQUARE_SIZE);


        /* Environment information/stats (right) */
        VBox otherInfoWindow = new VBox();
        otherInfoWindow.setBackground(Background.EMPTY);
        otherInfoWindow.setMinWidth(SQUARES_SHOWN * SQUARE_SIZE);

        StackPane playerPlot = new StackPane();
        playerPlot.setMinWidth(SQUARES_SHOWN * SQUARE_SIZE);
        playerPlot.setMinHeight(SQUARES_SHOWN * SQUARE_SIZE);

        Image plot = Plot.getPlotImage(me.getLocation());

        /* format the plot according to player position */
        /* PixelReader allows cropping of image */
        PixelReader reader = plot.getPixelReader();

        int leftBorder = 0;
        int topBorder = 0;
        int botBorder = 0;
        int rightBorder = 0;

        /* choose starting position */
        int x_Offset = SQUARE_SIZE * (me.getX() - SQUARES_SHOWN / 2);
        int y_Offset = SQUARE_SIZE * (me.getY() - SQUARES_SHOWN / 2);
        int heightToShow = 0;
        int widthToShow = 0;
        if(x_Offset < 0) {
            leftBorder = x_Offset * -1;
            widthToShow = x_Offset;
            x_Offset = 0;
        }
        if(y_Offset < 0) {
            topBorder = y_Offset * -1;
            heightToShow = y_Offset;
            y_Offset = 0;
        }

        /* choose how many squares to show */
        int pixelsToShow = SQUARE_SIZE * SQUARES_SHOWN;
        heightToShow += pixelsToShow;
        widthToShow += pixelsToShow;

        if(x_Offset + widthToShow > plot.getWidth()) {
            widthToShow = (int) plot.getWidth() - x_Offset;
            rightBorder = pixelsToShow - widthToShow - leftBorder;
        }
        if(y_Offset + heightToShow > plot.getHeight()) {
            heightToShow = (int) plot.getHeight() - y_Offset;
            botBorder = pixelsToShow - heightToShow - topBorder;
        }

        WritableImage newPlot = new WritableImage(reader, x_Offset, y_Offset,
                widthToShow, heightToShow);

        ImageView plotView = new ImageView(newPlot);
        ImageView playerIconView = new ImageView(playerIcon);
        playerPlot.getChildren().addAll(plotView, playerIconView);

        StackPane.setMargin(plotView, new Insets(topBorder, rightBorder,
                botBorder, leftBorder));

        /* addPlottable location information */
        String locationText = String.format("(%d, %d)", me.getX(), me.getY());
        Text location = new Text(locationText);
        location.setFont(infoFont);

        /* Add the nodes onto the infoWindow */
        otherInfoWindow.getChildren().add(playerPlot);
        otherInfoWindow.getChildren().add(location);

        /* addPlottable other information if needed */
        if(event.getOther() != null) {
            Text otherInfo = new Text(event.getOther().getInfo());
            otherInfo.setFont(infoFont);
            otherInfoWindow.getChildren().add(otherInfo);
        }


        /* buttons (bottom) */
        FlowPane buttonPane = new FlowPane();
        buttonPane.getChildren().addAll(event.getButtonSet());

        BorderPane layout = new BorderPane();
        layout.setLeft(playerInfoWindow);
        layout.setCenter(textWindow);
        layout.setRight(otherInfoWindow);
        layout.setBottom(buttonPane);

        Scene gameScene = new Scene(layout, windowWidth, windowHeight);
        gameScene.setFill(Color.BLACK);

        window.setScene(gameScene);
    }

}
