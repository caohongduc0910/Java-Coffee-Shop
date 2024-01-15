package CoffeeShop.Obj;

public class Area {

    private int id;
    private String name;

    public Area(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Area() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
