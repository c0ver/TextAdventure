package Events;

import Things.Entity;
import main.Event;

import java.util.ArrayList;

public class Inventory extends Event {
	
	private static final String INVENTORY_TEXT = "Inventory Screen.";
	
	Inventory(Entity person, Event parentEvent) {
		super("checkInventory", INVENTORY_TEXT, parentEvent);
		other = person;
		
		/* include an extra space for "go back" */
		ArrayList<String> buttonSet = new ArrayList<>();
		buttonSet.add("Go Back");
		buttonSet.addAll(other.getInventory());
		
		createButtons(buttonSet);
	}

	@Override
	public Event chooseNewEvent(String command) {
		if(command.equals("Go Back")) {
			return parentEvent;
		}
		
		if(parentEvent instanceof Trade) {
			return parentEvent.chooseNewEvent(command);
		}

		System.err.println(BUTTON_ERROR);
		return null;
	}
}
