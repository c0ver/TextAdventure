package Events;

public class Interact extends Event {

    public Interact(String title, String text, Event nextEvent) {

    }

    @Override
    public Event chooseNewEvent(String command) {
        return null;
    }
}
