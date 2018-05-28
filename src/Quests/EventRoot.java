package Quests;

import Events.Event;
import Things.Plottable;

import java.util.Map;

import static Main.Game.me;

public class EventRoot {

    private static final String NULL_TRIGGER_ERROR =
            "ERROR: %d is not a valid id.\n";

    private Quest myQuest;

    // holds the children
    private Event rootEvent;

    private Map<String, Integer> rewards;
    private Map<String, Integer> expenses;

    private double timeLength;

    private String name;

    private int triggerID;
    private String triggerAction;

    public EventRoot(String name, Map<String, Integer> rewards,
                     Map<String, Integer> expenses, double timeLength, int
                     triggerID, String triggerAction) {
        this.name = name;
        this.rewards = rewards;
        this.expenses = expenses;
        this.timeLength = timeLength;
        this.triggerID = triggerID;
        this.triggerAction = triggerAction;
    }

    public void finish(int next) {
        if(next != -1) {
            myQuest.setNext(next);

            // if this eventSeries doesn't go anywhere, don't do anything
            if( name.equals(myQuest.nextStep()) ) return;
        }

        me.addItems(rewards);
        me.removeItems(expenses);
    }

    public void addToTrigger(Quest quest) {
        Plottable.getPlottable(triggerID).addQuest(quest);
    }

    public void removeFromTrigger(Quest quest) {
        Plottable plottable = Plottable.getPlottable(triggerID);

        if(plottable == null) {
            System.err.printf(NULL_TRIGGER_ERROR, triggerID);
            return;
        }

        Plottable.getPlottable(triggerID).removeQuest(quest);
    }

    public void setRootEvent(Event rootEvent) { this.rootEvent = rootEvent; }

    public void setMyQuest(Quest quest) { myQuest = quest; }

    public Event getEvent() { return rootEvent; }

    public String getName() { return name; }

    @Override
    public boolean equals(Object o) {
        if(o instanceof EventRoot) {
            return ((EventRoot) o).name.equals(name);
        }
        return false;
    }
}
