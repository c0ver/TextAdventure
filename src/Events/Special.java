package Events;

import Quests.EventRoot;
import javafx.scene.control.Button;

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
                   List<Event> childrenEvents, String response, int next) {
        super(title, text, createButtonNames(childrenEvents));
        this.response = response;
        this.root = root;
        this.next = next;


        children = new HashMap<>();
        for(Event event : childrenEvents) {
            children.put(event.getResponse(), event);
        }
    }

    private static String[] createButtonNames(List<Event> childrenEvents) {
        List<String> buttonNames = new ArrayList<>();
        for(Event event : childrenEvents) {
            buttonNames.add(event.getResponse());
        }

        return buttonNames.toArray(new String[0]);
    }

    @Override
    public Event chooseNewEvent(Button button) {
        String command = button.getText();

        if(!(children.get(command) instanceof Special)) {
            root.finish(next);
        }
        return children.get(command);
    }

    @Override
    public void validate() {}
}
