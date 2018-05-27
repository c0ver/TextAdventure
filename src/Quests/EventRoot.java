package Quests;

import Events.Event;
import Things.Plottable;

import java.util.Map;

public class EventRoot {

    // holds the children
    private Event rootEvent;

    private Map<String, Integer> rewards;
    private Map<String, Integer> expenses;

    private double timeLength;

    private String name;

    private Plottable trigger;
    private String triggerAction;

    public EventRoot(String name, Event rootEvent, Map<String, Integer> rewards,
                     Map<String, Integer> expenses, double timeLength, Plottable
                     trigger, String triggerAction) {
        this.name = name;
        this.rootEvent = rootEvent;
        this.rewards = rewards;
        this.expenses = expenses;
        this.timeLength = timeLength;
        this.trigger = trigger;
        this.triggerAction = triggerAction;
    }

    public Event getEvent() {
        return rootEvent;
    }
}
