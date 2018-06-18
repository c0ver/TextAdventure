package Main;

import Events.Event;
import Plot.Plot;
import Things.Entities.Entity;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;
import javafx.stage.Stage;

import static Main.Game.me;

public class DisplayEvent {

    private static final Font storyFont = new Font("Arial", 20);
    private static final Font infoFont = new Font("Arial", 30);

    // screen size
    public static Stage window;
    public static int windowWidth;
    public static int windowHeight;
    private static int infoWindowWidth;
    private static int mainWindowWidth;

    // map stuff
    private static final int SQUARE_SIZE = 64;
    private static final int SQUARES_SHOWN = 5; // needs to be odd

    /* increase space between map and mainText */
    private static final int MAIN_TEXT_PADDING = 50;


    /* The story (center) */
    private static Text mainText;

    /* Player information/stats (left) */
    private static Text playerInfo;
    private static Text[] moneyCount;

    /* Environment information/stats (right) */
    private static VBox otherInfoWindow;
    private static ImageView plotView;
    // Location information
    private static Text location;
    // other information
    private static Text otherInfo;

    /* buttons (bottom) */
    private static FlowPane buttonPane;


    public static void setStage(Stage primaryStage) {
         // get the size of the main monitor
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        windowWidth = (int) screenBounds.getWidth();
        windowHeight = (int) screenBounds.getHeight();

        infoWindowWidth = SQUARE_SIZE * SQUARES_SHOWN;
        mainWindowWidth = windowWidth - infoWindowWidth * 2;

        window = primaryStage;
        window.setFullScreen(true);
        window.show();
    }

    public static void initialize() {

        /* The story (center) */
        mainText = new Text();
        mainText.setFont(storyFont);
        mainText.setWrappingWidth(windowWidth -
                (SQUARES_SHOWN * SQUARE_SIZE * 2) - MAIN_TEXT_PADDING);
        mainText.setTextAlignment(TextAlignment.JUSTIFY);

        StackPane centerText = new StackPane(mainText);
        StackPane.setMargin(mainText, new Insets(20));
        centerText.setMinWidth(windowWidth - SQUARES_SHOWN * SQUARE_SIZE * 2);

        ScrollPane textWindow = new ScrollPane(centerText);
        textWindow.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);


        /* Player information/stats (left) */
        playerInfo = new Text();
        playerInfo.setFont(infoFont);

        GridPane moneyInfo = new GridPane();
        moneyInfo.setHgap(15);
        moneyInfo.setAlignment(Pos.CENTER);
        moneyCount = new Text[Entity.MONEY_TYPES];
        for(int x = 0; x < Entity.MONEY_TYPES; x++) {
            moneyCount[x] = new Text();
            moneyCount[x].setFont(infoFont);
            moneyInfo.add(moneyCount[x], x, 0);
        }
        moneyCount[Entity.COPPER_INDEX].setFill(Color.PERU);
        moneyCount[Entity.SILVER_INDEX].setFill(Color.SILVER);
        moneyCount[Entity.GOLD_INDEX].setFill(Color.GOLDENROD);

        VBox playerInfoWindow = new VBox(playerInfo, moneyInfo);
        playerInfoWindow.setBackground(Background.EMPTY);
        playerInfoWindow.setMinWidth(SQUARES_SHOWN * SQUARE_SIZE);


        /* Environment information/stats (right) */
        ImageView playerIconView = new ImageView(Game.playerIcon);
        plotView = new ImageView();
        StackPane playerPlot = new StackPane(plotView, playerIconView);
        playerPlot.setMinWidth(SQUARES_SHOWN * SQUARE_SIZE);
        playerPlot.setMinHeight(SQUARES_SHOWN * SQUARE_SIZE);

        location = new Text();
        location.setFont(infoFont);

        otherInfo = new Text();
        otherInfo.setFont(infoFont);

        /* Add the nodes onto the infoWindow */
        otherInfoWindow = new VBox(playerPlot, location);
        otherInfoWindow.setBackground(Background.EMPTY);
        otherInfoWindow.setMinWidth(SQUARES_SHOWN * SQUARE_SIZE);


        /* buttons (bottom) */
        buttonPane = new FlowPane();


        /* MAIN LAYOUT */
        BorderPane layout = new BorderPane();
        layout.setLeft(playerInfoWindow);
        layout.setCenter(textWindow);
        layout.setRight(otherInfoWindow);
        layout.setBottom(buttonPane);

        Scene gameScene = new Scene(layout, windowWidth, windowHeight);
        window.setScene(gameScene);
    }

    public static void show(Event event) {

        // update event in case it wasn't completely made when initially created
        event.validate();

        if(Game.debug) System.err.println(event.getTitle());

        /* The story (center) */
        mainText.setText(event.getText());


        /* Player information/stats (left) */
        playerInfo.setText(me.getInfo());
        for(int x = 0; x < Entity.MONEY_TYPES; x++) {
            moneyCount[x].setText("" + me.getMoney()[x]);
        }


        /* Environment information/stats (right) */
        Image plot = Plot.getPlotImage(me.getLocation());

        // format the plot according to player position
        PixelReader reader = plot.getPixelReader();

        int leftBorder, topBorder, botBorder, rightBorder;
        leftBorder = topBorder = botBorder = rightBorder = 0;

        /* choose starting position */
        int x_Offset = SQUARE_SIZE * (me.getX() - SQUARES_SHOWN / 2);
        int y_Offset = SQUARE_SIZE * (me.getY() - SQUARES_SHOWN / 2);
        int heightToShow = 0, widthToShow = 0;
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
        plotView.setImage(newPlot);
        StackPane.setMargin(plotView, new Insets(topBorder, rightBorder,
                botBorder, leftBorder));

        /* addPlottable location information */
        String locationText = String.format("(%d, %d)", me.getX(), me.getY());
        location.setText(locationText);

        /* addPlottable other information if needed */
        if(event.getOther() != null) {
            otherInfo.setText(event.getOther().getInfo());
            if(!otherInfoWindow.getChildren().contains(otherInfo)) {
                otherInfoWindow.getChildren().add(otherInfo);
            }
        } else {
            otherInfoWindow.getChildren().remove(otherInfo);
        }


        /* buttons (bottom) */
        buttonPane.getChildren().clear();
        buttonPane.getChildren().addAll(event.getButtonSet());
    }
}
