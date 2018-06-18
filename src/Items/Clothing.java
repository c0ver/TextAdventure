package Items;

import Things.Item;

public class Clothing extends Item {

    private int resistance;

    public Clothing(String name, int id, String description, Rarity rarity, Type
            type, int[] value, int resistance) {
        super(name, id, description, rarity, type, value);
        this.resistance = resistance;
    }
}
