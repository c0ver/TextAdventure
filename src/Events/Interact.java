package Events;

import Plot.Plot;
import Quests.Quest;
import Things.Entities.Entity;
import Things.Plottable;
import javafx.scene.control.Button;

import java.util.*;

public class Interact extends Event {

    private Map<String, Quest> talkOptions;

    private static final String BUTTON = "Go Back";

    public Interact(Plottable plottable, Event parentEvent) {
        super(plottable.getName(), plottable.getDescription(), parentEvent,
                null, plottable.getInteractions());
        other = plottable;

        if(other.getQuests() != null) {
            talkOptions = new HashMap<>();
            for(Quest quest : other.getQuests()) {
                talkOptions.put(quest.nextStep(), quest);
            }
        }
    }

    @Override
    public Event chooseNewEvent(Button button) {
        String command = button.getText();

        switch(command) {
            case BUTTON:
                return parentEvent;
            case "Trade":
                return new Trade(other.getName(), other.getDescription(),
                        (Entity) other, this);
        }
        return talkOptions.get(command).doQuest();
    }

    @Override
    public void validate() {}
}
