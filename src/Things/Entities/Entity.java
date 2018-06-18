package Things.Entities;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Things.Item;
import Things.Place;
import Things.Plottable;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import Events.Fight;
import Events.Event;

import static Main.Game.me;

public class Entity extends Plottable {

    private static final String MONSTER_LIST_FILE =
            "assets/json/monsterList.json";
    private static final String ENTITY_FILE_ERROR =
            "ERROR: %s was not found\n";
    private static final String ITEM_MISSING_ERROR = "ERROR: %s was not found.";
	private static final String NEGATIVE_ENERGY_ERROR =
            "ERROR: Energy is negative";

	private static final String ENTITY_INFO =
            "%s\nHP: %d\nEnergy: %d\nLevel: %d\nMoney:";

    public static final int MONEY_TYPES = 3;
    public static final int COPPER_INDEX = 0;
    public static final int SILVER_INDEX = 1;
    public static final int GOLD_INDEX = 2;
    private static final int[] CONVERSION_RATE = {1, 16, 128};

	private static final int TOTAL_STAT_COUNT = 2;
	private static final int HP_INDEX = 0;
    private static final int ENERGY_INDEX = 1;

    private static final int BASE_MULTIPLIER = 10;


    private static Map<Integer, Entity> monsterMap;

	private int[] stats, money;

	private int level;
	private double statMultiplier, tempModifier;
	private double baseAttack;
	private List<Integer> inventory;

	/* creates the player */
	public Entity(String name, int id, Place location) {
        super(name, id, null, location);
        level = 1;
        statMultiplier = 1;
        tempModifier = 1;
        money = new int[MONEY_TYPES];

        stats = new int[TOTAL_STAT_COUNT];
        for(int i = 0; i < TOTAL_STAT_COUNT; i++) {
            stats[i] = (int) (level * statMultiplier * BASE_MULTIPLIER);
        }

        inventory = new ArrayList<>();
	}

    private Entity(Entity toCopy) {
        super(toCopy);
        level = toCopy.level;
        money = toCopy.money;
        statMultiplier = toCopy.statMultiplier;
        tempModifier = toCopy.tempModifier;
        baseAttack = toCopy.baseAttack;
        inventory = new ArrayList<>(toCopy.inventory);
        stats = new int[TOTAL_STAT_COUNT];

        System.arraycopy(toCopy.stats, 0, stats, 0, TOTAL_STAT_COUNT);
    }

    public Event interact(Event parentEvent) { return null; }

	public boolean loseHP(int amount) {
        stats[HP_INDEX] -= amount;
        if(stats[HP_INDEX] <= 0) {
            stats[HP_INDEX] = 0;
            return false;
        }
        return true;
    }

    public void loseEnergy(String action) {
        switch(action) {
            case "Attack":
                stats[ENERGY_INDEX] -= 1;
                break;
            case "Defend":
                stats[ENERGY_INDEX] -= 1;
                break;
            default:
                break;
        }

        if(stats[ENERGY_INDEX] < 0) {
            System.err.println(NEGATIVE_ENERGY_ERROR);
        }
    }

    public int energyCost(String action) {
        switch(action) {
            case "Attack":
                return 1;
            case "Defend":
                return 1;
            default:
                return 0;
        }
    }

    public void gainMoney(int[] amount) {
	    for(int x = 0; x < MONEY_TYPES; x++) {
	        money[x] += amount[x];
        }
	}

	public void loseMoney(int[] amount) {
        int remainder = getTotalMoney(money) - getTotalMoney(amount);

        for(int x = MONEY_TYPES - 1; x >= 0; x--) {
            money[x] = remainder / CONVERSION_RATE[x];
            remainder -= money[x] * CONVERSION_RATE[x];
        }
	}

	public boolean enoughMoney(int[] amount) {
        return getTotalMoney(money) > getTotalMoney(amount);
    }

	public void removeItem(int itemID) { inventory.remove((Integer) itemID); }

    public void removeItems(Map<Integer, Integer> items) {
        for(int itemID : items.keySet()) {
            for(int x = 0; x < items.get(itemID); x++) {
                inventory.remove((Integer) itemID);
            }
        }
    }

	public void addItem(int itemID) {

	    // special case if the item is money
	    if(itemID >= Item.MONEY_ID) {
	        money[itemID - Item.MONEY_ID]++;
	        return;
        }

	    inventory.add(itemID);
	}

    public void addItems(Map<Integer, Integer> items) {
        for(int itemID : items.keySet()) {
            for(int x = 0; x < items.get(itemID); x++) {
                addItem(itemID);
            }
        }
    }

