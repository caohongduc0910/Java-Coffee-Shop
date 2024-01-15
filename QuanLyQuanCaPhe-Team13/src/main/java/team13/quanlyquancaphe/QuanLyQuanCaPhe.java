package team13.quanlyquancaphe;

import CoffeeShop.GUI.home.Dashboard;
import CoffeeShop.Util.Common;
import CoffeeShop.Util.DbUtil;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;

public class QuanLyQuanCaPhe {

    public static void main(String[] args) {
        try {
            DbUtil dbUtil = new DbUtil();
            Connection conn = dbUtil.getConnection();

            if (Common.isNullOrEmpty(conn)) {
                JOptionPane.showMessageDialog(null, "Kết nối CSDL không thành công.", "Có lỗi xảy ra", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            } else {                
                Dashboard dashboard = new Dashboard(dbUtil);
                dashboard.setVisible(true);
            }
        } catch (HeadlessException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }
}
