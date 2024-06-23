package com.example.privateclinic.DataAccessObject;

import com.example.privateclinic.Models.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.swing.text.DateFormatter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class ExaminationHistoryDAO {
    ConnectDB connectDB = ConnectDB.getInstance();
    DiseaseDAO diseaseDAO = new DiseaseDAO();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public  ObservableList<ExaminationHistory> getHistoryExaminationByYear(int id,int year)
    {
        ObservableList<ExaminationHistory> examinations = FXCollections.observableArrayList();
        String query = "SELECT DISTINCT hd.*, kb.*, bn.*, tn.*, b1.mabenh AS maBenhChinh, b2.mabenh AS maBenhPhu, nv.hoten as tenbs " +
                "FROM khambenh kb JOIN benh b1 ON b1.mabenh = kb.benhchinh LEFT JOIN benh b2 ON b2.mabenh = kb.benhphu JOIN benhnhan bn ON bn.mabn = ? JOIN tiepnhan tn ON tn.matn = kb.matn AND bn.mabn = tn.mabn JOIN hoadon hd ON hd.makb = kb.makb JOIN nhanvien nv ON nv.manv = kb.manv ";
                if(year>0) query+="WHERE EXTRACT(YEAR FROM kb.ngay) = ? ";
        query+= "ORDER BY kb.ngay DESC";
        try (PreparedStatement statement = connectDB.databaseLink.prepareStatement(query)) {
            statement.setInt(1, id);
            if(year>0) statement.setInt(2, year);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Patient patient = new Patient();
                    patient.setPatientId(resultSet.getInt("mabn"));
                    patient.setPatientName(resultSet.getString("hoten"));
                    patient.setPatientGender(resultSet.getString("gioitinh"));
                    patient.setPatientPhoneNumber(resultSet.getString("sdt"));
                    patient.setPatientAddress(resultSet.getString("diachi"));
                    patient.setArrivalDate(resultSet.getDate("ngayvao"));
                    patient.setPatientBirth(resultSet.getDate("ngaysinh"));
                    patient.setNumber(resultSet.getInt("stt"));

                    Examination examination = new Examination();
                    examination.setMatn(resultSet.getInt("matn"));
                    examination.setManv(resultSet.getInt("manv"));
                    examination.setMakb(resultSet.getInt("makb"));
                    examination.setTenNhanVien(getEmployee(examination.getManv()));
                    examination.setMainDisease(diseaseDAO.getDisease(resultSet.getInt("maBenhChinh")));
                    examination.setSubDisease(diseaseDAO.getDisease(resultSet.getInt("maBenhPhu")));
                    examination.setNgay(resultSet.getString("ngay"));
                    examination.setTrieuChung(resultSet.getString("trieuchung"));
                    examination.setLuuy(resultSet.getString("luuy"));
                    examination.setTienkham(resultSet.getInt("tienkham"));
                    examination.setTienthuoc(resultSet.getInt("tienthuoc"));
                    examination.setMahd(resultSet.getInt("mahd"));
                    examination.setTenNhanVien(resultSet.getString("tenbs"));
                    ObservableList<Receipt> receipt = getDetailReceipt(examination.getMakb());

                    ExaminationHistory examinationHistory = new ExaminationHistory(patient,examination,receipt);
                    examinations.add(examinationHistory);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return examinations;
    }
    public  ObservableList<ExaminationHistory> getAllPayslipDayToDay(java.sql.Date dateFrom, java.sql.Date  dateTo)
    {
        ObservableList<ExaminationHistory> examinations = FXCollections.observableArrayList();
        String query = "SELECT DISTINCT hd.*, kb.*, bn.*, tn.*, b1.mabenh AS maBenhChinh, b2.mabenh AS maBenhPhu, nv.hoten as tenbs " +
                "FROM benhnhan bn " +
                "JOIN tiepnhan tn ON bn.mabn = tn.mabn " +
                "JOIN khambenh kb ON tn.matn = kb.matn " +
                "JOIN hoadon hd ON hd.makb = kb.makb " +
                "JOIN nhanvien nv ON nv.manv = kb.manv " +
                "JOIN benh b1 ON b1.mabenh = kb.benhchinh " +
                "LEFT JOIN benh b2 ON b2.mabenh = kb.benhphu " +
                "WHERE kb.ngay::date >= ? AND kb.ngay::date <= ? ";
        query+= "ORDER BY kb.ngay DESC";
        try (PreparedStatement statement = connectDB.databaseLink.prepareStatement(query)) {
            statement.setDate(1, dateFrom);
            statement.setDate(2, dateTo);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Patient patient = new Patient();
                    patient.setPatientId(resultSet.getInt("mabn"));
                    patient.setPatientName(resultSet.getString("hoten"));
                    patient.setPatientGender(resultSet.getString("gioitinh"));
                    patient.setPatientPhoneNumber(resultSet.getString("sdt"));
                    patient.setPatientAddress(resultSet.getString("diachi"));
                    patient.setArrivalDate(resultSet.getDate("ngayvao"));
                    patient.setPatientBirth(resultSet.getDate("ngaysinh"));
                    patient.setNumber(resultSet.getInt("stt"));

                    Examination examination = new Examination();
                    examination.setMatn(resultSet.getInt("matn"));
                    examination.setManv(resultSet.getInt("manv"));
                    examination.setMakb(resultSet.getInt("makb"));
                    examination.setTenNhanVien(getEmployee(examination.getManv()));
                    examination.setMainDisease(diseaseDAO.getDisease(resultSet.getInt("maBenhChinh")));
                    examination.setSubDisease(diseaseDAO.getDisease(resultSet.getInt("maBenhPhu")));
                    examination.setNgay(resultSet.getString("ngay"));
                    examination.setTrieuChung(resultSet.getString("trieuchung"));
                    examination.setLuuy(resultSet.getString("luuy"));
                    examination.setTienkham(resultSet.getInt("tienkham"));
                    examination.setTienthuoc(resultSet.getInt("tienthuoc"));
                    examination.setMahd(resultSet.getInt("mahd"));
                    examination.setTenNhanVien(resultSet.getString("tenbs"));
                    ObservableList<Receipt> receipt = getDetailReceipt(examination.getMakb());

                    ExaminationHistory examinationHistory = new ExaminationHistory(patient,examination,receipt);
                    examinations.add(examinationHistory);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return examinations;
    }
    public  ObservableList<ExaminationHistory> getAllHistoryExamination(int day,int month, int year)
    {
        ObservableList<ExaminationHistory> examinations = FXCollections.observableArrayList();
        String query = "SELECT DISTINCT hd.*, kb.*, bn.*, tn.*, b1.mabenh AS maBenhChinh, b2.mabenh AS maBenhPhu, nv.hoten as tenbs " +
                "FROM benhnhan bn " +
                "JOIN tiepnhan tn ON bn.mabn = tn.mabn " +
                "JOIN khambenh kb ON tn.matn = kb.matn " +
                "JOIN hoadon hd ON hd.makb = kb.makb " +
                "JOIN nhanvien nv ON nv.manv = kb.manv " +
                "JOIN benh b1 ON b1.mabenh = kb.benhchinh " +
                "LEFT JOIN benh b2 ON b2.mabenh = kb.benhphu " ;


        if(year>0) query+="WHERE EXTRACT(YEAR FROM kb.ngay) = ? ";
        if( day>0) query+="AND EXTRACT(DAY FROM kb.ngay) = ? ";
        if(month>0) query+="AND EXTRACT(MONTH FROM kb.ngay) = ? ";
        query+= "ORDER BY kb.ngay DESC";
        try (PreparedStatement statement = connectDB.databaseLink.prepareStatement(query)) {
            if(year>0) statement.setInt(1, year);
            if(day>0) {
                statement.setInt(2,day);
                if(month>0) statement.setInt(3,month);
            } else {
                if(month>0) statement.setInt(2,month);
            }
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Patient patient = new Patient();
                    patient.setPatientId(resultSet.getInt("mabn"));
                    patient.setPatientName(resultSet.getString("hoten"));
                    patient.setPatientGender(resultSet.getString("gioitinh"));
                    patient.setPatientPhoneNumber(resultSet.getString("sdt"));
                    patient.setPatientAddress(resultSet.getString("diachi"));
                    patient.setArrivalDate(resultSet.getDate("ngayvao"));
                    patient.setPatientBirth(resultSet.getDate("ngaysinh"));
                    patient.setNumber(resultSet.getInt("stt"));

                    Examination examination = new Examination();
                    examination.setMatn(resultSet.getInt("matn"));
                    examination.setManv(resultSet.getInt("manv"));
                    examination.setMakb(resultSet.getInt("makb"));
                    examination.setTenNhanVien(getEmployee(examination.getManv()));
                    examination.setMainDisease(diseaseDAO.getDisease(resultSet.getInt("maBenhChinh")));
                    examination.setSubDisease(diseaseDAO.getDisease(resultSet.getInt("maBenhPhu")));
                    examination.setNgay(resultSet.getString("ngay"));
                    examination.setTrieuChung(resultSet.getString("trieuchung"));
                    examination.setLuuy(resultSet.getString("luuy"));
                    examination.setTienkham(resultSet.getInt("tienkham"));
                    examination.setTienthuoc(resultSet.getInt("tienthuoc"));
                    examination.setMahd(resultSet.getInt("mahd"));
                    examination.setTenNhanVien(resultSet.getString("tenbs"));
                    ObservableList<Receipt> receipt = getDetailReceipt(examination.getMakb());

                    ExaminationHistory examinationHistory = new ExaminationHistory(patient,examination,receipt);
                    examinations.add(examinationHistory);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return examinations;
    }
    private ObservableList<Receipt> getDetailReceipt(int exam_id) {
        ObservableList<Receipt> detailReceipts = FXCollections.observableArrayList();
        String query ="SELECT hd.*, t.*,ct.*, cd.tencd,dvt.tendvt,dt.tendt, ct.soluong as sl FROM hoadon hd,cachdung cd, dangthuoc dt, donvitinh dvt,thuoc t,cthd ct WHERE hd.makb = ? AND ct.mathuoc = t.mathuoc AND ct.mahd = hd.mahd"+
                " AND t.macd = cd.macd AND t.madt = dt.madt AND t.madvt = dvt.madvt";
        try (PreparedStatement statement = connectDB.databaseLink.prepareStatement(query)) {
            statement.setInt(1,exam_id);
            try(ResultSet resultSet = statement.executeQuery()) {
                if(resultSet==null) return null;
                while (resultSet.next()) {
                    Receipt rowDetailreceipt = new Receipt();
                    rowDetailreceipt.setMakhambenh(exam_id);
                    rowDetailreceipt.setSothuTu(resultSet.getInt("stt"));
                    rowDetailreceipt.setMaThuoc(resultSet.getInt("mathuoc"));
                    rowDetailreceipt.setTenThuoc(resultSet.getString("tenthuoc"));
                    rowDetailreceipt.setTenCachDung(resultSet.getString("tencd"));
                    rowDetailreceipt.setTenDangThuoc(resultSet.getString("tendt"));
                    rowDetailreceipt.setTenDonViTinh(resultSet.getString("tendvt"));
                    rowDetailreceipt.setNgay(resultSet.getInt("ngay"));
                    rowDetailreceipt.setSang(resultSet.getInt("sang"));
                    rowDetailreceipt.setTrua(resultSet.getInt("trua"));
                    rowDetailreceipt.setChieu(resultSet.getInt("chieu"));
                    rowDetailreceipt.setToi(resultSet.getInt("toi"));
                    rowDetailreceipt.setSoLuong(resultSet.getInt("sl"));
                    rowDetailreceipt.setDonGia(resultSet.getInt("giaban"));
                    rowDetailreceipt.setThanhTien(resultSet.getInt("thanhtien"));
                    rowDetailreceipt.setNote(resultSet.getString("note"));
                    detailReceipts.add(rowDetailreceipt);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return detailReceipts;
    }
    public String getEmployee(int id) throws SQLException {
        String query ="SELECT hoten FROM nhanvien WHERE manv = "+id+"";
        return connectDB.getData(query).getString("hoten");
    }
}
