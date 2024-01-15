package CoffeeShop.DAO.impl;

import CoffeeShop.DAO.IProductDao;
import CoffeeShop.Obj.Product;
import CoffeeShop.Util.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductDao implements IProductDao {

    private Connection conn = null; 
    private CallableStatement cs = null; 
    private ResultSet rs = null; 

    public ProductDao(DbUtil dbUtil) {
        conn = dbUtil.getConnection();
    }

    @Override
    public int count() {
        int count = 0;
        String sql = "{CALL sp_countProducts}";

        try {
            cs = conn.prepareCall(sql);
            rs = cs.executeQuery();

        while(rs.next())
            count = rs.getInt("count");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            rs = null;
            cs = null;
        }

        return count;
    }

    @Override
    public List<Product> getAll(Product product, Integer fromPrice, Integer toPrice) { //danh sách sản phẩm với khoảng giá hoặc thông tin sản phẩm cần tìm kiếm
        List<Product> list = new ArrayList<>();
        String sql = "{CALL sp_getAllProduct(?, ?, ?, ?, ?)}";

        try {
            cs = conn.prepareCall(sql);
            cs.setNull(1, Types.NVARCHAR);
            cs.setNull(2, Types.INTEGER);
            cs.setNull(3, Types.INTEGER);
            cs.setNull(4, Types.INTEGER);
            cs.setNull(5, Types.BOOLEAN);

            if (!Common.isNullOrEmpty(product)) {
                if (!Common.isNullOrEmpty(product.getName())) {
                    cs.setNString(1, product.getName());
                }
                if (!Common.isNullOrEmpty(product.getCategory_id())) {
                    cs.setInt(2, product.getCategory_id());
                }
                if (!Common.isNullOrEmpty(fromPrice)) {
                    cs.setInt(3, fromPrice);
                }
                if (!Common.isNullOrEmpty(toPrice)) {
                    cs.setInt(4, toPrice);
                }
                if (!Common.isNullOrEmpty(product.getStatus())) {
                    cs.setBoolean(5, product.getStatus());
                }
            }
            rs = cs.executeQuery();

            while (rs.next()) {
                Product obj = new Product(
                        rs.getInt("id"),
                        rs.getInt("category_id"),
                        rs.getNString("name"),
                        rs.getInt("price"),
                        rs.getBoolean("status"),
                        rs.getString("category_name")
                );

                list.add(obj);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            rs = null;
            cs = null;
        }

        return list;
    }

    @Override
    public Map<String, Object> create(Product product) { //tạo, thêm sản phẩm
        Map<String, Object> output = new HashMap<>();
        String sql = "{CALL sp_insertProduct(?, ?, ?, ?, ?, ?)}";

        try {
            cs = conn.prepareCall(sql);
            cs.setInt(1, product.getCategory_id());
            cs.setNString(2, product.getName());
            cs.setInt(3, product.getPrice());
            cs.setBoolean(4, product.getStatus());
            cs.registerOutParameter(5, Types.BIT);
            cs.registerOutParameter(6, Types.NVARCHAR);
            cs.execute();

            output.put("status", cs.getBoolean(5));
            output.put("message", cs.getNString(6));

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cs = null;
        }

        return output;
    }

    @Override
    public Map<String, Object> update(Product product) { //sửa thông tin sản phẩm
        Map<String, Object> output = new HashMap<>();
        String sql = "{CALL sp_updateProduct(?, ?, ?, ?, ?, ?, ?)}";

        try {
            cs = conn.prepareCall(sql);
            cs.setInt(1, product.getId());
            cs.setInt(2, product.getCategory_id());
            cs.setNString(3, product.getName());
            cs.setInt(4, product.getPrice());
            cs.setBoolean(5, product.getStatus());
            cs.registerOutParameter(6, Types.BIT);
            cs.registerOutParameter(7, Types.NVARCHAR);
            cs.execute();

            output.put("status", cs.getBoolean(6));
            output.put("message", cs.getNString(7));

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cs = null;
        }

        return output;
    }

    @Override
    public Map<String, Object> delete(int id) { //xoá sản phẩm
        Map<String, Object> output = new HashMap<>();
        String sql = "{CALL sp_deleteProduct(?, ?, ?)}";

        try {
            cs = conn.prepareCall(sql);
            cs.setInt(1, id);
            cs.registerOutParameter(2, Types.BIT);
            cs.registerOutParameter(3, Types.NVARCHAR);
            cs.execute();

            output.put("status", cs.getBoolean(2));
            output.put("message", cs.getNString(3));

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cs = null;
        }

        return output;
    }
}
