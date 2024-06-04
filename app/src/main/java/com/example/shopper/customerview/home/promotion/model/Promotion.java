package com.example.shopper.customerview.home.promotion.model;

import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Promotion {
    private String MaKM;
    private String TenKM;
    private Double TiLe;
    private String HinhAnhKM;
    private String ChiTietKM;
    private Integer DonToiThieu;
    private String LoaiGiamGia;
    private Timestamp NgayBatDau;
    private Timestamp NgayKetThuc;
    private boolean Check;

    public Promotion(String maKM, String tenKM, Double tiLe, String hinhAnhKM, String chiTietKM, Integer donToiThieu, String loaiGiamGia, Timestamp ngayBatDau, Timestamp ngayKetThuc, Boolean check) {
        MaKM = maKM;
        TenKM = tenKM;
        TiLe = tiLe;
        HinhAnhKM = hinhAnhKM;
        ChiTietKM = chiTietKM;
        DonToiThieu = donToiThieu;
        LoaiGiamGia = loaiGiamGia;
        NgayBatDau = ngayBatDau;
        NgayKetThuc = ngayKetThuc;
        Check = check;
    }


    public String getMaKM() {
        return MaKM;
    }

    public void setMaKM(String maKM) {
        MaKM = maKM;
    }

    public String getTenKM() {
        return TenKM;
    }

    public void setTenKM(String tenKM) {
        TenKM = tenKM;
    }

    public Double getTiLe() {
        return TiLe;
    }

    public void setTiLe(Double tiLe) {
        TiLe = tiLe;
    }

    public String getHinhAnhKM() {
        return HinhAnhKM;
    }

    public void setHinhAnhKM(String hinhAnhKM) {
        HinhAnhKM = hinhAnhKM;
    }

    public String getChiTietKM() {
        return ChiTietKM;
    }

    public void setChiTietKM(String chiTietKM) {
        ChiTietKM = chiTietKM;
    }

    public Integer getDonToiThieu() {
        return DonToiThieu;
    }

    public void setDonToiThieu(Integer donToiThieu) {
        DonToiThieu = donToiThieu;
    }

    public String getLoaiGiamGia() {
        return LoaiGiamGia;
    }

    public void setLoaiGiamGia(String loaiGiamGia) {
        LoaiGiamGia = loaiGiamGia;
    }

    public String getNgayBatDau() {
        Date date = NgayBatDau.toDate();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); // Định dạng ngày tháng
        String formattedDate = sdf.format(date);
        return formattedDate;
    }

    public void setNgayBatDau(Timestamp ngayBatDau) {
        NgayBatDau = ngayBatDau;
    }

    public String getNgayKetThuc() {

        Date date = NgayKetThuc.toDate();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); // Định dạng ngày tháng
        String formattedDate = sdf.format(date);
        return formattedDate;
    }

    public void setNgayKetThuc(Timestamp ngayKetThuc) {
        NgayKetThuc = ngayKetThuc;
    }

    public boolean isCheck() {
        return Check;
    }

    public void setCheck(boolean check) {
        Check = check;
    }
}
