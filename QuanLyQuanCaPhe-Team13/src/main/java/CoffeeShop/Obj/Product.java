package CoffeeShop.Obj;

public class Product {

    private Integer id;
    private Integer category_id;
    private String name;
    private Integer price;
    private Boolean status;
    
    private String category_name;

    public Product() {
    }

    public Product(Integer id, Integer category_id, String name, Integer price, Boolean status, String category_name) {
        this.id = id;
        this.category_id = category_id;
        this.name = name;
        this.price = price;
        this.status = status;
        this.category_name = category_name;
    }

    public Integer getId() {
        return id;
    }

    public Integer getPrice() {
        return price;
    }

    public Integer getCategory_id() {
        return category_id;
    }

    public String getName() {
        return name;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setCategory_id(Integer category_id) {
        this.category_id = category_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCategory_name() {
        return category_name;
    }
    
}
