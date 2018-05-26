package Quests;

import java.util.List;

/**
 * Holds information about a simple quest event
 */
public class QuestEvent {

    private static final String NEXT_TYPE_EVENT_ERROR = "ERROR: index given " +
            "was not 0.";

    private String type, text;

    /* holds indexes for next events in EventSequence*/
    private List<Integer> next;

    public String getType() {
        return type;
    }

    public String getText() { return text; }

    // for questEvents of type NEXT since there is only one in the list
    public int getNext(int index) {
        if(index != 0) {
            System.err.println(NEXT_TYPE_EVENT_ERROR);
        }

        return next.get(index);
    }

    // for questEvents with more than 1 option
    public List<Integer> getNext() {
        return next;
    }
}
