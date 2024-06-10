package com.example.privateclinic.DataAccessObject;

import com.example.privateclinic.Models.ConnectDB;
import com.example.privateclinic.Models.Receipt;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ReceiptDAO {
    ConnectDB connectDB = ConnectDB.getInstance();
    public ReceiptDAO() {
    }

    public boolean AddDetailReceipt(int receipt_id, Receipt prescribe)
    {
        String query = "INSERT INTO cthd (mahd,mathuoc,stt,ngay,sang,trua,chieu,toi,soluong,thanhtien,note)" +
                " VALUES(?,?,?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement statement = connectDB.databaseLink.prepareStatement(query)) {
            statement.setInt(1, receipt_id);
            statement.setInt(2, prescribe.getMaThuoc());
            statement.setInt(3, prescribe.getSothuTu());
            statement.setInt(4, prescribe.getNgay());
            statement.setInt(5, prescribe.getSang());
            statement.setInt(6, prescribe.getTrua());
            statement.setInt(7, prescribe.getChieu());
            statement.setInt(8, prescribe.getToi());
            statement.setInt(9, prescribe.getSoLuong());
            statement.setDouble(10, prescribe.getThanhTien());
            statement.setString(11, prescribe.getNote());
            return connectDB.handleData(statement);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public int addReceipt(int examinationID, int tienkham, int tienthuoc) {
        String query = "INSERT INTO hoadon (makb,tienkham,tienthuoc,tongtien) " +
                " VALUES(?,?,?,?) RETURNING mahd";
        try (PreparedStatement statement = connectDB.databaseLink.prepareStatement(query)) {
            statement.setInt(1, examinationID);
            statement.setInt(2, tienkham);
            statement.setInt(3,tienthuoc);
            statement.setInt(4,tienkham + tienthuoc );
            return connectDB.getId(statement);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }
}
