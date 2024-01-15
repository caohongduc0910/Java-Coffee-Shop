package CoffeeShop.DAO.impl;

import CoffeeShop.DAO.ITableDao;
import CoffeeShop.Obj.Table;
import CoffeeShop.Util.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TableDao implements ITableDao {

    private Connection conn = null;
    private CallableStatement cs = null;
    private ResultSet rs = null;

    public TableDao(DbUtil dbUtil) {
        conn = dbUtil.getConnection();
    }

    @Override
    public int count() {
        int count = 0;
        String sql = "{CALL sp_countTables}";

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
    public ArrayList<Table> getAll(Table table) {
        ArrayList<Table> list = new ArrayList<>();
        String sql = "{CALL sp_getAllTable(?, ?, ?)}";        

        try {
            cs = conn.prepareCall(sql);
            cs.setNull(1, Types.INTEGER);
            cs.setNull(2, Types.INTEGER);
            cs.setNull(3, Types.NVARCHAR);

            if (!Common.isNullOrEmpty(table)) {
                if (!Common.isNullOrEmpty(table.getId())) {
                    cs.setInt(1, table.getId());
                }
                if (!Common.isNullOrEmpty(table.getArea_id())) {
                    cs.setInt(2, table.getArea_id());
                }
                if (!Common.isNullOrEmpty(table.getName())) {
                    cs.setNString(3, table.getName());
                }
            }
            rs = cs.executeQuery();

            while (rs.next()) {
                Table obj = new Table(
                        rs.getInt("id"),
                        rs.getInt("area_id"),
                        rs.getNString("name")
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
    public Map<String, Object> create(Table table) { //tạo, thêm bàn
        Map<String, Object> output = new HashMap<>();
        String sql = "{CALL sp_insertTable(?, ?, ?, ?)}";

        try {
            cs = conn.prepareCall(sql);
            cs.setInt(1, table.getArea_id());
            cs.setNString(2, table.getName());
            cs.registerOutParameter(3, Types.BIT);
            cs.registerOutParameter(4, Types.NVARCHAR);
            cs.execute();
            
            output.put("status", cs.getBoolean(3));
            output.put("message", cs.getNString(4));
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cs = null;
        }

        return output;
    }

    @Override
    public Map<String, Object> update(Table table) {
        Map<String, Object> output = new HashMap<>();
        String sql = "{CALL sp_updateTable(?, ?, ?, ?, ?)}";


        try {
            cs = conn.prepareCall(sql);
            cs.setInt(1, table.getId());
            cs.setInt(2, table.getArea_id());
            cs.setNString(3, table.getName());
            cs.registerOutParameter(4, Types.BIT);
            cs.registerOutParameter(5, Types.NVARCHAR);
            cs.execute();

            output.put("status", cs.getBoolean(4));
            output.put("message", cs.getNString(5));

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cs = null;
        }

        return output;
    }

    @Override
    public Map<String, Object> delete(int id) {
        Map<String, Object> output = new HashMap<>();
        String sql = "{CALL sp_deleteTable(?, ?, ?)}";

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

    @Override
    public Table findByName(String name) {
        Table obj = null;
        String sql = "{CALL sp_getAllTable(?, ?, ?)}";

        try {
            cs = conn.prepareCall(sql);
            cs.setNull(1, Types.INTEGER);
            cs.setNull(2, Types.INTEGER);
            cs.setNull(3, Types.NVARCHAR);

            if (!Common.isNullOrEmpty(name)) {
                cs.setNString(3, name);
            }
            rs = cs.executeQuery();

            while (rs.next()) {
                obj = new Table(
                        rs.getInt("id"),
                        rs.getInt("area_id"),
                        rs.getNString("name")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            rs = null;
            cs = null;
        }

        return obj;
    }
}
