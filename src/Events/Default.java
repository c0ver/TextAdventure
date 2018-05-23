package Events;

import static Main.Game.me;

public class Default extends Event  {
	
	private static final String[] BUTTON_SET = {"Interact", "North",
			"Inventory", "West", "South", "East"};

	public Default(String title, String text, Event  nextEvent) {
		super(title, text, nextEvent, BUTTON_SET);
	}

	@Override
	public Event  chooseNewEvent(String command) {

		switch (command) {
			case "Interact":
				return me.getLocation().interact(this);
			case "Inventory":
				return new Inventory(me, this);
			case "North":
				me.goUp();
				break;
			case "South":
				me.goDown();
				break;
			case "West":
				me.goLeft();
				break;
			case "East":
				me.goRight();
				break;
	
			default:
				System.err.println(BUTTON_ERROR);
				return null;
		}
		return me.getLocation().getEvent();
	}

}
