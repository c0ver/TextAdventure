package Things;

public class Thing {

    private String name, description;

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

    @Override
    public String toString() { return getName(); }

    @Override
    public boolean equals(Object o) {

        if(o instanceof String) {
            String obj = (String) o;
            return name.equals(obj);
        } else if(o instanceof Thing) {
            Thing obj = (Thing) o;
            return name.equals(obj.toString());
        } else {
            return false;
        }

    }
}
