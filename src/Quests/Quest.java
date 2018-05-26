package Quests;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Quest {

    private static final String QUEST_FILE = "assets/json/questList.json";

    private static final String QUEST_FINISH_ERROR =
            "ERROR: Quest: %s is already finished.\n";

    private static Map<String, Quest> questMap;

    private String name, description;

    private List<EventSequence> story;

    private int progress;

    public static void doQuest(String ID) {
        Quest quest = questMap.get(ID);
        if(quest.progress == quest.story.size()) {
            System.err.printf(QUEST_FINISH_ERROR, ID);
            return;
        }

        quest.story.get(quest.progress).play();
        quest.progress++;
    }

    public static void parseQuest() {

        Gson gson = new Gson();
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
            questMap.put(element.getKey(),
                    gson.fromJson(element.getValue(), Quest.class) );
        }
    }

}
