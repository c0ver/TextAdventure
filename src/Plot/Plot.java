package Plot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import main.Something;

public class Plot {
	public static final int PLOT_HEIGHT = 32;
	public static final int PLOT_WIDTH = 32;

	// number of layers the plot has
	private static final int PLOT_LAYER_COUNT = 2;

	private static final String TILE_TEXT_FILE = "assets/tileText.json";
	private static final String PLOT_FILE = "assets/plot_Tile Layer %d.csv";

	private static final String PLOT_FILE_ERROR =
            "ERROR: %s %d was not found or could not be read properly\n";
	private static final String PLOT_SIZE_ERROR =
            "ERROR: Plot does not match given HEIGHT AND WIDTH";
	private static final String TILE_NAME_ERROR =
            "ERROR: %s not found in tileList";

    // matrix of entire map of the game
	private static Tile[][] plot;
	
	// list of total possible tiles
	private static List<Tile> tileList;
	
	public static void createPlot() {
		plot = new Tile[PLOT_HEIGHT][PLOT_WIDTH];
		
		parseTileText();
		parsePlot();
	}

	/*
	 * Creates a separate tile for every unique description
	 */
	private static void parseTileText() {

        Gson gson = new Gson();
        FileReader tileGSON;
        try {
            tileGSON = new FileReader(TILE_TEXT_FILE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
        JsonObject tileJSON = gson.fromJson(tileGSON, JsonObject.class);

        tileList = new ArrayList<Tile>();
        for(Map.Entry<String, JsonElement> element : tileJSON.entrySet()) {
            Tile tile = gson.fromJson(element.getValue(), Tile.class);
            tile.setID(element.getKey());
            tileList.add(tile);
        }
	}

    /*
     *  Has to parse through each map layer
     *  Go through the primary layer first, then others added on
     */
	private static void parsePlot() {
		Scanner scanner;
		for(int i = 1; i <= PLOT_LAYER_COUNT; i++) {

            try {
                String file = String.format(PLOT_FILE, i);
                scanner = new Scanner(new File(file));
            } catch (FileNotFoundException e) {
                System.err.printf(PLOT_FILE_ERROR, PLOT_FILE, i);
                return;
            }

            /* input tiles into game based on x, y coordinates */
            int y = 0;
            while (scanner.hasNextLine()) {
                String[] temp = scanner.nextLine().split(",");

                if (temp.length != PLOT_WIDTH) {
                    System.err.println(PLOT_SIZE_ERROR);
                }

                for (int x = 0; x < PLOT_WIDTH; x++) {

                    // no tile in csv file == -1
                    if (temp[x].equals("-1")) {
                        continue;
                    }

                    int index = tileList.indexOf(new Tile(temp[x]));

                    if (index == -1) {
                        System.err.printf(TILE_NAME_ERROR, temp[x]);
                        return;
                    }

                    // first pass through
                    if (plot[y][x] == null) {
                        plot[y][x] = new Tile(tileList.get(index));
                    } else {
                        plot[y][x].addTile(tileList.get(index));
                    }
                }
                y++;
            }
            scanner.close();
        }
	}
	
	public static Tile getTile(int x, int y) {
		return plot[y][x];
	}
	
	public static void addSomething(Something thing) {
		plot[thing.getY()][thing.getX()].addThing(thing);
	}
}
