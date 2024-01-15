package CoffeeShop.DAO.impl;

import CoffeeShop.DAO.IBillDao;
import CoffeeShop.Obj.Bill;
import CoffeeShop.Util.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BillDao implements IBillDao {

    private Connection conn = null;
    private CallableStatement cs = null;
    private ResultSet rs = null;

    public BillDao(DbUtil dbUtil) {
        conn = dbUtil.getConnection();
    }

    @Override
    public int count() {
        int count = 0;
        String sql = "{CALL sp_countBills}";

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
    public List<Bill> getAll(Bill bill, String dateStart, String dateEnd) {
        List<Bill> list = new ArrayList<>();
        String sql = "{CALL sp_getAllBill(?, ?, ?, ?, ?, ?, ?)}";

        try {
            cs = conn.prepareCall(sql);
            cs.setNull(1, Types.INTEGER);
            cs.setNull(2, Types.INTEGER);
            cs.setNull(3, Types.INTEGER);
            cs.setNull(4, Types.BOOLEAN);
            cs.setNull(5, Types.NVARCHAR);
            cs.setNull(6, Types.VARCHAR);
            cs.setNull(7, Types.VARCHAR);

            if (!Common.isNullOrEmpty(bill)) {
                if (!Common.isNullOrEmpty(bill.getId())) {
                    cs.setInt(1, bill.getId());
                }
                if (!Common.isNullOrEmpty(bill.getUser_id())) {
                    cs.setInt(2, bill.getUser_id());
                }
                if (!Common.isNullOrEmpty(bill.getTable_id())) {
                    cs.setInt(3, bill.getTable_id());
                }
                if (!Common.isNullOrEmpty(bill.getStatus())) {
                    cs.setBoolean(4, bill.getStatus());
                }
                if (!Common.isNullOrEmpty(bill.getTable_name())) {
                    cs.setNString(5, bill.getTable_name());
                }
                if (!Common.isNullOrEmpty(dateStart)) {
                    cs.setNString(6, dateStart);
                }
                if (!Common.isNullOrEmpty(dateEnd)) {
                    cs.setNString(7, dateEnd);
                }
            }
            rs = cs.executeQuery();

            while (rs.next()) {
                Bill obj = new Bill(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getInt("table_id"),
                        rs.getInt("total_price"),
                        rs.getBoolean("status"),
                        rs.getString("created_at"),
                        //lấy từ cột user_name của bảng User
                        rs.getNString("user_name"),
                        //lấy từ cột table_name của bảng Tables
                        rs.getNString("table_name")                     
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
    public Map<String, Object> create(Bill bill) { //tạo hoá đơn (sau khi ấn đặt bàn)
        Map<String, Object> output = new HashMap<>();
        String sql = "{CALL sp_insertBill(?, ?, ?, ?, ?, ?)}";

        try {
            cs = conn.prepareCall(sql);
            cs.setNull(1, Types.INTEGER);
            cs.setNull(2, Types.INTEGER);
            cs.setNull(3, Types.INTEGER);
            cs.setNull(4, Types.BOOLEAN);

            if (!Common.isNullOrEmpty(bill)) {
                if (!Common.isNullOrEmpty(bill.getUser_id())) {
                    cs.setInt(1, bill.getUser_id());
                }
                if (!Common.isNullOrEmpty(bill.getTable_id())) {
                    cs.setInt(2, bill.getTable_id());
                }
                if (!Common.isNullOrEmpty(bill.getTotal_price())) {
                    cs.setFloat(3, bill.getTotal_price());
                }
                if (!Common.isNullOrEmpty(bill.getStatus())) {
                    cs.setBoolean(4, bill.getStatus());
                }
            }
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
    public Map<String, Object> update(Bill bill) { //cập nhật thông tin bill
        Map<String, Object> output = new HashMap<>();
        String sql = "{CALL sp_updateBill(?, ?, ?, ?, ?, ?, ?)}";

        try {
            cs = conn.prepareCall(sql);
            cs.setInt(1, bill.getId());
            cs.setInt(2, bill.getUser_id());
            cs.setInt(3, bill.getTable_id());
            cs.setInt(4, bill.getTotal_price());
            cs.setBoolean(5, bill.getStatus());
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
    public Map<String, Object> delete(int id) { //xoá hoá đơn đang / đã được tạo
        Map<String, Object> output = new HashMap<>();
        String sql = "{CALL sp_deleteBill(?, ?, ?)}";

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
    public Bill getByTableId(Bill bill) { //lấy thông tin hoá đơn qua id bàn
        Bill obj = null;
        String sql = "{CALL sp_getBillByTableId(?, ?)}";

        try {
            cs = conn.prepareCall(sql);
            cs.setInt(1, bill.getTable_id());

            if (Common.isNullOrEmpty(bill.getStatus())) 
                cs.setNull(2, Types.BOOLEAN);           
            else 
                cs.setBoolean(2, bill.getStatus());            

            rs = cs.executeQuery();
            while (rs.next()) {
                obj = new Bill(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getInt("table_id"),
                        rs.getInt("total_price"),
                        rs.getBoolean("status"),
                        rs.getString("created_at"),
                        rs.getNString("user_name"),
                        rs.getNString("table_name")
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
