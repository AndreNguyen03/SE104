package com.example.privateclinic.DataAccessObject;

import com.example.privateclinic.Models.DrugUsageReport;
import com.example.privateclinic.Models.MonthlyReport;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReportDAO {

    public List<MonthlyReport> getMonthlyReports(Connection conn) throws SQLException {
        List<MonthlyReport> monthlyReports = new ArrayList<>();
        String query = "SELECT EXTRACT(MONTH FROM khambenh.ngay) AS thang, SUM(hoadon.tongtien) AS doanhthu, COUNT(*) AS sokham FROM khambenh JOIN hoadon ON khambenh.makb = hoadon.makb GROUP BY EXTRACT(MONTH FROM khambenh.ngay)";

        try (PreparedStatement statement = conn.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                int month = resultSet.getInt("thang");
                double revenue = resultSet.getDouble("doanhthu");
                int patientCount = resultSet.getInt("sokham");
                MonthlyReport monthlyReport = new MonthlyReport(month, patientCount, revenue);
                monthlyReports.add(monthlyReport);
            }
        }
        return monthlyReports;
    }

    public List<DrugUsageReport> getDrugUsageReports(Connection conn) throws SQLException {
        List<DrugUsageReport> drugUsageReports = new ArrayList<>();
        String query = "SELECT thuoc.tenthoc, SUM(kethuoc.soluong) AS tong_so_luong FROM kethuoc JOIN thuoc ON kethuoc.mathuoc = thuoc.mathuoc GROUP BY thuoc.tenthoc";

        try (PreparedStatement statement = conn.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                String drugName = resultSet.getString("tenthoc");
                int quantity = resultSet.getInt("tong_so_luong");
                DrugUsageReport drugUsageReport = new DrugUsageReport(drugName, quantity);
                drugUsageReports.add(drugUsageReport);
            }
        }
        return drugUsageReports;
    }
}

