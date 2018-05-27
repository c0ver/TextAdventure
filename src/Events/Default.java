package Events;

import Plot.Tile;
import Things.Plottable;

import static Main.Game.me;

public class Default extends Event  {
	
	private static final String[] BUTTON_SET = {"Interact", "North",
			"Inventory", "West", "South", "East"};

	private Plottable plottable;

	private boolean playerTrigger;

	public Default(Tile tile) {
		super(tile.getID(), tile.getDescription(), BUTTON_SET);
	}

	/**
	 * To use when the final location to be is not known yet
	 * @param plottable		Go to this plottable when this event is triggered
	 */
	public Default(Plottable plottable) {
	    super("Placeholder", "Placeholder", BUTTON_SET);
		this.plottable = plottable;

		// trigger N/A
		// ex: Opening scenes
		if(plottable == null) {
			playerTrigger = true;
		}
	}

	@Override
	public Event chooseNewEvent(String command) {

		switch (command) {
			case "Interact":
				return me.getCurrentTile().interact(this);
			case "Inventory":
				return new Inventory(me, this);
			case "North":
				me.move(0, -1);
				break;
			case "South":
				me.move(0, 1);
				break;
			case "West":
				me.move(-1, 0);
				break;
			case "East":
				me.move(1, 0);
				break;
	
			default:
				System.err.println(BUTTON_ERROR);
				return null;
		}
		return me.getCurrentTile().getEvent();
	}

	@Override
	public void validate() {
		if(plottable == null && !playerTrigger) { return; }
		if(plottable != null) {
			me.setLocation(plottable.getLocation());
			me.setPosition(plottable.getX(), plottable.getY());
		}
		resetEvent(me.getCurrentTile().getID(),
				me.getCurrentTile().getDescription());
	}
}
