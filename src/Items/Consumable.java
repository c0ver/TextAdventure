package Items;

import Things.Item;

public class Consumable extends Item {

    private boolean beneficial;
    private int power;

    public Consumable(String name, String description, Rarity rarity, Type
            type, int value, int power, boolean beneficial) {
        super(name, description, rarity, type, value);
        this.power = power;
        this.beneficial = beneficial;
    }
}
