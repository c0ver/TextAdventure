package Quests;

import Events.Event;

import java.util.List;

public class QuestEvent extends Event {
    private String type;

    /* holds indexes for next events in EventSequence*/
    private List<Integer> next;

    @Override
    public Event chooseNewEvent(String command) {
        return null;
    }
}
