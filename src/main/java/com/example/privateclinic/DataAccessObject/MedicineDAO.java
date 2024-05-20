package com.example.privateclinic.DataAccessObject;

import com.example.privateclinic.Models.ConnectDB;
import com.example.privateclinic.Models.Medicine;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MedicineDAO {
    ConnectDB connectDB = new ConnectDB();

    public MedicineDAO() {

    }

    public void addMedicine(Medicine medicine) {
        LocalDate now = LocalDate.now();

        String query = "INSERT INTO thuoc (mathuoc, tenthuoc, madvt, spluong, giaban, madt, macd) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connectDB.databaseLink.prepareStatement(query)) {

            statement.setInt(1, medicine.getMedicineId());
            statement.setString(2, medicine.getMedicineName());
            statement.setInt(3, medicine.getMedicineUnit());
            statement.setInt(4, medicine.getMedicineQuantity());
            statement.setDouble(5, medicine.getMedicinePrice());
            statement.setInt(6, medicine.getMedicineForm());
            statement.setInt(7, medicine.getMedicineUse());


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
                medicine.setMedicineId(resultSet.getInt("mathuoc"));
                medicine.setMedicineName(resultSet.getString("tenthuoc"));
                medicine.setMedicineUnit(resultSet.getInt("madvt"));
                medicine.setMedicineQuantity(resultSet.getInt("soluong"));
                medicine.setMedicinePrice(resultSet.getDouble("giaban"));
                medicine.setMedicineForm(resultSet.getInt("madt"));
                medicine.setMedicineUse(resultSet.getInt("macd"));

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

            statement.setString(1, medicine.getMedicineName());
            statement.setInt(2, medicine.getMedicineUnit());
            statement.setInt(3, medicine.getMedicineQuantity());
            statement.setDouble(4, medicine.getMedicinePrice());
            statement.setInt(5, medicine.getMedicineForm());
            statement.setInt(6, medicine.getMedicineUse());

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
