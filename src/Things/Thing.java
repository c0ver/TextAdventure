package Things;

public class Thing {

    private String name, description;
    private int id;

    protected Thing(String name) {
        this.name = name;
    }

    protected Thing(String name, int id, String description) {
        this(name);
        this.id = id;
        this.description = description;
    }

    protected Thing(Thing toCopy) {
        name = toCopy.name;
        description = toCopy.description;
        id = toCopy.id;
    }

    public void setName(String name) { this.name = name; }

    public String getName() { return name; }

    public String getDescription() { return description; }

    public int getid() { return id; }

    @Override
    public String toString() { return name + " " + id; }

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
