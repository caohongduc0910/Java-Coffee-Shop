package CoffeeShop.DAO;

import CoffeeShop.Obj.User;

import java.util.List;
import java.util.Map;

public interface IUserDao {

    public int count();

    public List<User> getAll(User user);

    public Map<String, Object> create(User user);

    public Map<String, Object> update(User user);

    public Map<String, Object> delete(int id);

    public User auth(String email, String password);
}
