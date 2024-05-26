package com.example.privateclinic.DataAccessObject;

import com.example.privateclinic.Models.ConnectDB;
import com.example.privateclinic.Models.Customer;
import com.example.privateclinic.Models.Medicine;
import com.example.privateclinic.Models.Model;
import com.jfoenix.controls.JFXDialog;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MedicineDAO {
    public ObservableList<Medicine> searchMedicineByIDorName(String idOrName)
    {
        ObservableList<Medicine> medicines = FXCollections.observableArrayList();
        String query = "SELECT * FROM thuoc t, donvitinh dvt, dangthuoc dt, cachdung cd " +
                "WHERE dvt.madvt = t.madvt and dt.madt=t.madt and cd.macd=t.macd and (t.mathuoc::text ILIKE '%" + idOrName + "%' or t.tenthuoc ILIKE '%" + idOrName + "%')";
        ConnectDB connectDB = new ConnectDB();
            try (ResultSet resultSet = connectDB.getData(query))
            {
                while(resultSet.next())
                {
                    Medicine medicine = new Medicine();
                    medicine.setMaThuoc(resultSet.getInt("mathuoc"));
                    medicine.setTenThuoc(resultSet.getString("tenthuoc"));
                    medicine.setMaCachDung(resultSet.getInt("macd"));
                    medicine.setMaDangThuoc(resultSet.getInt("madt"));
                    medicine.setMaDonViTinh(resultSet.getInt("madvt"));
                    medicine.setTenDangThuoc(resultSet.getString("tendt"));
                    medicine.setTenCachDung(resultSet.getString("tencd"));
                    medicine.setTenDonViTinh(resultSet.getString("tendvt"));
                    medicine.setSoLuong(resultSet.getInt("soluong"));
                    medicine.setGiaBan(resultSet.getInt("giaban"));
                    medicines.add(medicine);
                }
            }
            catch (SQLException e)
            {
                System.out.println(e.getMessage());
            }
        return medicines;
    }

    public ResultSet getMedicine(String maThuoc)
    {
        String query = "SELECT * FROM THUOC WHERE mathuoc = "+maThuoc+"";
        ConnectDB connectDB = new ConnectDB();
        return connectDB.getData(query);
    }
}
