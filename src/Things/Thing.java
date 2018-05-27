package Things;

public class Thing {

    private String name, description;
    private int ID;

    protected Thing(String name) {
        this.name = name;
    }

    protected Thing(String name, String description) {
        this(name);
        this.description = description;
    }

    protected Thing(Thing toCopy) {
        name = toCopy.name;
        description = toCopy.description;
    }

    public void setName(String name) { this.name = name; }

    public String getName() { return name; }

    public String getDescription() { return description; }

    public int getID() { return ID; }

    @Override
    public String toString() { return name + " " + ID; }

    @Override
    public boolean equals(Object o) {

        if(o instanceof String) {
            String obj = (String) o;
            return name.equals(obj);
        } else if(o instanceof Thing) {
            Thing obj = (Thing) o;
            return name.equals(obj.getName());
        } else {
            return false;
        }

    }
}
