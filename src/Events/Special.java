package Events;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class Special extends Event {

    Map<String, Event> children;

    public Special(String title, String text,
                   List<Event> children, String response) {
        super(title, text);
        this.response = response;

        this.children = new HashMap<>();
        for(Event event : children) {
            this.children.put(event.getResponse(), event);
        }
    }

    @Override
    public Event chooseNewEvent(String command) {
       return children.get(command);
    }

    @Override
    public void validate() {}
}
