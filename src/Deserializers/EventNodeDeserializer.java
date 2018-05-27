package Deserializers;

import Events.Default;
import Events.Event;
import Events.Next;
import Events.Special;
import Quests.EventRoot;
import Things.Entities.Mobile;
import Things.Plottable;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EventNodeDeserializer implements JsonDeserializer<EventRoot> {

    private static final String TRIGGER_ERROR = "ERROR: |%s| is an unknown " +
            "triggerType in event |%s|.\n";

    private static Gson gson = new Gson();

    private JsonArray eventSequence;

    private Plottable trigger;

    private String name;

    @Override
    public EventRoot deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

        JsonObject obj = jsonElement.getAsJsonObject();

        name = obj.get("name").getAsString();

        int triggerID = obj.get("triggerID").getAsInt();
        String triggerType = obj.get("triggerType").getAsString();
        String triggerAction = obj.get("triggerAction").getAsString();
        switch(triggerType) {
            case "NPC":
                trigger = Mobile.getNPC(triggerID);
                break;
            default:
                System.err.printf(TRIGGER_ERROR, triggerType, name);
                break;
        }

        double timeLength = obj.get("timeLength").getAsDouble();

        JsonElement rewardList = obj.get("rewards");
        JsonElement expenseList = obj.get("expenses");
        Type itemMap = new TypeToken<Map<String, Integer>>() { }.getType();
        Map<String, Integer> rewards = gson.fromJson(rewardList, itemMap);
        Map<String, Integer> expenses = gson.fromJson(expenseList, itemMap);

        eventSequence = obj.get("eventSeries").getAsJsonArray();
        Event rootEvent = createEvent(0);

        return new EventRoot(name, rootEvent, rewards, expenses, timeLength,
                trigger, triggerAction);
    }

    /**
     * A recursive function that creates a tree from the root event
     * @param eventIndex    The index of the event to make
     * @return              The event created
     */
    private Event createEvent(int eventIndex) {

        JsonObject node = eventSequence.get(eventIndex).getAsJsonObject();

        String text = node.get("text").getAsString();
        String response = node.get("response").getAsString();

        JsonArray childEvents = node.get("children").getAsJsonArray();
        List<Event> children = new ArrayList<>();
        for(int x = 0; x < childEvents.size(); x++) {
            int childIndex = childEvents.get(x).getAsInt();
            if(childIndex == -1) {
                children.add(new Default(trigger));
                continue;
            }
            children.add(createEvent(childIndex));
        }

        String eventTitle = name + " " + eventIndex;

        if(children.size() == 1) {
            return new Next(eventTitle, text, children.get(0), response);
        }
        return new Special(eventTitle, text, children, response);
    }
}
