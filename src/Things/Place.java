package Things;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

import Events.Event;
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

    private static Map<Integer, Place> placeMap = new HashMap<>();

    private List<String> noticeBoard;
    private Plot plot;
    private int size;
    private boolean hasEntry;

    /**
     * Called to create the main map of the game
     */
    public Place(String name, int size, int id) {
        super(name, id, null, null);
        this.size = size;
        plot = new Plot(name, size);
        hasEntry = false;

        placeMap.put(id, this);
    }

    public Event interact(Event parentEvent) { return null; }

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

    public static Collection<Place> getPlaceList() { return placeMap.values(); }

    public static Map<Integer, Place> createVillages() {
        Gson gson = new Gson();
        FileReader villageGSON = null;
        try {
            villageGSON = new FileReader(VILLAGE_INFO_FILE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        JsonObject villageJSON = gson.fromJson(villageGSON, JsonObject.class);

        for(Map.Entry<String, JsonElement> element : villageJSON.entrySet()) {
            Place place = gson.fromJson(element.getValue(), Place.class);
            place.setName(element.getKey());
            place.plot = new Plot(place.getName(), place.size);
            place.setLocation(mainMap);

            mainMap.plot.addPlottable(place);
            placeMap.put(place.getid(), place);
        }
        System.err.println("PLACE " + placeMap);
        return placeMap;
    }
}
