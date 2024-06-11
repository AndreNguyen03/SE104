package com.example.privateclinic.DataAccessObject;

import com.example.privateclinic.Models.ConnectDB;
import com.example.privateclinic.Models.DrugUsageReport;
import com.example.privateclinic.Models.MonthlyReport;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ReportDAO {
    ConnectDB connectDB = ConnectDB.getInstance();
    DateTimeFormatter formatterDatePicker = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    public List<MonthlyReport> getMonthlyReports(int month,int year) {
        List<MonthlyReport> monthlyReports = new ArrayList<>();
        String query = "SELECT count(kb.makb) as count,kb.ngay::date as date, SUM(hd.tongtien) as total, SUM(hd.tongtien)/count(kb.makb) as average  FROM khambenh kb,hoadon hd where hd.makb = kb.makb and EXTRACT(MONTH FROM kb.ngay) = ? AND EXTRACT(YEAR FROM kb.ngay) = ? GROUP BY ngay::date ORDER BY kb.ngay::date desc";
        try (PreparedStatement statement = connectDB.databaseLink.prepareStatement(query)){
             statement.setInt(1,month);
             statement.setInt(2,year);
             ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                double result = (double) resultSet.getInt("total") / getToTalMonth(month, year);
                MonthlyReport monthlyReport = new MonthlyReport(
                        resultSet.getDate("date").toLocalDate().format(formatterDatePicker),
                        resultSet.getInt("count"),
                        resultSet.getInt("total"),
                        resultSet.getDouble("average"),
                        (double) Math.round(result * 100) / 100);
                monthlyReports.add(monthlyReport);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return monthlyReports;
    }

    public List<DrugUsageReport> getDrugUsageReports(int month,int year) {
        List<DrugUsageReport> drugUsageReports = new ArrayList<>();
        String query = "SELECT thuoc.tenthuoc,dvt.tendvt, SUM(cthd.soluong) AS tong_so_luong, COUNT(*) as so_lan_dung FROM cthd, thuoc, donvitinh dvt,hoadon hd,khambenh kb " +
                "WHERE cthd.mathuoc = thuoc.mathuoc and dvt.madvt = thuoc.madvt and cthd.mahd = hd.mahd and hd.makb = kb.makb " +
                "AND EXTRACT(MONTH FROM kb.ngay) = ? AND EXTRACT(YEAR FROM kb.ngay) = ? " +
                "GROUP BY thuoc.tenthuoc,dvt.tendvt " +
                "ORDER BY  SUM(cthd.soluong) DESC";
        try (PreparedStatement statement = connectDB.databaseLink.prepareStatement(query))
        {
            statement.setInt(1,month);
            statement.setInt(2,year);
             ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                DrugUsageReport drugUsageReport = new DrugUsageReport(
                        resultSet.getString("tenthuoc"),
                        resultSet.getString("tendvt"),
                        resultSet.getInt("tong_so_luong"),
                        resultSet.getInt("so_lan_dung")
                );
                drugUsageReports.add(drugUsageReport);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return drugUsageReports;
    }
    public int getToTalMonth(int month,int year) throws SQLException {
        String query =" SELECT SUM(hd.tongtien) as sl FROM hoadon hd, khambenh kb WHERE kb.makb = hd.makb AND EXTRACT(MONTH FROM kb.ngay) = ? AND EXTRACT(YEAR FROM kb.ngay) = ? ";
        int total = 0;
        try(PreparedStatement statement = connectDB.databaseLink.prepareStatement(query)) {
            statement.setInt(1,month);
            statement.setInt(2,year);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) {
                total=resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

    public String GetNumberOfProductMonth(int month, int year) throws SQLException {
        String query = "SELECT SUM(cthd.soluong) AS tong_so_luong FROM cthd, hoadon hd,khambenh kb  where cthd.mahd = hd.mahd and hd.makb = kb.makb and EXTRACT(MONTH FROM kb.ngay) = "+month+" AND EXTRACT(YEAR FROM kb.ngay) = "+year+" ";
        ResultSet resultSet = connectDB.getData(query);
        if(resultSet==null) return "0";
        return String.valueOf(connectDB.getData(query).getString("tong_so_luong"));
    }

    public String GetNumberOfProductToday(int day, int month, int year) throws SQLException {
        String query = "SELECT SUM(cthd.soluong) AS tong_so_luong FROM cthd, hoadon hd,khambenh kb  where cthd.mahd = hd.mahd and hd.makb = kb.makb and EXTRACT(DAY FROM kb.ngay) = "+day+" and EXTRACT(MONTH FROM kb.ngay) = "+month+" AND EXTRACT(YEAR FROM kb.ngay) = "+year+" ";
        ResultSet resultSet = connectDB.getData(query);
        if(resultSet==null) return "0";
        return String.valueOf(connectDB.getData(query).getString("tong_so_luong"));
    }

    public String GetRevenueFromExamMonth(int month, int year) throws SQLException {
        String query = "SELECT SUM(hd.tongtien) as total FROM khambenh kb,hoadon hd where hd.makb = kb.makb and EXTRACT(MONTH FROM kb.ngay) = "+month+" AND EXTRACT(YEAR FROM kb.ngay) = "+year+" ";
        ResultSet resultSet = connectDB.getData(query);
        if(resultSet==null) return "0";
        return String.valueOf(connectDB.getData(query).getString("total"));
    }

    public String GetRevenueFromExamToday(int day, int month, int year) throws SQLException {
        String query = "SELECT SUM(hd.tongtien) as total FROM khambenh kb,hoadon hd where hd.makb = kb.makb and EXTRACT(DAY FROM kb.ngay) = "+day+" and EXTRACT(MONTH FROM kb.ngay) = "+month+" AND EXTRACT(YEAR FROM kb.ngay) = "+year+" ";
        ResultSet resultSet = connectDB.getData(query);
        if(resultSet==null) return "0";
        return String.valueOf(connectDB.getData(query).getString("total"));
    }

    public String GetNumberExamOfMonth(int month, int year) throws SQLException {
        String query = "SELECT COUNT(*) as cakham FROM khambenh kb WHERE EXTRACT(MONTH FROM kb.ngay) = "+month+" AND EXTRACT(YEAR FROM kb.ngay) = "+year+" ";
        ResultSet resultSet = connectDB.getData(query);
        if(resultSet==null) return "0";
        return String.valueOf(connectDB.getData(query).getString("cakham"));
    }

    public String GetNumberExamOfToday(int day, int month, int year) throws SQLException {
        String query = "SELECT COUNT(*) as cakham FROM khambenh kb WHERE EXTRACT(DAY FROM kb.ngay) = "+day+" AND EXTRACT(MONTH FROM kb.ngay) = "+month+" AND EXTRACT(YEAR FROM kb.ngay) = "+year+" ";
        ResultSet resultSet = connectDB.getData(query);
        if(resultSet==null) return "0";
        return String.valueOf(connectDB.getData(query).getString("cakham"));

    }
}
