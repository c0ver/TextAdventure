package Things;

import Events.Event;
import Events.Trade;
import Plot.Tile;
import Quests.Quest;
import Things.Entities.Entity;
import Things.Entities.Mobile;

import java.util.*;

public abstract class Plottable extends Thing {

    private static final int START_POSITION = 3;

    private static Map<Integer, Plottable> plottableMap;

    // the Place this Plottable is on
    private Place location;

    private int xPosition, yPosition;

    // questID
    private List<Quest> quests;

    protected Plottable(String name, int id, String description, Place location) {
        super(name, id, description);
        this.location = location;
        xPosition = yPosition = START_POSITION;
    }

    protected Plottable(Plottable toCopy) {
        super(toCopy);
        location = toCopy.location;
        xPosition = toCopy.xPosition;
        yPosition = toCopy.yPosition;
    }

    public abstract Event interact(Event parentEvent);

    public void addQuest(Quest quest) {
        if(quests == null) quests = new ArrayList<>();
        quests.add(quest);
    }

    public void removeQuest(Quest quest) { quests.remove(quest); }

    public void setPosition(int x, int y) {
        xPosition = x;
        yPosition = y;
    }

    public void setLocation(Place location) { this.location = location; }

    public int getX() { return xPosition; }

    public int getY() { return yPosition; }

    public Tile getCurrentTile() {
        return location.getPlot().getTile(xPosition, yPosition);
    }

    public List<Quest> getQuests() { return quests; }

    public Place getLocation() { return location; }

    public String[] getInteractions() {

        List<String> buttons = new ArrayList<>();

        if(quests != null) {
            for (Quest quest : quests) {
                buttons.add(quest.nextStep());
            }
        }

        if(this instanceof Entity) {
            buttons.add("Trade");
        }

        buttons.add(Event.RETURN);

        return buttons.toArray(new String[0]);
    }

    public String getInfo() {
        if(this instanceof Entity) {
            return this.getInfo();
        }
        return "HI";
    }

    public static Plottable getPlottable(int index) { return plottableMap.get(index); }

    public static Map<Integer, Plottable> createPlottables() {
        plottableMap = new HashMap<>();
        plottableMap.putAll(Place.createVillages());
        plottableMap.putAll(Entity.createMonsters());
        plottableMap.putAll(Mobile.createNPCs());

        return plottableMap;
    }
}
