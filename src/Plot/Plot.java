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

	private static final String SQUARE_TEXT_FILE = "assets/squareText.json";
	private static final String PLOT_1_FILE = "assets/plot_Tile Layer 1.csv";
    private static final String PLOT_2_FILE = "assets/plot_Tile Layer 2.csv";

	private static final String PLOT_FILE_ERROR =
            "ERROR: %s was not found or could not be read properly\n";
	private static final String PLOT_SIZE_ERROR =
            "ERROR: Plot does not match given HEIGHT AND WIDTH";
	private static final String SQUARE_NAME_ERROR =
            "ERROR: %s not found in squareList";

    // matrix of plot that contains appropriate names from squareList
	private static Square[][] plot;
	
	// list of total possible squares
	private static List<Square> squareList;
	
	public static void createPlot() {
		plot = new Square[PLOT_HEIGHT][PLOT_WIDTH];
		
		parseSquareText();
		parsePlot();
	}
	
	private static void parseSquareText() {

        Gson gson = new Gson();
        FileReader squareGSON;
        try {
            squareGSON = new FileReader(SQUARE_TEXT_FILE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
        JsonObject squareJSON = gson.fromJson(squareGSON, JsonObject.class);

        squareList = new ArrayList<Square>();
        for(Map.Entry<String, JsonElement> element : squareJSON.entrySet()) {
            Square square = gson.fromJson(element.getValue(), Square.class);
            square.setName(element.getKey());
            squareList.add(square);
        }
	}
	
	private static void parsePlot() {
		Scanner scanner;
		try {
			scanner = new Scanner(new File(PLOT_1_FILE));
		} catch (FileNotFoundException e) {
			System.err.printf(PLOT_FILE_ERROR, PLOT_1_FILE);
			return;
		}
		
		/* input squares into game based on x, y coordinates */
		int y = 0;
		while(scanner.hasNextLine()) {
			String[] temp = scanner.nextLine().split(",");
			
			if(temp.length != PLOT_WIDTH) {
				System.err.println(PLOT_SIZE_ERROR);
			}
			for(int x = 0; x < PLOT_WIDTH; x++) {
				int index = squareList.indexOf(new Square(temp[x]));

				if(index == -1) {
				    System.err.printf(SQUARE_NAME_ERROR, temp[x]);
				    return;
                }

				plot[y][x] = new Square(squareList.get(index));
			}
			y++;
		}
		scanner.close();
	}
	
	public static Square getSquare(int x, int y) {
		return plot[y][x];
	}
	
	public static void addSomething(Something thing) {
		plot[thing.getY()][thing.getX()].addThing(thing);
	}
}
