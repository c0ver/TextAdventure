package Events;

import Quests.EventSequence;
import Quests.QuestEvent;
import Things.Entities.Entity;

import java.util.List;

import static Main.Game.me;

public class Next extends Event {
	
	private static final String[] BUTTON_SET = {"Next"};

	private List<QuestEvent> eventSeries;
	private QuestEvent questEvent;

    public Next(String title, String text, Event nextEvent) {
		super(title, text, nextEvent, BUTTON_SET);
	}

	// creates a next event for the given questEvent
	public Next(QuestEvent questEvent, List<QuestEvent> eventSeries) {
        super("Quest Event", questEvent.getText(), BUTTON_SET);
        this.eventSeries = eventSeries;

        int questEventIndex = questEvent.getNext(0);
        if(questEventIndex != -1) {
        	this.questEvent = eventSeries.get(questEventIndex);
        } else {
            nextEvent = me.getCurrentTile().getEvent();
        }
    }

	Next(String title, String text, Event nextEvent, Entity other) {
		this(title, text, nextEvent);
		this.other = other;
	}



	@Override
	public Event chooseNewEvent(String command) {

        // assume when nextEvent is null that we are in a quest, not that
        // there is a problem
        if(nextEvent == null && questEvent != null) {
            return EventSequence.chooseEvent(questEvent, eventSeries);
        }

		return nextEvent;
	}
}
