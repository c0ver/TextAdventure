package Events;

import Things.Entity;
import main.Event;

public class Next extends Event {
	
	private static final String[] BUTTON_SET = {"Next"};

	Next(String title, String mainText, Event parentEvent) {
		super(title, mainText, parentEvent, BUTTON_SET);
	}

	Next(String title, String mainText, Event parentEvent, Entity other) {
		this(title, mainText, parentEvent);
		this.other = other;
	}

	@Override
	public Event chooseNewEvent(String command) {
		return parentEvent;
	}
}
