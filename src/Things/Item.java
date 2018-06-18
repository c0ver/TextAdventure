package Things;

import Items.Clothing;
import Items.Consumable;
import Items.Tool;
import com.google.gson.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class Item extends Thing {

    public enum Rarity {COMMON, UNCOMMON, RARE, LEGENDARY}
    public enum Type {TOOL, CLOTHING, MISC, CONSUMABLE}

    private static final String ITEM_LIST_FILE = "assets/json/itemList.json";
    private static final String ITEM_FILE_ERROR = "ERROR: %s was not found\n";
    private static final String ITEM_TYPE_ERROR = "ERROR: %s is not a valid " +
            "item type\n";

    public static final int MONEY_ID = 10000;

    private static Map<Integer, Item> itemMap;

    private int[] value;
	private int weight;
	private Rarity rarity;
	private Type type;

	public Item(String name) {
		super(name);
	}
	
	protected Item(String name, int id, String description, Rarity rarity,
                   Type type, int[] value) {
	    super(name, id, description);
	    this.rarity = rarity;
	    this.type = type;
	    this.value = value;
	}

	public int[] getValue() {
		return value;
	}

	public static Map<Integer, Item> getItemMap() { return itemMap; }

	public static int[] getValue(int itemID) { return itemMap.get(itemID).value; }

	public static Map<Integer, Item> createItems() {
        Gson gson = new Gson();
        FileReader itemGSON = null;
        try {
            itemGSON = new FileReader(ITEM_LIST_FILE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(0);
        }
        JsonObject itemJSON = gson.fromJson(itemGSON, JsonObject.class);

        itemMap = new HashMap<>();
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
            itemMap.put(item.getid(), item);
        }

        return itemMap;
    }
}
