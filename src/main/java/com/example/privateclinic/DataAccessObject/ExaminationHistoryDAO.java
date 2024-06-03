package com.example.privateclinic.DataAccessObject;

import com.example.privateclinic.Models.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ExaminationHistoryDAO {
    ConnectDB connectDB = ConnectDB.getInstance();

    public  ObservableList<ExaminationHistory> getPatientsByDate(int id, int year)
    {
        ObservableList<ExaminationHistory> examinations = FXCollections.observableArrayList();
        String query = "SELECT kb.*,bn.*, b1.tenbenh as tenBenhChinh, b2.tenbenh as tenBenhPhu " +
                "FROM khambenh kb, kethuoc kt,benh b1,benh b2, benhnhan bn WHERE EXTRACT(YEAR FROM kb.ngay) <= ? and bn.mabn= ?  AND bn.mabn = kb.mabn AND kt.makhambenh = kb.makb AND b1.mabenh=kb.benhchinh AND b2.mabenh=kb.benhphu";
        try (PreparedStatement statement = connectDB.databaseLink.prepareStatement(query)) {
            statement.setInt(1, year);
            statement.setInt(2, id);
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

                    Examination examination = new Examination();
                    examination.setMabn(resultSet.getInt("mabn"));
                    examination.setManv(resultSet.getInt("manv"));
                    examination.setMakb(resultSet.getInt("makb"));
                    examination.setTenBenhChinh(resultSet.getString("tenBenhChinh"));
                    examination.setTenBenhPhu(resultSet.getString("tenBenhPhu"));
                    examination.setNgay(resultSet.getDate("ngay"));
                    examination.setTrieuChung(resultSet.getString("trieuchung"));
                    examination.setLuuy(resultSet.getString("luuy"));
                    examination.setMaBenhChinh(resultSet.getInt("benhchinh"));
                    examination.setMaBenhPhu(resultSet.getInt("benhphu"));

                    ObservableList<Prescribe> prescribes = GetPrescribes(examination.getMakb());

                    ExaminationHistory examinationHistory = new ExaminationHistory(patient,examination,prescribes);
                    examinations.add(examinationHistory);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return examinations;
    }
    private ObservableList<Prescribe> GetPrescribes(int exam_id) {
        ObservableList<Prescribe> prescribes = FXCollections.observableArrayList();
        String query ="SELECT kt.*, t.*, cd.tencd,dvt.tendvt,dt.tendt FROM kethuoc kt,cachdung cd, dangthuoc dt, donvitinh dvt,thuoc t WHERE makhambenh = ? AND kt.mathuoc = t.mathuoc"+
                " AND t.macd = cd.macd AND t.madt = dt.madt AND t.madvt = dvt.madvt";
        try (PreparedStatement statement = connectDB.databaseLink.prepareStatement(query)) {
            statement.setInt(1,exam_id);
            try(ResultSet resultSet = statement.executeQuery()) {
                if(resultSet==null) return null;
                while (resultSet.next()) {
                    Prescribe prescribe = new Prescribe();
                    prescribe.setMakhambenh(exam_id);
                    prescribe.setSothuTu(resultSet.getInt("sothutu"));
                    prescribe.setMaThuoc(resultSet.getInt("mathuoc"));
                    prescribe.setTenThuoc(resultSet.getString("tenthuoc"));
                    prescribe.setTenCachDung(resultSet.getString("tencd"));
                    prescribe.setTenDangThuoc(resultSet.getString("tendt"));
                    prescribe.setTenDonViTinh(resultSet.getString("tendvt"));
                    prescribe.setNgay(resultSet.getInt("ngay"));
                    prescribe.setSang(resultSet.getInt("sang"));
                    prescribe.setTrua(resultSet.getInt("trua"));
                    prescribe.setChieu(resultSet.getInt("chieu"));
                    prescribe.setToi(resultSet.getInt("toi"));
                    prescribe.setSoLuong(resultSet.getInt("soluong"));
                    prescribe.setDonGia(resultSet.getDouble("giaban"));
                    prescribe.setThanhTien(resultSet.getInt("thanhtien"));
                    prescribes.add(prescribe);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prescribes;
    }
}
