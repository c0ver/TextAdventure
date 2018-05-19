package Plot;

import Events.Default;
import main.Event;
import main.Something;
import Things.Entity;

import java.util.Random;

public class Square {
	
	private static final String INTERACT_ERROR = "There is nothing to " +
            "interact with at this location.";

	private static final int[] encounterChance = {0, 20, 50};
	
	private int dangerLevel;
	
	// ex: SAFE, GRASS, DEEP_GRASS
	private String name, description;
	
	private Something thing;

	/* CANNOT HAVE EVENT CLASS OBJECT IN THIS CLASS DUE TO GSON */

	/* used for comparison */
    Square(String name) {
		this.name = name;
	}
	
	/* this constructor makes copies of the original square so other location
	 depended (things) can be added on later*/
	Square(Square square) {
		dangerLevel = square.dangerLevel;
		name = square.name;
		description = square.description;
	}

	public void setName(String name) { this.name = name; }
	
	public int getDangerLevel() {
		return dangerLevel;
	}
	
	public Event getEvent() {
	    Random rand = new Random();
        if(rand.nextInt(100) + 1 <= encounterChance[dangerLevel]) {
            int index = rand.nextInt(Entity.getMonsterList().size());
            return Entity.fight(index);
        }

        return new Default(name, description, null);
	}
	
	void addThing(Something thing) {
		this.thing = thing;
	}
	
	public Event interact(Event parentEvent) {
		if(thing != null) {
			return thing.getEvent(parentEvent);
		} else {
			System.err.println(INTERACT_ERROR);
			return null;
		}
	}
	
	@Override
	public boolean equals(Object o) {
		if(!(o instanceof Square)) {
			return false;
		}
		
		Square obj = (Square) o;

        return name.equals(obj.name);
    }
	
	@Override
	public String toString() {
		return name + " " + dangerLevel;
	}
}
