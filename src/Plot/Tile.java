package Plot;

import Events.Default;
import Events.Event;
import Things.Plottable;
import Things.Entities.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/*
 * Can hold multiple names as tiles are stacked on top of each other
 */
public class Tile {

    private static final String INTERACT_ERROR = "ERROR: There is nothing to " +
            "interact with at this location.";
    private static final String LAYER_TILE_ERROR = "ERROR: Tried to layer a " +
            "wrong layer with another.";

    private static final int[] encounterChance = {0, 20, 50};

    // nameList.indexOf(0) will be primary tile
    private List<String> nameList;

    private String description;

    private Plottable plottable;

    private int dangerLevel;

    // to be used in indexOf comparison
    Tile(String name) {
        nameList = new ArrayList<>();
        nameList.add(name);
    }

	/* this constructor makes copies of the original tile so other location
	 depended (things) can be added on later*/
	Tile(Tile tile) {
		dangerLevel = tile.dangerLevel;
		nameList = new ArrayList<>(tile.nameList);
		description = new String(tile.description);
	}

    void addTile(Tile tile) {
	    nameList.addAll(tile.nameList);
	    description += "\n\n" + tile.description;

	    if(tile.dangerLevel == 0) {
	        dangerLevel = 0;
        }

	    if(tile.plottable != null) {
	        System.err.println(LAYER_TILE_ERROR);
        }
    }

	void addPlottable(Plottable plottable) {
		this.plottable = plottable;
	}

	public Event interact(Event parentEvent) {
		if(plottable != null) {
			return plottable.interact(parentEvent);
		} else {
			System.err.println(INTERACT_ERROR);
			return null;
		}
	}

	public boolean findTile(String name) {
	    return nameList.contains(name);
    }

	public void setName(String name){
	    nameList = new ArrayList<>();
	    nameList.add(name);
    }

    public String getName(int index) {
	    if(index >= nameList.size()) return null;
	    return nameList.get(index);
    }

    public String getName() {
		StringBuilder title = new StringBuilder();
		for(String name : nameList) {
			title.append(name).append("\\");
		}
		return title.toString();
	}

	public int getDangerLevel() {
		return dangerLevel;
	}

	public String getDescription() { return description; }
	
	public Event getEvent() {
	    Random rand = new Random();

	    // increasing levels of dangerLevel increase encounter chance
        // might want to make that increase more dangerous encounters instead
        /*if(rand.nextInt(100) + 1 <= encounterChance[dangerLevel]) {
            int index = rand.nextInt(Entity.getMonsterMap().size());
            return Entity.fight(index);
        }*/

        return new Default(this);
	}

	public Plottable getPlottable() { return plottable; }
	
	@Override
	public boolean equals(Object o) {
		if(!(o instanceof Tile)) {
			return false;
		}
		
		Tile obj = (Tile) o;

        return nameList.equals(obj.nameList);
    }
	
	@Override
	public String toString() {
		return nameList + " " + dangerLevel;
	}
}
