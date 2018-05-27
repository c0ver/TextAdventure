package Events;

import Things.Entities.Entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Inventory extends Event {

    private static final String BUTTON = "Go Back";
	private static final String INVENTORY_TEXT = "Inventory Screen.";
	
	Inventory(Entity person, Event parentEvent) {
		super("checkInventory", INVENTORY_TEXT, parentEvent);
		other = person;
		
		/* include an extra space for "go back" */
		List<String> buttonSet = new ArrayList<>();
		buttonSet.add(BUTTON);
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

	@Override
    public void validate() {}
}
