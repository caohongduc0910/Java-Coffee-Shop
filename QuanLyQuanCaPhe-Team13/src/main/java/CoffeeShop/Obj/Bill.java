package CoffeeShop.Obj;

public class Bill {

    private Integer id;
    private Integer user_id;
    private Integer table_id;
    private Integer total_price;
    private Boolean status;
    private String created_at;

    private String user_name;
    private String table_name;

    public Bill() {
    }

    public Bill(Integer id, Integer user_id, Integer table_id, Integer total_price, Boolean status, String created_at, String user_name, String table_name) {
        this.id = id;
        this.user_id = user_id;
        this.table_id = table_id;
        this.total_price = total_price;
        this.status = status;
        this.created_at = created_at;
        this.user_name = user_name;
        this.table_name = table_name;
    }

    public Bill(Integer table_id, Boolean status) {
        this.table_id = table_id;
        this.status = status;
    }
    
    public Bill(String table_name, Boolean status) {
        this.table_name = table_name;
        this.status = status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public Integer getId() {
        return id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public Integer getTable_id() {
        return table_id;
    }

    public Boolean getStatus() {
        return status;
    }

    public Integer getTotal_price() {
        return total_price;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getTable_name() {
        return table_name;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setTable_id(Integer table_id) {
        this.table_id = table_id;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public void setTotal_price(Integer total_price) {
        this.total_price = total_price;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public void setTable_name(String table_name) {
        this.table_name = table_name;
    }
    
}
