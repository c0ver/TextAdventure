package Things;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import Plot.Plot;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import static Main.Game.mainMap;

public class Place extends Something {

    private static final String EXIT_TILE = "ROAD";

    private static final String VILLAGE_INFO_FILE = "assets/villageInfo.json";

    private static List<Place> placeList;

    private List<String> noticeBoard;
    private Plot plot;
    private int size;
    private boolean hasEntry;

    private Place parentPlace;

    public Place(String name) {
        super(name);
    }

    public Place(String name, int size, Place parentPlace) {
        this(name);
        this.size = size;
        this.parentPlace = parentPlace;
        plot = new Plot(name, size);
    }

    public boolean onExitTile(int x, int y) {
        return plot.getTile(x, y).searchTile(EXIT_TILE);
    }

    public boolean hasEntry() {
        return hasEntry;
    }

    public Plot getPlot() {
        return plot;
    }

    public Place getParentPlace() {
        return parentPlace;
    }

    public int getSize() {
        return size;
    }

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

        placeList = new ArrayList<>();
        for(Map.Entry<String, JsonElement> element : villageJSON.entrySet()) {
            Place place = gson.fromJson(element.getValue(), Place.class);
            place.setName(element.getKey());
            place.plot = new Plot(place.getName(), place.size);
            place.parentPlace = mainMap;
            mainMap.plot.add(place);
            placeList.add(place);
        }
    }
}
