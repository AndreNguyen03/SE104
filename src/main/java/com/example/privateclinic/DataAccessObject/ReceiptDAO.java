package com.example.privateclinic.DataAccessObject;

import com.example.privateclinic.Models.ConnectDB;
import com.example.privateclinic.Models.Receipt;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ReceiptDAO {
    ConnectDB connectDB = ConnectDB.getInstance();
    public ReceiptDAO() {
    }

    public boolean addReceiptAndDetailReceipt(int examinationID, Receipt receipt)
    {
        int receipt_id = addReceipt(examinationID,receipt);
        String query = "INSERT INTO cthd (mahd,mathuoc,stt,ngay,sang,trua,chieu,toi,soluong,thanhtien,note)" +
                " VALUES(?,?,?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement statement = connectDB.databaseLink.prepareStatement(query)) {
            statement.setInt(1, receipt_id);
            statement.setInt(2, receipt.getMaThuoc());
            statement.setInt(3, receipt.getSothuTu());
            statement.setInt(4, receipt.getNgay());
            statement.setInt(5, receipt.getSang());
            statement.setInt(6, receipt.getTrua());
            statement.setInt(7, receipt.getChieu());
            statement.setInt(8, receipt.getToi());
            statement.setInt(9, receipt.getSoLuong());
            statement.setDouble(10, receipt.getThanhTien());
            statement.setString(11, receipt.getNote());
            return connectDB.handleData(statement);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public int addReceipt(int examinationID, Receipt receipt) {
        String query = "INSERT INTO hoadon (makb,tienkham,tienthuoc) " +
                " VALUES(?,?,?) RETURNING mahd";
        try (PreparedStatement statement = connectDB.databaseLink.prepareStatement(query)) {
            statement.setInt(1, examinationID);
            statement.setInt(2, receipt.getTienkham());
            statement.setInt(3, receipt.getTienthuoc());
            return connectDB.getId(statement);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }
}
