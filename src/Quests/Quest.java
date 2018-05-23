package Quests;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Quest {

    private static final String QUEST_FILE = "assets/questList.json";

    private static List<Quest> questList;

    private String name, description;

    private List<EventSequence> story;

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

        questList = new ArrayList<>();

        for(Map.Entry<String, JsonElement> element : questJSON.entrySet()) {
            questList.add(gson.fromJson(element.getValue(), Quest.class));
        }

        System.err.println(questList);
    }

}
