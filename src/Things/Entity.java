package Things;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import Events.Fight;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import main.Game;
import Plot.Plot;
import Events.Event;

import static main.Game.me;

public class Entity extends Something {
    private static List<Entity> monsterList, persistentNPCList;

    private static final String PERSISTENT_NPC_LIST_FILE =
            "assets/persistentNPCList.json";
    private static final String MONSTER_LIST_FILE =
            "assets/monsterList.json";
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

	/* HP, Energy */
	private int[] stats;

	private int level, money;
	private double statMultiplier, tempModifier;
	private double baseAttack;
	private List<String> inventory;

	/* creates the player */
	public Entity(String name, String description) {
        super(name, description);
        level = 1;
        money = START_MONEY;
        statMultiplier = 1;
        tempModifier = 1;
        xPosition = yPosition = Plot.PLOT_WIDTH / 2;

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

    @Override
    public String toString() {
        return name + ", " + description + ", " + level + ", " + statMultiplier
                + ", " + baseAttack + ", " + inventory.get(0) + ", " +
                stats[0]+ ", " + stats[1];
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
		inventory.remove(new Item(itemName));
	}

	public void addItem(String itemName) {
		inventory.add(itemName);
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
	    return new Fight("fight" + name, description, new Entity(this), me
                .getLocation().getEvent());
    }

	public void goUp() {
		if(yPosition == Game.SQUARES_SHOWN / 2) return;
		yPosition--;
	}
	
	public void goDown() {
		if(yPosition == Plot.PLOT_HEIGHT - 1 - Game.SQUARES_SHOWN / 2) return;
		yPosition++;
	}
	
	public void goLeft() {
		if(xPosition == Game.SQUARES_SHOWN / 2) return;
		xPosition--;
	}
	
	public void goRight() {
		if(xPosition == Plot.PLOT_WIDTH - 1 - Game.SQUARES_SHOWN / 2) return;
		xPosition++;
	}

	public void setName(String name) { this.name = name; }

	public int getItemValue(String itemName) {
		int index = Item.itemList.indexOf(new Item(itemName));
		
		if(index == -1) {
			System.err.printf(ITEM_MISSING_ERROR, itemName);
		}
		
		return Item.itemList.get(index).getValue();
	}

	public String getInfo() {
	    return String.format(ENTITY_INFO, name, getHP(), getEnergy(),
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

    public static List<Entity> getPersistentNPCList() { return
            persistentNPCList; }

	public static void createPersistentNPCs() {
	    Gson gson = new Gson();
	    FileReader npcGSON;
        try {
            npcGSON = new FileReader(PERSISTENT_NPC_LIST_FILE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }

        JsonObject npcJSON = gson.fromJson(npcGSON, JsonObject.class);

        persistentNPCList = new ArrayList<>();
        for(Map.Entry<String, JsonElement> element : npcJSON.entrySet()) {
            Entity npc = gson.fromJson(element.getValue(), Entity.class);
            npc.finishInitialization(element.getKey());
            persistentNPCList.add(npc);
        }
	}

	public static void createRandomNPCs() {
        Gson gson = new Gson();
	    FileReader monsterGSON;
        try {
            monsterGSON = new FileReader(MONSTER_LIST_FILE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }

        JsonObject monsterJSON = gson.fromJson(monsterGSON, JsonObject.class);

        monsterList = new ArrayList<>();
        for(Map.Entry<String, JsonElement> element : monsterJSON.entrySet()) {
            Entity monster = gson.fromJson(element.getValue(), Entity.class);
            monster.finishInitialization(element.getKey());
            monsterList.add(monster);
        }
    }

    /* acts like a constructor that finishes Entities read from json files.
     * Could maybe be replaced with InstanceCreators or default constructor?
     */
    private void finishInitialization(String name) {

        /* for persistent NPCs */
        if (xPosition != 0 || yPosition != 0) {
            Plot.addSomething(this);
        }

        this.name = name;
        stats = new int[TOTAL_STAT_COUNT];
        for (int i = 0; i < TOTAL_STAT_COUNT; i++) {
            stats[i] = (int) (level * statMultiplier * BASE_MULTIPLIER);
        }
    }



}
