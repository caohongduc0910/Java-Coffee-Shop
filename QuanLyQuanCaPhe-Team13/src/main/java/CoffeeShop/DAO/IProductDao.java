package CoffeeShop.DAO;

import CoffeeShop.Obj.Product;

import java.util.List;
import java.util.Map;

public interface IProductDao {

    public int count();

    public List<Product> getAll(Product product, Integer fromPrice, Integer toPrice);

    public Map<String, Object> create(Product product);

    public Map<String, Object> update(Product product);

    public Map<String, Object> delete(int id);
}
