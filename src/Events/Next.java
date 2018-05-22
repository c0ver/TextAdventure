package Events;

import Things.Entity;

public class Next extends Event {
	
	private static final String[] BUTTON_SET = {"Next"};

    public Next(String title, String text, Event nextEvent) {
		super(title, text, nextEvent, BUTTON_SET);
	}

	Next(String title, String text, Event nextEvent, Entity other) {
		this(title, text, nextEvent);
		this.other = other;
	}



	@Override
	public Event chooseNewEvent(String command) {
		return nextEvent;
	}
}
