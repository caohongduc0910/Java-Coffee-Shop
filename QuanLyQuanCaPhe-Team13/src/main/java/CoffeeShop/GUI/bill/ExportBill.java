package CoffeeShop.GUI.bill;

import CoffeeShop.DAO.impl.BillDao;
import CoffeeShop.DAO.impl.BillDetailDao;
import CoffeeShop.Obj.Bill;
import CoffeeShop.Obj.BillDetail;
import CoffeeShop.Util.Common;
import CoffeeShop.Util.DbUtil;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public final class ExportBill {
    private DbUtil dbUtil;

    private Bill bill = null;

    private List<BillDetail> billDetails = null;

    private BillDao billDao = null;
    private BillDetailDao billDetailDao = null;
    
    private static int columnWidthName = 25;
    private static int columnWidthNumber = 15;

    public ExportBill(Frame parent, boolean modal, DbUtil dbUtil, Bill bill) throws ParseException {
        this.dbUtil = dbUtil;
        this.bill = bill;
        this.billDao = new BillDao(dbUtil);
        this.billDetailDao = new BillDetailDao(dbUtil);

        billDetails = billDetailDao.getAll(bill.getId());
        
        String fileName = createFileName();
        JFileChooser fileChooser = new JFileChooser(".");
        fileChooser.setDialogTitle("Chọn vị trí xuất file");        
        fileChooser.setSelectedFile(new File(fileName));
        
        fileChooser.setFileFilter(new FileNameExtensionFilter("Text files (*.txt)", "txt"));

        int userSelection = fileChooser.showSaveDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) { 
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileChooser.getSelectedFile().getPath() + ".txt", StandardCharsets.UTF_8))) {
                if (!Common.isNullOrEmpty(billDetails)) {
                    writer.write("*************************Hoá đơn thanh toán*************************\n");
                    writer.write("Mã hoá đơn: " + bill.getId() + "\n");
                    writer.write("Giờ đặt bàn: " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse(bill.getCreated_at())) + "\n");
                    writer.write("Nhân viên: " + bill.getUser_name() + "\n");
                    writer.write("********************************************************************\n");
                    String header[] = {"Tên hàng", "Đơn giá", "Số lượng", "Thành tiền"};
                    writer.write(printRow(header));
                    
                    billDetails.forEach(obj -> {
                        int total = (int) (obj.getProduct_price() * obj.getAmount());
                        try {
                            String data[] = {obj.getProduct_name(), NumberFormat.getInstance().format(obj.getProduct_price())
                                    , obj.getAmount().toString(), NumberFormat.getInstance().format(total)};
                            writer.write(printRow(data));
                        } catch (IOException e) {
                            JOptionPane.showMessageDialog(parent, "Xuất hoá đơn thất bại");
                            e.printStackTrace();                                    
                        }
                    });
                    writer.write("********************************************************************\n");
                    String footer[] = {"Tổng thành tiền:", "", "", NumberFormat.getInstance().format(bill.getTotal_price())};
                    writer.write(printRow(footer));      
                    writer.write("**************************Cảm ơn quý khách**************************");  
                }                
                writer.close();
                JOptionPane.showMessageDialog(parent, "Xuất hoá đơn thành công");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(parent, "Xuất hoá đơn thất bại");
                e.printStackTrace();
            }
        }
    }
    
    private String createFileName() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String dateTimeInfo = dateFormat.format(new Date());
        return "Bill_" + String.format("%s", dateTimeInfo);
    }
    
    private static String printRow(String[] rowData) {
        String write = "";
        int padding;
        for (int i = 0; i < 4; i++) {
            if (i == 0) {
                padding = (columnWidthName - rowData[i].length()) / 2;
                write += padString(" ", padding) + rowData[i] + padString(" ", padding + (columnWidthName - rowData[i].length()) % 2);
            }
            else {
                padding = (columnWidthNumber - rowData[i].length()) / 2;
                write += padString(" ", padding) + rowData[i] + padString(" ", padding + (columnWidthNumber - rowData[i].length()) % 2);
            }
        }
        return write + "\n";
    }

    private static String padString(String str, int length) {
        StringBuilder paddedString = new StringBuilder();
        for (int i = 0; i < length; i++)
            paddedString.append(str);
        
        return paddedString.toString();
    }
}