package CoffeeShop.Obj;

public class Table {

    private Integer id;
    private Integer area_id;
    private String name;

    public Table(Integer id, Integer area_id, String name) {
        this.id = id;
        this.area_id = area_id;
        this.name = name;
    }

    public Table(Integer area_id) {
        this.area_id = area_id;
    }

    public Table() {
    }

    public Integer getId() {
        return id;
    }

    public Integer getArea_id() {
        return area_id;
    }

    public String getName() {
        return name;
    }  

    public void setId(Integer id) {
        this.id = id;
    }

    public void setArea_id(Integer area_id) {
        this.area_id = area_id;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    @Override
    public String toString() {
        return this.name;
    }
}
