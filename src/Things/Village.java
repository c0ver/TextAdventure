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

import static Main.Game.mainPlot;

public class Village extends Something {

    private static final String VILLAGE_INFO_FILE = "assets/villageInfo.json";

    private static List<Village> villageList;

    private List<String> noticeBoard;
    private Plot plot;
    private int size;

    public Village(String name) {
        super(name);
    }

    public Plot getPlot() {
        return plot;
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

        villageList = new ArrayList<>();
        for(Map.Entry<String, JsonElement> element : villageJSON.entrySet()) {
            Village village = gson.fromJson(element.getValue(), Village.class);
            village.setName(element.getKey());
            village.plot = new Plot(village.getName(), village.size);
            mainPlot.add(village);
            villageList.add(village);
        }
    }
}
