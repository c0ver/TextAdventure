package Events;

import Things.Entities.Entity;

public class Next extends Event {
	
	private static final String[] BUTTON_SET = {"Next"};

	// differs from parentEvent in that nextEvent goes forward
	// while parentEvent goes back
	private Event nextEvent;

    public Next(String title, String text, Event nextEvent) {
		super(title, text, BUTTON_SET);
		this.nextEvent = nextEvent;
	}

	public Next(String title, String text, Event nextEvent, Entity other) {
		this(title, text, nextEvent);
		this.other = other;
	}

	@Override
	public Event chooseNewEvent(String command) {
    	if(nextEvent != null) {
    		return nextEvent;
		}
    	return parentEvent;
    }

	@Override
	public void validate() {}
}
