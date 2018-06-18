package Events;

import Things.Entities.Entity;
import javafx.scene.control.Button;

import java.util.ArrayList;
import java.util.List;

public class Inventory extends Event {

	private static final String INVENTORY_TEXT = "Inventory Screen.";
	
	Inventory(Entity person, Event parentEvent) {
		super("checkInventory", INVENTORY_TEXT, parentEvent, person,
				person.getInventoryItemNames());
		addUserDataToButtons(person.getInventory().toArray(new Object[0]));
	}

	@Override
	public Event chooseNewEvent(Button button) {
		String command = button.getText();

		if(command.equals(Event.RETURN)) {
			return parentEvent;
		}
		
		if(parentEvent instanceof Trade) {
			return parentEvent.chooseNewEvent(button);
		}

		System.err.println(BUTTON_ERROR);
		return null;
	}

	@Override
    public void validate() {}
}
