package CoffeeShop.Obj;

public class User {

    private Integer id;
    private String name;
    private String email;
    private String password;
    private Integer role;

    public User() {
    }

    public User(Integer id, String name, String email, String password, Integer role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public User(String name, String email, String password, Integer role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }
    
    public Integer getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Integer getRole() {
        return role;
    }

    public void setId(Integer id) { 
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(Integer role) {
        this.role = role;
    }
    
}
