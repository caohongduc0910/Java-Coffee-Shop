package CoffeeShop.DAO;

import CoffeeShop.Obj.Bill;

import java.util.List;
import java.util.Map;

public interface IBillDao {

    public int count();

    public List<Bill> getAll(Bill bill, String dateStart, String dateEnd);

    public Map<String, Object> create(Bill bill);

    public Map<String, Object> update(Bill bill);

    public Map<String, Object> delete(int id);

    public Bill getByTableId(Bill bill);
}
