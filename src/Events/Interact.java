package Events;

import Quests.Quest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Interact extends Event {

    private Map<String, Quest> talkOptions;

    private static final String BUTTON = "Go Back";

    public Interact(String title, String text,
                    Map<String, Quest> talkOptions, Event parentEvent) {
        super(title, text, parentEvent);
        this.talkOptions = talkOptions;

        List<String> buttonSet = new ArrayList<>(talkOptions.keySet());
        buttonSet.add(BUTTON);
        createButtons(buttonSet);
    }

    @Override
    public Event chooseNewEvent(String command) {
        if(command.equals(BUTTON)) {
            return parentEvent;
        }

        return null;
    }

    @Override
    public void validate() {}
}
