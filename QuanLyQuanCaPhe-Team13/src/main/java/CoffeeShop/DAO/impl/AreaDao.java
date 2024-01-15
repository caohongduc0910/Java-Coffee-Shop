package CoffeeShop.DAO.impl;

import CoffeeShop.DAO.IAreaDao;
import CoffeeShop.Obj.Area;
import CoffeeShop.Util.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AreaDao implements IAreaDao {

    private Connection conn = null;
    private CallableStatement cs = null;
    private ResultSet rs = null;

    public AreaDao(DbUtil dbUtil) {
        conn = dbUtil.getConnection();
    }

    @Override
    public int count() {
        int count = 0;
        String sql = "{CALL sp_countAreas}";

        try {
            cs = conn.prepareCall(sql);
            rs = cs.executeQuery();
        
        while (rs.next())
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
    public ArrayList<Area> getAll() {
        ArrayList<Area> list = new ArrayList<>();
        String sql = "{CALL sp_getAllArea}";

        try {
            cs = conn.prepareCall(sql);
            rs = cs.executeQuery();

            while (rs.next()) {
                Area obj = new Area(
                        rs.getInt("id"),
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
    public Map<String, Object> create(Area area) { //tạo, thêm khu vực
        Map<String, Object> output = new HashMap<>();
        String sql = "{CALL sp_insertArea(?, ?, ?)}";

        try {
            cs = conn.prepareCall(sql);
            cs.setNString(1, area.getName());
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
    public Area findByName(String name) { //tìm kiếm khu vực theo tên (dùng cho tab area)
        Area obj = null;
        String sql = "{CALL sp_findAreaByName(?)}";

        try {
            cs = conn.prepareCall(sql);
            cs.setNString(1, name);
            rs = cs.executeQuery();

            while (rs.next()) {
                obj = new Area(
                        rs.getInt("id"),
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

    @Override
    public Map<String, Object> update(Area area) { //sửa thông tin khu vực
        Map<String, Object> output = new HashMap<>();
        String sql = "{CALL sp_updateArea(?, ?, ?, ?)}";

        try {
            cs = conn.prepareCall(sql);
            cs.setInt(1, area.getId());
            cs.setNString(2, area.getName());
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
    public Map<String, Object> delete(int id) {
        Map<String, Object> output = new HashMap<>();
        String sql = "{CALL sp_deleteArea(?, ?, ?)}";

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
