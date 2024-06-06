package com.example.privateclinic.DataAccessObject;

import com.example.privateclinic.Models.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ExaminationHistoryDAO {
    ConnectDB connectDB = ConnectDB.getInstance();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    public  ObservableList<ExaminationHistory> getPatientsByDate(int id, int year)
    {
        ObservableList<ExaminationHistory> examinations = FXCollections.observableArrayList();
        String query = "SELECT DISTINCT kb.*,bn.*,tn.*, b1.tenbenh as tenBenhChinh, b2.tenbenh as tenBenhPhu " +
                "FROM khambenh kb, kethuoc kt,benh b1,benh b2, benhnhan bn,tiepnhan tn WHERE EXTRACT(YEAR FROM kb.ngay) <= ? and bn.mabn= ?  AND bn.mabn = kb.mabn AND kt.makhambenh = kb.makb AND b1.mabenh=kb.benhchinh AND b2.mabenh=kb.benhphu AND bn.mabn = tn.mabn";
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
                    examination.setMatn(resultSet.getInt("matn"));
                    examination.setManv(resultSet.getInt("manv"));
                    examination.setMakb(resultSet.getInt("makb"));
                    examination.setTenNhanVien(getEmployee(examination.getManv()));
                    examination.setTenBenhChinh(resultSet.getString("tenBenhChinh"));
                    examination.setTenBenhPhu(resultSet.getString("tenBenhPhu"));
                    examination.setNgay(LocalDateTime.parse(resultSet.getObject("ngay").toString(),formatter));
                    examination.setTrieuChung(resultSet.getString("trieuchung"));
                    examination.setLuuy(resultSet.getString("luuy"));
                    examination.setMaBenhChinh(resultSet.getInt("benhchinh"));
                    examination.setMaBenhPhu(resultSet.getInt("benhphu"));

                    ObservableList<Receipt> prescribes = GetPrescribes(examination.getMakb());

                    ExaminationHistory examinationHistory = new ExaminationHistory(patient,examination,prescribes);
                    examinations.add(examinationHistory);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return examinations;
    }
    private ObservableList<Receipt> GetPrescribes(int exam_id) {
        ObservableList<Receipt> prescribes = FXCollections.observableArrayList();
        String query ="SELECT kt.*, t.*, cd.tencd,dvt.tendvt,dt.tendt FROM kethuoc kt,cachdung cd, dangthuoc dt, donvitinh dvt,thuoc t WHERE makhambenh = ? AND kt.mathuoc = t.mathuoc"+
                " AND t.macd = cd.macd AND t.madt = dt.madt AND t.madvt = dvt.madvt";
        try (PreparedStatement statement = connectDB.databaseLink.prepareStatement(query)) {
            statement.setInt(1,exam_id);
            try(ResultSet resultSet = statement.executeQuery()) {
                if(resultSet==null) return null;
                while (resultSet.next()) {
                    Receipt prescribe = new Receipt();
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
                    prescribe.setDonGia(resultSet.getInt("giaban"));
                    prescribe.setThanhTien(resultSet.getInt("thanhtien"));
                    prescribes.add(prescribe);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prescribes;
    }
    public String getEmployee(int id) throws SQLException {
        String query ="SELECT hoten FROM nhanvien WHERE manv = "+id+"";
        return connectDB.getData(query).getString("hoten");
    }
}
