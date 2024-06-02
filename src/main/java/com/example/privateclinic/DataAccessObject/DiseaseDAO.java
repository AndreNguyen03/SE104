package com.example.privateclinic.DataAccessObject;

import com.example.privateclinic.Models.ConnectDB;
import com.example.privateclinic.Models.Disease;
import com.example.privateclinic.Models.Medicine;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Pair;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DiseaseDAO {
    ConnectDB connectDB = new ConnectDB();

    public DiseaseDAO() {
    }
    public ObservableList<Disease> searchMedicineByIDorName(String idOrName)
    {
        ObservableList<Disease> diseases = FXCollections.observableArrayList();
        String query = "SELECT * FROM benh WHERE maBenh::text LIKE '"+idOrName+"%' OR tenBenh ILIKE '%"+idOrName+"%'";
        ConnectDB connectDB = new ConnectDB();
        try (ResultSet resultSet = connectDB.getData(query))
        {
            while(resultSet.next())
            {
                Disease disease = new Disease();
                disease.setMaBenh(resultSet.getInt("mabenh"));
                disease.setTenBenh(resultSet.getString("tenbenh"));
                diseases.add(disease);
            }
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
        return diseases;
    }

    public void addDisease(Disease disease) {

        String query = "INSERT INTO benh (mabenh, tenbenh, maicd) VALUES (?, ?, ?)";

        try (PreparedStatement statement = connectDB.databaseLink.prepareStatement(query)) {

            statement.setInt(1, disease.getMaBenh());
            statement.setString(2, disease.getTenBenh());
            statement.setString(3, disease.getMaICD());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addDiseases(List<Disease> diseases) {
        String query = "INSERT INTO benh (mabenh, tenbenh, maicd) VALUES (?, ?, ?)";

        try (PreparedStatement statement = connectDB.databaseLink.prepareStatement(query)) {
            for (Disease disease : diseases) {
                statement.setInt(1, disease.getMaBenh());
                statement.setString(2, disease.getTenBenh());
                statement.setString(3, disease.getMaICD());
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Disease> getAllDiseases() {
        List<Disease> diseases = new ArrayList<>();
        String query = "SELECT * FROM benh ORDER BY mabenh ASC";

        try (PreparedStatement statement = connectDB.databaseLink.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Disease disease = new Disease();
                disease.setMaBenh(resultSet.getInt("mabenh"));
                disease.setMaICD(resultSet.getString("maicd"));
                disease.setTenBenh(resultSet.getString("tenbenh"));

                diseases.add(disease);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return diseases;
    }


    public void updateDisease(Disease disease) {
        String query = "UPDATE benh SET tenbenh = ?, maicd = ? WHERE mabenh = ?";

        try (PreparedStatement statement = connectDB.databaseLink.prepareStatement(query)) {

            statement.setString(1, disease.getTenBenh());
            statement.setString(2, disease.getMaICD());
            statement.setInt(3, disease.getMaBenh());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteDisease(int diseaseID) {
        String query = "DELETE FROM benh WHERE mabenh = ?";

        try (PreparedStatement statement = connectDB.databaseLink.prepareStatement(query)) {

            statement.setInt(1, diseaseID);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getNextDiseaseId() {
        String query = "SELECT MAX(mabenh) FROM benh";

        try (Statement statement = connectDB.databaseLink.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            if (resultSet.next()) {
                int maxId = resultSet.getInt(1);
                return maxId + 1;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi: " + e.getMessage());
        }
        return 1;
    }

    public void updateDiseaseIds() {
        List<Pair<Integer, Integer>> idPairs = new ArrayList<>();

        String selectQuery = "SELECT mabenh FROM benh ORDER BY mabenh";
        try (Statement selectStatement = connectDB.databaseLink.createStatement()) {
            ResultSet resultSet = selectStatement.executeQuery(selectQuery);
            int newId = 1; // Bắt đầu từ ID đầu tiên
            while (resultSet.next()) {
                int oldId = resultSet.getInt("mabenh");
                idPairs.add(new Pair<>(oldId, newId));
                newId++; // Tăng ID mới cho mục tiếp theo
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Thực hiện cập nhật cho mỗi cặp cũ-mới
        String updateQuery = "UPDATE benh SET mabenh = ? WHERE mabenh = ?";
        try (PreparedStatement updateStatement = connectDB.databaseLink.prepareStatement(updateQuery)) {
            for (Pair<Integer, Integer> pair : idPairs) {
                updateStatement.setInt(1, pair.getValue());
                updateStatement.setInt(2, pair.getKey());
                updateStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isDiseaseNameExists(String diseaseName, int currentDiseaseId) {
        String query = "SELECT COUNT(*) AS count FROM benh WHERE tenbenh = ? AND mabenh != ?";
        int count = 0;

        try (PreparedStatement statement = connectDB.databaseLink.prepareStatement(query)) {
            statement.setString(1, diseaseName); // Thiết lập tham số cho tên bệnh
            statement.setInt(2, currentDiseaseId); // Tránh kiểm tra tên của bệnh đang chỉnh sửa

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    count = resultSet.getInt("count");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return count > 0;
    }

    public boolean isDiseaseICDExists(String diseaseICD, int currentDiseaseId) {
        String query = "SELECT COUNT(*) AS count FROM benh WHERE maicd = ? AND mabenh != ?";
        int count = 0;

        try (PreparedStatement statement = connectDB.databaseLink.prepareStatement(query)) {
            statement.setString(1, diseaseICD);
            statement.setInt(2, currentDiseaseId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    count = resultSet.getInt("count");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return count > 0;
    }

    public int getMaxDiseaseId() {
        int maxId = 0;
        String query = "SELECT MAX(mabenh) AS max_id FROM benh";

        try (PreparedStatement statement = connectDB.databaseLink.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                maxId = resultSet.getInt("max_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return maxId;
    }
}
