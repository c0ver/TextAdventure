package Deserializers;

import Events.Default;
import Events.Event;
import Events.Next;
import Events.Special;
import Quests.EventRoot;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EventRootDeserializer implements JsonDeserializer<EventRoot> {

    private static final String TRIGGER_ERROR = "ERROR: |%s| is an unknown " +
            "triggerType in event |%s|.\n";

    private static Gson gson = new Gson();

    private JsonArray eventSequence;

    private String name;

    private int triggerID;

    private EventRoot root;

    @Override
    public EventRoot deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

        JsonObject obj = jsonElement.getAsJsonObject();

        name = obj.get("name").getAsString();

        triggerID = obj.get("triggerID").getAsInt();
        String triggerAction = obj.get("triggerAction").getAsString();

        double timeLength = obj.get("timeLength").getAsDouble();

        JsonElement gainList = obj.get("gain");
        JsonElement loseList = obj.get("lose");
        Type itemMap = new TypeToken<Map<Integer, Integer>>() { }.getType();
        Map<Integer, Integer> gain = gson.fromJson(gainList, itemMap);
        Map<Integer, Integer> expenses = gson.fromJson(loseList, itemMap);

        root = new EventRoot(name, gain, expenses, timeLength,
                triggerID, triggerAction);

        eventSequence = obj.get("eventSeries").getAsJsonArray();
        root.setRootEvent(createEvent(0));

        return root;
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

        // to be used when this event is the last one and next Event is not
        // the next EventRoot in the quest sequence
        int next = -1;

        JsonArray childEvents = node.get("children").getAsJsonArray();
        List<Event> children = new ArrayList<>();
        for(int x = 0; x < childEvents.size(); x++) {
            int childIndex = childEvents.get(x).getAsInt();
            if(childIndex == -1) {
                children.add(new Default(triggerID));
                JsonElement element = node.get("next");
                if(element != null) next = element.getAsInt();
                continue;
            }
            children.add(createEvent(childIndex));
        }

        String eventTitle = name + " " + eventIndex;

        return new Special(eventTitle, text, root, children, response, next);
    }
}
