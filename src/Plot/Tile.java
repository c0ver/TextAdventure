package Plot;

import Events.Default;
import Events.Event;
import Things.Plottable;
import Things.Entities.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/*
 * Can hold multiple ids as tiles are stacked on top of each other
 */
public class Tile {

    private static final String INTERACT_ERROR = "ERROR: There is nothing to " +
            "interact with at this location.";
    private static final String LAYER_TILE_ERROR = "ERROR: Tried to layer a " +
            "wrong layer with another.";

    private static final int[] encounterChance = {0, 20, 50};

    // idList.indexOf(0) will be primary tile
    private List<String> idList;

    private String description;

    private Plottable plottable;

    private int dangerLevel;

    // to be used in indexOf comparison
    Tile(String id) {
        idList = new ArrayList<>();
        idList.add(id);
    }

	/* this constructor makes copies of the original tile so other location
	 depended (things) can be added on later*/
	Tile(Tile tile) {
		dangerLevel = tile.dangerLevel;
		idList = new ArrayList<>(tile.idList);
		description = new String(tile.description);
	}

    void addTile(Tile tile) {
	    idList.addAll(tile.idList);
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

	public boolean findTile(String id) {
	    return idList.contains(id);
    }

	public void setid(String id){
	    idList = new ArrayList<>();
	    idList.add(id);
    }

    public String getid(int index) {
	    return idList.get(index);
    }

    public String getid() {
		StringBuilder title = new StringBuilder();
		for(String id : idList) {
			title.append(id).append("\\");
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

        return idList.equals(obj.idList);
    }
	
	@Override
	public String toString() {
		return idList + " " + dangerLevel;
	}
}