	public void loot(Entity toLoot) {
	    for(int itemID : toLoot.inventory) addItem(itemID);
	    for(int x = 0; x < MONEY_TYPES; x++) {
	        money[x] += toLoot.money[x];
	        toLoot.money[x] = 0;
        }
        toLoot.inventory.clear();
    }

    public boolean trade(Entity other, int itemID) {
        int[] itemValue = other.getItemValue(itemID);

	    if(!enoughMoney(itemValue)) return false;

	    loseMoney(itemValue);
        other.gainMoney(itemValue);

        other.removeItem(itemID);
        addItem(itemID);

        return true;
    }

    /**
     * Modifies the tempModifier to affect damage dealt/taken during a fight
     *
     * @param change    >1: increases dmg dealt/taken
     *                  <1: decreases dmg dealt/taken
     */
	public void changeTempModifier(double change) {
	    if(change == 1) {
	        tempModifier = 1;
	        return;
        }

	    tempModifier *= change;
    }

    public static Event fight(int index) {
	    return monsterMap.get(index).fight();
    }

    private Event fight() {
	    return new Fight("fight" + getName(), getDescription(),
                new Entity(this), me.getCurrentTile().getEvent());
    }

	public int[] getItemValue(int itemID) {
		return Item.getItemMap().get(itemID).getValue();
	}

	public String getInfo() {
	    return String.format(ENTITY_INFO, getName(), getHP(), getEnergy(),
				level);
    }

	public List<Integer> getInventory() { return inventory; }

	// adds an extra name "Go Back"; only to be used for Inventory Event
	public String[] getInventoryItemNames() {
	    String[] itemNames = new String[inventory.size() + 1];
	    for(int x = 0; x < inventory.size(); x++) {
	        itemNames[x] = Item.getName(inventory.get(x));
        }

        itemNames[itemNames.length - 1] = Event.RETURN;

        return itemNames;
    }

	public int getLevel() { return level; }

	public double getBaseAttack() { return baseAttack; }

	public int getHP() { return stats[HP_INDEX]; }

	public int getEnergy() { return stats[ENERGY_INDEX]; }

	public double getTempModifier() { return tempModifier; }

	public int[] getMoney() { return money; }

	public int getCopper() { return money[COPPER_INDEX]; }

    public int getSilver() { return money[SILVER_INDEX]; }

    public int getGold() { return money[GOLD_INDEX]; }

	public static int getTotalMoney(int[] amount) {
		int totalMoney = 0;
		for(int x = 0; x < MONEY_TYPES; x++) {
		    totalMoney += amount[x] * CONVERSION_RATE[x];
        }
        return totalMoney;
	}

	public static Map<Integer, Entity> getMonsterMap() { return monsterMap; }

	public static Map<Integer, Entity> createMonsters() {
        Gson gson = new Gson();
	    FileReader monsterJSON = null;
        try {
            monsterJSON = new FileReader(MONSTER_LIST_FILE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        JsonObject monsterObj = gson.fromJson(monsterJSON, JsonObject.class);

        monsterMap = new HashMap<>();
        for(Map.Entry<String, JsonElement> element : monsterObj.entrySet()) {
            Entity monster = gson.fromJson(element.getValue(), Entity.class);
            monster.finishGSON(element);
            monsterMap.put(monster.getid(), monster);
        }
        System.err.println("MONSTER " + monsterMap);
        return monsterMap;
    }

    /* acts like a constructor that finishes Entities read from json files.
     * Could be replaced by custom deserializer
     */
    protected void finishGSON(Map.Entry<String, JsonElement> element) {

        // for NPCs
        if (this instanceof Mobile) {

            JsonObject obj = element.getValue().getAsJsonObject();
            JsonElement near = obj.get("near");
            for(Place place : Place.getPlaceList()) {
                if( place.getName().equals(obj.get("locationName").getAsString()) ) {
                    setLocation(place);
                }

                if(near != null && place.getName().equals(near.getAsString())) {
                    int x = place.getX() + obj.get("xDelta").getAsInt();
                    int y = place.getY() + obj.get("yDelta").getAsInt();
                    setPosition(x, y);
                    getLocation().getPlot().addPlottable(this);
                }
            }

            if(obj.get("on") != null) {
                String tile = obj.get("on").getAsString();
                getLocation().getPlot().addPlottable(this, tile);
            }
        } else {
            setPosition(-1, -1);
        }

        setName(element.getKey());
        stats = new int[TOTAL_STAT_COUNT];
        for (int i = 0; i < TOTAL_STAT_COUNT; i++) {
            stats[i] = (int) (level * statMultiplier * BASE_MULTIPLIER);
        }

        if(inventory == null) inventory = new ArrayList<>();
    }
}
