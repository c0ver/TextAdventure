package Things;

import Events.Event;
import Events.Interact;
import Plot.Tile;

public abstract class Plottable extends Thing {

    // the Place this Plottable is on
    private Place location;

    private int xPosition, yPosition;

    // list of quests at this Plottable
    protected String[] quests;

    protected Plottable(String name, String description, Place location, int
            xPosition, int yPosition) {
        super(name, description);
        this.location = location;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
    }

    protected Plottable(Plottable toCopy) {
        super(toCopy);
        location = toCopy.location;
        xPosition = toCopy.xPosition;
        yPosition = toCopy.yPosition;
    }

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

    public Place getLocation() { return location; }

    public Event getEvent(Event parentEvent) {
        return null;
    }
}
