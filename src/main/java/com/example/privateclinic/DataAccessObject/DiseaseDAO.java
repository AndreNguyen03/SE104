package com.example.privateclinic.DataAccessObject;

import com.example.privateclinic.Models.ConnectDB;
import com.example.privateclinic.Models.Disease;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DiseaseDAO {
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
}
