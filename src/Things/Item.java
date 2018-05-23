package Things;

import Items.Clothing;
import Items.Consumable;
import Items.Tool;
import com.google.gson.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Item extends Something {

    public enum Rarity {COMMON, UNCOMMON, RARE, LEGENDARY}
    public enum Type {TOOL, CLOTHING, MISC, CONSUMABLE}

    private static final String ITEM_LIST_FILE = "assets/itemList.json";
    private static final String ITEM_FILE_ERROR = "ERROR: %s was not found\n";
    private static final String ITEM_TYPE_ERROR = "ERROR: %s is not a valid " +
            "item type\n";

    public static List<Item> itemList = new ArrayList<>();

	private int weight, value;
	private Rarity rarity;
	private Type type;

	public Item(String name) {
		super(name);
	}
	
	protected Item(String name, String description, Rarity rarity, Type type,
                int value) {
	    super(name, description);
	    this.rarity = rarity;
	    this.type = type;
	    this.value = value;
	}

	int getValue() {
		return value;
	}

	public static void createItems() {
        Gson gson = new Gson();
        FileReader itemGSON;
        try {
            itemGSON = new FileReader(ITEM_LIST_FILE);
        } catch (FileNotFoundException e) {
            System.err.printf(ITEM_FILE_ERROR, ITEM_LIST_FILE);
            e.printStackTrace();
            return;
        }
        JsonObject itemJSON = gson.fromJson(itemGSON, JsonObject.class);

        for (Map.Entry<String, JsonElement> element : itemJSON.entrySet()) {
            Type type = Type.valueOf(
                    element.getValue().getAsJsonObject().get("type").getAsString() );

            Item item;

            switch(type) {
                case TOOL:
                    item = gson.fromJson(element.getValue(), Tool.class);
                    break;
                case CLOTHING:
                    item = gson.fromJson(element.getValue(), Clothing.class);
                    break;
                case CONSUMABLE:
                    item = gson.fromJson(element.getValue(), Consumable.class);
                    break;
                case MISC:
                    item = gson.fromJson(element.getValue(), Item.class);
                    break;
                default:
                    System.err.printf(ITEM_TYPE_ERROR, type.name());
                    continue;
            }
            item.setName(element.getKey());
            itemList.add(item);
        }
    }
}
