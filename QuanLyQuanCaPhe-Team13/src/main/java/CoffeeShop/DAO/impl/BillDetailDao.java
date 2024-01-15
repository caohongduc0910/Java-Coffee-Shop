package CoffeeShop.DAO.impl;

import CoffeeShop.DAO.IBillDetailDao;
import CoffeeShop.Obj.BillDetail;
import CoffeeShop.Util.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BillDetailDao implements IBillDetailDao {

    private Connection conn = null;
    private CallableStatement cs = null;
    private ResultSet rs = null;

    public BillDetailDao(DbUtil dbUtil) {
        conn = dbUtil.getConnection();
    }

    @Override
    public ArrayList<BillDetail> getAll(int bill_id) { //Lấy dữ liệu chi tiết của 1 bill qua bill_id
        ArrayList<BillDetail> list = new ArrayList<>();
        String sql = "{CALL sp_getBillDetailByBillId(?)}";

        try {
            cs = conn.prepareCall(sql);
            cs.setInt(1, bill_id);
            rs = cs.executeQuery();

            while (rs.next()) {
                BillDetail billDetail = new BillDetail(
                        rs.getInt("bill_id"),
                        rs.getInt("product_id"),
                        rs.getInt("amount"),
                        rs.getNString("product_name"),
                        rs.getInt("product_price")
                );

                list.add(billDetail);
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
    public Map<String, Object> create(BillDetail billDetail) { //Thêm sản phẩm vào hoá đơn (trong db là bill_id - product_id - amount)
        Map<String, Object> output = new HashMap<>();
        String sql = "{CALL sp_insertBillDetail(?, ?, ?, ?, ?)}";

        try {
            cs = conn.prepareCall(sql);
            cs.setInt(1, billDetail.getBill_id());
            cs.setInt(2, billDetail.getProduct_id());
            cs.setInt(3, billDetail.getAmount());
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
    public Map<String, Object> update(BillDetail billDetail) { //sửa số lượng sản phẩm khi đặt
        Map<String, Object> output = new HashMap<>();
        String sql = "{CALL sp_updateBillDetail(?, ?, ?, ?, ?)}";

        try {
            cs = conn.prepareCall(sql);
            cs.setInt(1, billDetail.getBill_id());
            cs.setInt(2, billDetail.getProduct_id());
            cs.setInt(3, billDetail.getAmount());
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
    public Map<String, Object> delete(BillDetail billDetail) { //xoá sản phẩm trong hoá đơn khi đặt

        Map<String, Object> output = new HashMap<>();
        String sql = "{CALL sp_deleteBillDetail(?, ?, ?, ?)}";

        try {
            cs = conn.prepareCall(sql);
            cs.setInt(1, billDetail.getBill_id());
            cs.setInt(2, billDetail.getProduct_id());
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
}
