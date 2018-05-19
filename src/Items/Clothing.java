package Items;

import Things.Item;

public class Clothing extends Item {

    private int resistance;

    public Clothing(String name, String description, Rarity rarity, Type
            type, int value, int resistance) {
        super(name, description, rarity, type, value);
        this.resistance = resistance;
    }
}
