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

import Things.Something;

public class Plot {

	// number of layers the plot has
	private static final int PLOT_LAYER_COUNT = 2;

	private static final String TILE_TEXT_FILE = "assets/tileText.json";
	private static final String PLOT_FILE = "assets/plot/%s_%s.csv";

	private static final String[] LAYER_NAMES = {"Terrain", "Structures"};

	private static final String PLOT_FILE_ERROR =
            "ERROR: %s was not found or could not be read properly\n";
	private static final String PLOT_SIZE_ERROR =
            "ERROR: Plot does not match given size\n";
	private static final String TILE_NAME_ERROR =
            "ERROR: %s from %s not found in tileList\n";

	private Tile[][] plot;

	private String name;
	private int size;
	
	// list of total possible tiles
	private static List<Tile> tileList;

	public Plot(String name, int size) {
        this.name = name;
        this.size = size;
        createPlot();
    }

    /*
     *  Has to parse through each map layer
     *  Go through the primary layer first, then others added on
     */
	private void createPlot() {
        plot = new Tile[size][size];
		Scanner scanner;
		for(int i = 0; i < PLOT_LAYER_COUNT; i++) {

            try {
                String file = String.format(PLOT_FILE, name, LAYER_NAMES[i]);
                scanner = new Scanner(new File(file));
            } catch (FileNotFoundException e) {
                System.err.println(e.getMessage());
                return;
            }

            /* input tiles into game based on x, y coordinates */
            int y = 0;
            while (scanner.hasNextLine()) {
                String[] temp = scanner.nextLine().split(",");

                if (temp.length != size) {
                    System.err.println(PLOT_SIZE_ERROR);
                }

                for (int x = 0; x < size; x++) {

                    // no tile in csv file == -1
                    // 113 is a placeholder for a city/town
                    if (temp[x].equals("-1") || temp[x].equals("113")) {
                        continue;
                    }

                    int index = tileList.indexOf(new Tile(temp[x]));

                    if (index == -1) {
                        System.err.printf(TILE_NAME_ERROR, temp[x], name);
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

	public int getSize() {
	    return size;
    }

    public String getName() {
	    return name;
    }
	
	public Tile getTile(int x, int y) {
		return plot[y][x];
	}
	
	public void add(Something thing) {
		plot[thing.getY()][thing.getX()].addThing(thing);
	}

	/*
	 * Creates a separate tile for every unique description
	 */
	public static void parseTileText() {

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
}
