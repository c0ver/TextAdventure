package Things;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import Plot.Plot;
import Plot.Tile;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import static Main.Game.mainMap;

public class Place extends Plottable {

    private static final String EXIT_TILE = "ROAD";

    private static final String VILLAGE_INFO_FILE = "assets/json/villageInfo" +
            ".json";

    private static List<Place> placeList = new ArrayList<>();

    private List<String> noticeBoard;
    private Plot plot;
    private int size;
    private boolean hasEntry;

    /**
     * Called to create the main map of the game
     */
    public Place(String name, int size) {
        super(name, null, null, -1, -1);
        this.size = size;
        plot = new Plot(name, size);
        hasEntry = false;

        placeList.add(this);
    }

    public boolean onExitTile(int x, int y) {
        return getTile(x, y).findTile(EXIT_TILE);
    }

    public boolean hasEntry() {
        return hasEntry;
    }

    public boolean hasOuter() {
        return getLocation() != null;
    }

    public Tile getTile(int x, int y) {
        return plot.getTile(x, y);
    }

    public Plot getPlot() {
        return plot;
    }

    public int getSize() {
        return size;
    }

    public static List<Place> getPlaceList() { return placeList; }

    public static void createVillages() {
        Gson gson = new Gson();
        FileReader villageGSON;
        try {
            villageGSON = new FileReader(VILLAGE_INFO_FILE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
        JsonObject villageJSON = gson.fromJson(villageGSON, JsonObject.class);

        for(Map.Entry<String, JsonElement> element : villageJSON.entrySet()) {
            Place place = gson.fromJson(element.getValue(), Place.class);
            place.setName(element.getKey());
            place.plot = new Plot(place.getName(), place.size);
            place.setLocation(mainMap);

            mainMap.plot.addPlottable(place);
            placeList.add(place);
        }
    }
}
