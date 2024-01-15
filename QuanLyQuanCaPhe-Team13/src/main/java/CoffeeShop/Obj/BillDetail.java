package CoffeeShop.Obj;

public class BillDetail {

    private Integer bill_id;
    private Integer product_id;
    private Integer amount;
    
    // Lấy thông tin từ id
    private String product_name;
    private Integer product_price;

    public BillDetail() {
    }

    public BillDetail(Integer bill_id, Integer product_id, Integer amount, String product_name, Integer product_price) {
        this.bill_id = bill_id;
        this.product_id = product_id;
        this.amount = amount;
        this.product_name = product_name;
        this.product_price = product_price;
    }

    public Integer getBill_id() {
        return bill_id;
    }

    public Integer getProduct_id() {
        return product_id;
    }

    public Integer getAmount() {
        return amount;
    }

    public String getProduct_name() {
        return product_name;
    }

    public Integer getProduct_price() {
        return product_price;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public void setBill_id(Integer bill_id) {
        this.bill_id = bill_id;
    }

    public void setProduct_id(Integer product_id) {
        this.product_id = product_id;
    }

    
}
