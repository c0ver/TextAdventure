package Events;

/*
 * Format of this Event:
 * The outcome of this event will be tied to the next two events in
 * the eventSeries List in the deserializer.
 */
public class Choice extends Event {

    private static final String[] BUTTON_SET = {"Fine", "No"};

    private Event[] nextChoice = new Event[2];

    public Choice(String title, String text, Event nextEvent_0, Event
            nextEvent_1) {
        super(title, text,null, BUTTON_SET);

        nextChoice[0] = nextEvent_0;
        nextChoice[1] = nextEvent_1;
    }

    @Override
    public Event chooseNewEvent(String command) {
        if(command.equals(BUTTON_SET[0])){
            return nextChoice[0];
        } else if(command.equals(BUTTON_SET[1])) {
            return nextChoice[1];
        }

        System.err.println("ERROR: Neither of the correct buttons were found.");
        return null;
    }
}
