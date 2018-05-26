package Quests;

import Events.Event;
import Events.Next;

import java.util.List;

import static Main.Game.displayEvent;
import static Main.Game.me;

public class EventSequence {

    private static final String QUEST_START_ERROR = "ERROR: Thing " +
            "went wrong with a scene in this sequence.";

    private List<QuestEvent> eventSeries;

    // even numbers hold item name
    // odd numbers hold item amount
    private List<Object> rewards;
    private List<Object> expenses;

    private String button;
    private double timeLength;

    public static Event chooseEvent(QuestEvent questEvent, List<QuestEvent>
            eventSeries) {
        switch(questEvent.getType()) {
            case "NEXT":
                return new Next(questEvent, eventSeries);
            case "CHOICE":
                return null;
            default:
                System.err.println(QUEST_START_ERROR);
                return null;
        }
    }

    public void play() {

        // enter into the quest loop
        displayEvent( chooseEvent(eventSeries.get(0), eventSeries) );


        /*for(int x = 0; x < rewards.size(); x+=2) {
            me.addItem((String) rewards.get(x), (Integer) rewards.get(x + 1));
        }
        for(int x = 0; x < expenses.size(); x+=2) {
            me.removeItem( (String) expenses.get(x),
                    (Integer) expenses.get(x + 1) );
        }*/
    }
}
