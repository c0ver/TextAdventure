package Events;

import Plot.Tile;
import Things.Plottable;

import static Main.Game.me;

public class Default extends Event  {
	
	private static final String[] BUTTON_SET = {"Interact", "North",
			"Inventory", "West", "South", "East"};

	private int triggerID;

	public Default(Tile tile) {
		super(tile.getid(), tile.getDescription(), BUTTON_SET);
	}

	/**
	 * To use when the final location to be is not known yet
	 * @param triggerID		Go to this plottable when this event is triggered
	 */
	public Default(int triggerID) {
	    super("Placeholder", "Placeholder", BUTTON_SET);
	    this.triggerID = triggerID;

	    // Special events that lead here need a button to come here
	    this.response = "Next";
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
		if(triggerID == 0) return;

		// player's id will be -1
		if(triggerID != -1) {
			Plottable plottable = Plottable.getPlottable(triggerID);
			me.setLocation(plottable.getLocation());
			me.setPosition(plottable.getX(), plottable.getY());
		}
		resetEvent(me.getCurrentTile().getid(),
				me.getCurrentTile().getDescription());
	}
}
