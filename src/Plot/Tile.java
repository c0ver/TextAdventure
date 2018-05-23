package Plot;

import Events.Default;
import Events.Event;
import Things.Something;
import Things.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/*
 * Can hold multiple IDs as tiles are stacked on top of each other
 */
public class Tile {

    private static final String INTERACT_ERROR = "ERROR: There is nothing to " +
            "interact with at this location.";
    private static final String LAYER_TILE_ERROR = "ERROR: Tried to layer a " +
            "wrong layer with another.";

    private static final int[] encounterChance = {0, 20, 50};

    private int dangerLevel;

    // IDList.indexOf(0) will be primary tile
    private List<String> IDList;

    private String description;

    private Something thing;

    // to be used in indexOf comparison
    Tile(String ID) {
        IDList = new ArrayList<>();
        IDList.add(ID);
    }

	/* this constructor makes copies of the original tile so other location
	 depended (things) can be added on later*/
	Tile(Tile tile) {
		dangerLevel = tile.dangerLevel;
		IDList = new ArrayList<>(tile.IDList);
		description = new String(tile.description);
	}

	public void setID(String ID){
	    this.IDList = new ArrayList<>();
	    this.IDList.add(ID);
    }

	public int getDangerLevel() {
		return dangerLevel;
	}
	
	public Event getEvent() {
	    /*Random rand = new Random();

	    // increasing levels of dangerLevel increase encounter chance
        // might want to make that increase more dangerous encounters instead
        if(rand.nextInt(100) + 1 <= encounterChance[dangerLevel]) {
            int index = rand.nextInt(Entity.getMonsterList().size());
            return Entity.fight(index);
        }*/

        StringBuilder title = new StringBuilder();
        for(String ID : IDList) {
            title.append(ID).append("\\");
        }

        return new Default(title.toString(), description, null);
	}

	void addTile(Tile tile) {
	    IDList.addAll(tile.IDList);
	    description += "\n\n" + tile.description;

	    if(tile.dangerLevel == 0) {
	        dangerLevel = 0;
        }

	    if(tile.thing != null) {
	        System.err.println(LAYER_TILE_ERROR);
        }
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
		if(!(o instanceof Tile)) {
			return false;
		}
		
		Tile obj = (Tile) o;

        return IDList.equals(obj.IDList);
    }
	
	@Override
	public String toString() {
		return IDList + " " + dangerLevel;
	}
}
