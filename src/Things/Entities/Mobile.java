package Things.Entities;

import Quests.Quest;
import Things.Place;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A class for the player and more persistent NPCs that can move
 */
public class Mobile extends Entity {

    private static final String NPC_LIST_FILE =
            "assets/json/npcList.json";

    private static Map<Integer, Mobile> npcMap;

    private Map<String, Quest> talkOptions;

    public Mobile(String name, Place location) {
        super(name, location);
    }

    public void move(int xDelta, int yDelta) {

        int moveStatus = checkMove(xDelta, yDelta);

        if(moveStatus == 1)         setPosition(getX() + xDelta, getY() + yDelta);
        else if(moveStatus == 0)    exitLocation(xDelta, yDelta);
        else                        return;

        if(getCurrentTile().getPlottable() instanceof Place) {
            enterVillage((Place) getCurrentTile().getPlottable());
        }
    }

    /**
     * @return -1 for invalid move, 0 for exiting location, 1 for normal move
     */
    private int checkMove(int xDelta, int yDelta) {
        int x = xDelta + getX();
        int y = yDelta + getY();
        int max = getLocation().getSize();

        if(x >= 0 && x < max && y >= 0 && y < max)  return 1;
        else if(canLeaveLocation())                 return 0;
        else                                        return -1;
    }

    /**
     * Assumes that this object is on the edge of the plot
     */
    private boolean canLeaveLocation() {

        // on the mainMap
        if(!getLocation().hasOuter()) {
            return false;
        }

        // city allows leaving freely or on specified entry/exit tile (ROAD)
        return !getLocation().hasEntry() || getLocation().onExitTile(getX(), getY());
    }

    private void enterVillage(Place place) {
        setLocation(place);
        setPosition(0, 0);
    }

    private void exitLocation(int xDelta, int yDelta) {
        setPosition(xDelta + getLocation().getX(), yDelta + getLocation().getY());
        setLocation(getLocation().getLocation());
    }

    public static Mobile getNPC(int index) { return npcMap.get(index); }

    public static void createNPCs() {
        Gson gson = new Gson();
        FileReader npcJSON;
        try {
            npcJSON = new FileReader(NPC_LIST_FILE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }

        JsonObject npcObj = gson.fromJson(npcJSON, JsonObject.class);

        npcMap = new HashMap<>();
        for (Map.Entry<String, JsonElement> element : npcObj.entrySet()) {
            Mobile npc = gson.fromJson(element.getValue(), Mobile.class);
            npc.finishGSON(element);
            npcMap.put(npc.getID(), npc);
        }
    }
}
