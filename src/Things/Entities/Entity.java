package Things.Entities;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
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
            "%s\nHP: %d\nEnergy: %d\nLevel: %d\nMoney: %d\n";

	private static final int START_MONEY = 10;

	private static final int TOTAL_STAT_COUNT = 2;
	private static final int HP_INDEX = 0;
    private static final int ENERGY_INDEX = 1;

    private static final int BASE_MULTIPLIER = 10;

    private static final int startPosition = 28;

    private static List<Entity> monsterList;

	/* HP, Energy */
	private int[] stats;

	private int level, money;
	private double statMultiplier, tempModifier;
	private double baseAttack;
	private List<String> inventory;

	/* creates the player */
	public Entity(String name, Place location) {
        super(name, null, location, startPosition, startPosition);
        level = 1;
        money = START_MONEY;
        statMultiplier = 1;
        tempModifier = 1;

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

	public void gainMoney(int amount) {
		money += amount;
	}

	public boolean loseMoney(int amount) {
		if(money - amount < 0) {
			return false;
		}
		money -= amount;
		return true;
	}

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

	public void removeItem(String itemName) {
		inventory.remove(itemName);
	}

    public void removeItem(String itemName, int count) {
        for(int x = 0; x < count; x++) {
            inventory.remove(itemName);
        }
    }

	public void addItem(String itemName) {
		inventory.add(itemName);
	}

	public void addItem(String itemName, int count) {
	    for(int x = 0; x < count; x++) {
	        inventory.add(itemName);
        }
    }

	public void loot(Entity toLoot) {
	    inventory.addAll(toLoot.inventory);
        toLoot.inventory.clear();
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
	    return monsterList.get(index).fight();
    }

    private Event fight() {
	    return new Fight("fight" + getName(), getDescription(),
                new Entity(this), me.getCurrentTile().getEvent());
    }

	public int getItemValue(String itemName) {
		int index = Item.itemList.indexOf(new Item(itemName));
		
		if(index == -1) {
			System.err.printf(ITEM_MISSING_ERROR, itemName);
		}
		
		return Item.itemList.get(index).getValue();
	}

	public String getInfo() {
	    return String.format(ENTITY_INFO, getName(), getHP(), getEnergy(),
				level, money);
    }

	public List<String> getInventory() { return inventory; }
	
	public int getMoney() {
		return money;
	}

	public int getLevel() { return level; }

	public double getBaseAttack() { return baseAttack; }

	public int getHP() { return stats[HP_INDEX]; }

	public int getEnergy() { return stats[ENERGY_INDEX]; }

	public double getTempModifier() { return tempModifier; }

    public static List<Entity> getMonsterList() { return monsterList; }

	public static void createMonsters() {
        Gson gson = new Gson();
	    FileReader monsterJSON;
        try {
            monsterJSON = new FileReader(MONSTER_LIST_FILE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }

        JsonObject monsterObj = gson.fromJson(monsterJSON, JsonObject.class);

        monsterList = new ArrayList<>();
        for(Map.Entry<String, JsonElement> element : monsterObj.entrySet()) {
            Entity monster = gson.fromJson(element.getValue(), Entity.class);
            monster.finishGSON(element);
            monsterList.add(monster);
        }
    }



    /* acts like a constructor that finishes Entities read from json files.
     * Could be replaced by custom deserializer
     */
    protected void finishGSON(Map.Entry<String, JsonElement> element) {

        // for NPCs
        if (this instanceof Mobile) {

            JsonObject obj = element.getValue().getAsJsonObject();
            for(Place place : Place.getPlaceList()) {
                if( place.getName().equals(obj.get("locationName").getAsString()) ) {
                    setLocation(place);
                }

                if( place.getName().equals(obj.get("near").getAsString()) ) {
                    int x = place.getX() + obj.get("xDelta").getAsInt();
                    int y = place.getY() + obj.get("yDelta").getAsInt();
                    setPosition(x, y);
                }
            }

            getLocation().getPlot().addPlottable(this);
        } else {
            setPosition(-1, -1);
        }

        setName(element.getKey());
        stats = new int[TOTAL_STAT_COUNT];
        for (int i = 0; i < TOTAL_STAT_COUNT; i++) {
            stats[i] = (int) (level * statMultiplier * BASE_MULTIPLIER);
        }
    }



}
