package Quests;

import Deserializers.EventRootDeserializer;
import Events.Event;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Quest {

    private static final String QUEST_FILE = "assets/json/questList.json";

    private static final String QUEST_FINISH_ERROR =
            "ERROR: Quest: %s is already finished.\n";

    private static Map<Integer, Quest> questMap;

    private String name, description;
    private int id;

    private List<EventRoot> story;

    private int next;

    public Event doQuest() {
        if(next == story.size()) {
            System.err.printf(QUEST_FINISH_ERROR, name);
            return null;
        }

        Event event = story.get(next).getEvent();

        // remove the this quest before adding on the new one
        removeQuestLink();
        next++;
        if(next < story.size()) linkQuest();

        return event;
    }

    private void linkQuest() {
        story.get(next).addToTrigger(this);
    }

    private void removeQuestLink() {
        story.get(next).removeFromTrigger(this);
    }

    public String nextStep() { return story.get(next).getName(); }

    public void setNext(int next) { this.next = next; }

    public EventRoot getEventRoot(int index) { return story.get(index); }

    public static Quest getQuest(int id) { return questMap.get(id); }

    public static void parseQuest() {

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(EventRoot.class, new
                EventRootDeserializer());
        Gson gson = gsonBuilder.create();
        FileReader questGSON;
        try {
            questGSON = new FileReader(QUEST_FILE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
        JsonObject questJSON = gson.fromJson(questGSON, JsonObject.class);

        questMap = new HashMap<>();
        for(Map.Entry<String, JsonElement> element : questJSON.entrySet()) {
            Quest quest = gson.fromJson(element.getValue(), Quest.class);
            questMap.put(quest.id, quest);

            for(EventRoot root : quest.story) {
                root.setMyQuest(quest);
            }

            // attach initial eventRoot in each quest to triggers
            // except for starting event
            if(quest.id != 0) quest.linkQuest();
        }
    }

}
