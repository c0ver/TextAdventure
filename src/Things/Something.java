package Things;

import Events.Trade;
import static Main.Game.mainMap;
import Plot.Plot;
import Plot.Tile;

import Events.Event;

public class Something {

    protected String name, description;
    protected int xPosition, yPosition;

    protected Place currentPlace;

    protected Something(String name) {
        this.name = name;
    }

    protected Something(String name, String description) {
        this.name = name;
        this.description = description;
        currentPlace = mainMap;
    }

    protected Something(String name, String description, int x, int y) {
        this(name, description);
        xPosition = x;
        yPosition = y;
    }

    protected Something(Something toCopy) {
        name = toCopy.name;
        description = toCopy.description;
    }

    public void setName(String name) { this.name = name; }

    public String getName() {
        return name;
    }

    public int getX() {
        return xPosition;
    }

    public int getY() {
        return yPosition;
    }

    public Tile getLocation() {
        Tile location = currentPlace.getPlot().getTile(xPosition, yPosition);
        if(location.getThing() instanceof Place) {
            enterVillage((Place) location.getThing());
        }

        return currentPlace.getPlot().getTile(xPosition, yPosition);
    }

    public String getDescription() {
        return description;
    }

    public Event getEvent(Event parentEvent) {
        return (new Trade(name, description, (Entity) this, parentEvent));
    }

    public Place getCurrentPlace() {
        return currentPlace;
    }

    public void enterVillage(Place place) {
        currentPlace = place;
        xPosition = yPosition = 0;
    }

    public boolean exitCurrentPlace() {
        if(currentPlace.getParentPlace() == null) {
            return false;
        }

        currentPlace = currentPlace.getParentPlace();
        return true;
	}

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {

        if(!(o instanceof Something)) {
            return false;
        }

        Something obj = (Something) o;

        return name.equals(obj.getName());
    }
}
