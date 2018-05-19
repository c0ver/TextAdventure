package Items;

import Things.Item;

public class Tool extends Item {

    private int strength;

    public Tool(String name, String description, Rarity rarity, Type type, int
            value, int strength) {
        super(name, description, rarity, type, value);
        this.strength = strength;
    }
}
