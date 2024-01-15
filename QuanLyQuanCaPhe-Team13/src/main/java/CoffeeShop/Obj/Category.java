package CoffeeShop.Obj;

public class Category {

    private Integer id;
    private String name;
    private Boolean status;

    public Category() {
    }

    public Category(Integer id, String name, Boolean status) {
        this.id = id;
        this.name = name;
        this.status = status;
    }
    
    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public void setId(Integer id) {
        this.id = id;
    }    
    
    @Override
    public String toString() {
        return this.name;
    }
}
