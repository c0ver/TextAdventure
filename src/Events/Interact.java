package Events;

import Quests.Quest;
import Things.Plottable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Interact extends Event {

    private Map<String, Quest> talkOptions;

    private static final String BUTTON = "Go Back";

    public Interact(Plottable plottable, Event parentEvent) {
        super(plottable.getName(), plottable.getDescription(), parentEvent);

        talkOptions = new HashMap<>();
        List<String> buttons = new ArrayList<>();
        for(Quest quest : plottable.getQuests()) {
            talkOptions.put(quest.nextStep(), quest);
            buttons.add(quest.nextStep());
        }

        buttons.add(BUTTON);
        createButtons(buttons);
    }

    @Override
    public Event chooseNewEvent(String command) {
        if(command.equals(BUTTON)) {
            return parentEvent;
        }
        return talkOptions.get(command).doQuest();
    }

    @Override
    public void validate() {}
}
