package CoffeeShop.DAO;

import CoffeeShop.Obj.BillDetail;

import java.util.List;
import java.util.Map;

public interface IBillDetailDao {

    public List<BillDetail> getAll(int bill_id);

    public Map<String, Object> create(BillDetail billDetail);

    public Map<String, Object> update(BillDetail billDetail);

    public Map<String, Object> delete(BillDetail billDetail);
}
