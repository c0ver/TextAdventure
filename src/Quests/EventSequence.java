package Quests;

import java.util.List;

public class EventSequence {
    private List<QuestEvent> eventSeries;

    // even numbers hold item name
    // odd numbers hold item amount
    private List<Object> rewards;
    private List<Object> expenses;

    private String name;
    private double timeLength;
}
