package Deserializers;

import Events.QuestEvent;
import Events.EventSequence;
import Events.Next;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class EventSequenceDeserializers implements JsonDeserializer<EventSequence> {

    @Override
    public EventSequence deserialize(JsonElement json, Type typeOfT,
                                     JsonDeserializationContext context) throws
            JsonParseException {
        JsonObject eventSequence = json.getAsJsonObject();
        JsonArray events = eventSequence.getAsJsonArray("events");

        String name = eventSequence.get("name").getAsString();

        List<QuestEvent> eventSeries = new ArrayList<>();
        for (int index = 0; index < events.size(); index++) {
            JsonElement element = events.get(index);
            JsonObject event = element.getAsJsonObject();
            String eventType = event.get("type").getAsString();

            /*eventSeries.add(
                new QuestEvent(name, event.get("text").getAsString(),
                        eventType, ) );

            switch(eventType) {
                case "NEXT":
                    eventSeries.add(
                            new Next(name, event.get("text").getAsString(), null) );
                    break;
                case "CHOICE":
                    eventSeries.add(
                            new Choice(name, event.get("text").getAsString(),
                                    null) );
                    break;
            }*/
        }
        return null;
    }
}
