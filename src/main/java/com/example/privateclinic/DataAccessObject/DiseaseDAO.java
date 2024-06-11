package com.example.privateclinic.DataAccessObject;

import com.example.privateclinic.Models.ConnectDB;
import com.example.privateclinic.Models.Disease;
import com.example.privateclinic.Models.History;
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
    ConnectDB connectDB = ConnectDB.getInstance();

    public DiseaseDAO() {
    }
    public ObservableList<Disease> searchDiseaseByIDorName(String idOrName)
    {
        ObservableList<Disease> diseases = FXCollections.observableArrayList();
        String query = "SELECT * FROM benh ";
        boolean isInteger = false;
        if(!idOrName.isEmpty()) {
            query+="WHERE unaccent(tenbenh) ILIKE unaccent(?) ";
            try {
                int id = Integer.parseInt(idOrName);
                query += "OR mabenh = ?";
                isInteger = true;
            } catch (NumberFormatException e) {
            }
        }
        try (PreparedStatement statement = connectDB.databaseLink.prepareStatement(query))
        {
            if(!idOrName.isEmpty()) {
                statement.setString(1, "%" + idOrName.trim() + "%");
                if (isInteger) statement.setInt(2, Integer.parseInt(idOrName));
            }
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next())
            {
                Disease disease = new Disease();
                disease.setMaBenh(resultSet.getInt("mabenh"));
                disease.setTenBenh(resultSet.getString("tenbenh"));
                disease.setMaICD(resultSet.getString("maicd"));
                diseases.add(disease);
            }
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
        return diseases;
    }

    public boolean addDisease(Disease disease) {

        String query = "INSERT INTO benh (mabenh, tenbenh, maicd) VALUES (?, ?, ?)";

        try (PreparedStatement statement = connectDB.databaseLink.prepareStatement(query)) {

            statement.setInt(1, disease.getMaBenh());
            statement.setString(2, disease.getTenBenh());
            statement.setString(3, disease.getMaICD());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean addDiseases(List<Disease> diseases) {
        String query = "INSERT INTO benh (mabenh, tenbenh, maicd) VALUES (?, ?, ?)";

        try (PreparedStatement statement = connectDB.databaseLink.prepareStatement(query)) {
            for (Disease disease : diseases) {
                statement.setInt(1, disease.getMaBenh());
                statement.setString(2, disease.getTenBenh());
                statement.setString(3, disease.getMaICD());
                statement.addBatch();
            }

            int[] arr = statement.executeBatch();
            if(arr.length==0) return false;
            for(int i : arr) {
                if(i==0) return false;
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
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


    public boolean updateDisease(Disease disease) {
        String query = "UPDATE benh SET tenbenh = ?, maicd = ? WHERE mabenh = ?";

        try (PreparedStatement statement = connectDB.databaseLink.prepareStatement(query)) {

            statement.setString(1, disease.getTenBenh());
            statement.setString(2, disease.getMaICD());
            statement.setInt(3, disease.getMaBenh());

            return statement.executeUpdate()>0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteDisease(int diseaseID) {
        String query = "DELETE FROM benh WHERE mabenh = ?";

        try (PreparedStatement statement = connectDB.databaseLink.prepareStatement(query)) {

            statement.setInt(1, diseaseID);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
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
    public Disease getDisease(int id ){
        Disease disease = new Disease();
        if(id ==0 ) return disease;
        String query = "SELECT * FROM benh WHERE mabenh = "+id+"";
        try (ResultSet resultSet = connectDB.getResultSet(query))
        {
            if(resultSet.next()) {
                disease = new Disease(
                        resultSet.getInt("mabenh"),
                        resultSet.getString("tenbenh"),
                        resultSet.getString("maicd")
                );
            }
            return disease;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
