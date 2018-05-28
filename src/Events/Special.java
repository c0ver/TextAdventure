package Events;

import Quests.EventRoot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class Special extends Event {

    private static final String BUTTON = "Next";

    Map<String, Event> children;

    EventRoot root;
    int next;

    public Special(String title, String text, EventRoot root,
                   List<Event> children, String response, int next) {
        super(title, text);
        this.response = response;
        this.root = root;
        this.next = next;

        this.children = new HashMap<>();
        List<String> buttons = new ArrayList<>();
        for(Event event : children) {
            this.children.put(event.getResponse(), event);
            buttons.add(event.getResponse());
        }
        createButtons(buttons);
    }

    @Override
    public Event chooseNewEvent(String command) {
        if(!(children.get(command) instanceof Special)) {
            root.finish(next);
        }
        return children.get(command);
    }

    @Override
    public void validate() {}
}
