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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MedicineDAO {
    ConnectDB connectDB = new ConnectDB();

    public ObservableList<Medicine> searchMedicineByIDorName(String idOrName) {
        ObservableList<Medicine> medicines = FXCollections.observableArrayList();
        String query = "SELECT * FROM thuoc t, donvitinh dvt, dangthuoc dt, cachdung cd " +
                "WHERE dvt.madvt = t.madvt and dt.madt=t.madt and cd.macd=t.macd and (t.mathuoc::text ILIKE '%" + idOrName + "%' or t.tenthuoc ILIKE '%" + idOrName + "%')";
        ConnectDB connectDB = new ConnectDB();
        try (ResultSet resultSet = connectDB.getData(query)) {
            while (resultSet.next()) {
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
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return medicines;
    }

    public ResultSet getMedicine(String maThuoc) {
        String query = "SELECT * FROM THUOC WHERE mathuoc = "+maThuoc+"";
        ConnectDB connectDB = new ConnectDB();
        return connectDB.getData(query);

    }

    public void addMedicine(Medicine medicine) {
        LocalDate now = LocalDate.now();

        String query = "INSERT INTO thuoc (mathuoc, tenthuoc, madvt, spluong, giaban, madt, macd) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connectDB.databaseLink.prepareStatement(query)) {

            statement.setInt(1, medicine.getMaThuoc());
            statement.setString(2, medicine.getTenThuoc());
            statement.setInt(3, medicine.getMaDonViTinh());
            statement.setInt(4, medicine.getSoLuong());
            statement.setDouble(5, medicine.getGiaBan());
            statement.setInt(6, medicine.getMaDangThuoc());
            statement.setInt(7, medicine.getMaCachDung());


            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Medicine> getAllMedicines() {
        List<Medicine> medicines = new ArrayList<>();
        String query = "SELECT * FROM thuoc";

        try (PreparedStatement statement = connectDB.databaseLink.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Medicine medicine = new Medicine();
                medicine.setMaThuoc(resultSet.getInt("mathuoc"));
                medicine.setTenThuoc(resultSet.getString("tenthuoc"));
                medicine.setMaDonViTinh(resultSet.getInt("madvt"));
                medicine.setSoLuong(resultSet.getInt("soluong"));
                medicine.setGiaBan(resultSet.getDouble("giaban"));
                medicine.setMaDangThuoc(resultSet.getInt("madt"));
                medicine.setMaCachDung(resultSet.getInt("macd"));

                medicines.add(medicine);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return medicines;
    }

    public void updateMedicine(Medicine medicine) {
        String query = "UPDATE thuoc SET tenthuoc = ?, madvt = ?, soluong = ?, giaban = ?, madt = ?, macd = ? WHERE mathuoc = ?";

        try (PreparedStatement statement = connectDB.databaseLink.prepareStatement(query)) {

            statement.setString(1, medicine.getTenThuoc());
            statement.setInt(2, medicine.getMaDonViTinh());
            statement.setInt(3, medicine.getSoLuong());
            statement.setDouble(4, medicine.getGiaBan());
            statement.setInt(5, medicine.getMaDangThuoc());
            statement.setInt(6, medicine.getMaCachDung());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteMedicine(int medicineId) {
        String query = "DELETE FROM thuoc WHERE mathuoc = ?";

        try (PreparedStatement statement = connectDB.databaseLink.prepareStatement(query)) {

            statement.setInt(1, medicineId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
