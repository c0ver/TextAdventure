package Quests;

import Deserializers.EventNodeDeserializer;
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
    private int ID;

    private List<EventRoot> story;

    private int progress;

    public static Event doQuest(int ID) {
        Quest quest = questMap.get(ID);
        if(quest.progress == quest.story.size()) {
            System.err.printf(QUEST_FINISH_ERROR, quest.name);
            return null;
        }

        return quest.story.get(quest.progress++).getEvent();
    }

    public static void parseQuest() {

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(EventRoot.class, new
                EventNodeDeserializer());
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
            questMap.put(quest.ID, quest);
        }
    }

}
