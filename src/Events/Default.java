package Events;

import static Main.Game.me;

public class Default extends Event  {
	
	private static final String[] BUTTON_SET = {"Interact", "North",
			"Inventory", "West", "South", "East"};

	public Default(String title, String text, Event nextEvent) {
		super(title, text, nextEvent, BUTTON_SET);
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

}
