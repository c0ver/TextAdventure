package main;

import Events.Trade;
import Plot.Plot;
import Plot.Square;

import Things.Entity;

public class Something {

	protected String name, description;
	protected int xPosition, yPosition;

	protected Something(String name) {
	    this.name = name;
    }
	
	protected Something(String name, String description) {
		this.name = name;
		this.description = description;
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

	public String getName() {
		return name;
	}
	
	public int getX() {
		return xPosition;
	}
	
	public int getY() {
		return yPosition;
	}
	
	public Square getLocation() {
		return Plot.getSquare(xPosition, yPosition);
	}
	
	public String getDescription() {
		return description;
	}
	
	public Event getEvent(Event parentEvent) {
		return (new Trade(name, description, (Entity) this, parentEvent));
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
