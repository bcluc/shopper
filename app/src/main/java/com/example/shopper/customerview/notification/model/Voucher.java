package com.example.shopper.customerview.notification.model;

import com.google.firebase.Timestamp;

public class Voucher {
    private String TenKM;
    private Timestamp Thoigian;
    private String LoaiKhuyenMai;
    private String AnhTB;
    private String HinhAnhKM;
    private String HinhAnhTB;
    private String MaTB;
    private String MaKM;
    private String NoiDung;
    private String TB;
    private String LoaiTB;
    private String MaDH, maND;

    public Voucher(String HinhAnhKM, String HinhAnhTB, String MaTB, String MaKM, String NoiDung, String TB, String LoaiTB, String TenKM) {
        this.MaTB = MaTB;
        this.MaKM = MaKM;
        this.TB = TB;
        this.LoaiTB = LoaiTB;
        this.NoiDung = NoiDung;
        this.HinhAnhKM = HinhAnhKM;
        this.HinhAnhTB = HinhAnhTB;
        this.TenKM = TenKM;
    }

    public Voucher(String HinhAnhKM, String HinhAnhTB, String MaTB, String MaKM, String NoiDung, String TB, String LoaiTB, String TenKM, Timestamp Thoigian) {
        this.MaTB = MaTB;
        this.MaKM = MaKM;
        this.TB = TB;
        this.LoaiTB = LoaiTB;
        this.NoiDung = NoiDung;
        this.HinhAnhKM = HinhAnhKM;
        this.HinhAnhTB = HinhAnhTB;
        this.TenKM = TenKM;
        this.Thoigian = Thoigian;
    }

    public Voucher(String AnhTB, String LoaiTB, String NoiDung, String MaTB, String TB, String MaDH, String MaND) {
        this.AnhTB = AnhTB;
        this.LoaiTB = LoaiTB;
        this.NoiDung = NoiDung;
        this.MaTB = MaTB;
        this.TB = TB;
        this.MaDH = MaDH;
        this.maND = MaND;
    }
    public Voucher(String AnhTB, String LoaiTB, String NoiDung, String MaTB, String TB, String MaDH, String MaND, Timestamp timestamp) {
        this.AnhTB = AnhTB;
        this.LoaiTB = LoaiTB;
        this.NoiDung = NoiDung;
        this.MaTB = MaTB;
        this.TB = TB;
        this.MaDH = MaDH;
        this.maND = MaND;
        this.Thoigian = timestamp;
    }

    public Timestamp getThoigian() {
        return Thoigian;
    }

    public void setThoigian(Timestamp thoigian) {
        Thoigian = thoigian;
    }

    public String getLoaiKhuyenMai() {
        return LoaiKhuyenMai;
    }

    public void setLoaiKhuyenMai(String loaiKhuyenMai) {
        LoaiKhuyenMai = loaiKhuyenMai;
    }

    public String getHinhAnhKM() {
        return HinhAnhKM;
    }

    public void setHinhAnhKM(String hinhAnhKM) {
        HinhAnhKM = hinhAnhKM;
    }

    public String getHinhAnhTB() {
        return HinhAnhTB;
    }

    public void setHinhAnhTB(String hinhAnhTB) {
        HinhAnhTB = hinhAnhTB;
    }

    public String getAnhTB() {
        return AnhTB;
    }

    public void setAnhTB(String anhTB) {
        AnhTB = anhTB;
    }

    public String getTenKM() {
        return TenKM;
    }

    public void setTenKM(String tenKM) {
        TenKM = tenKM;
    }

    public String getMaTB() {
        return MaTB;
    }

    public void setMaTB(String maTB) {
        MaTB = maTB;
    }

    public String getMaKM() {
        return MaKM;
    }

    public void setMaKM(String maKM) {
        MaKM = maKM;
    }

    public String getNoiDung() {
        return NoiDung;
    }

    public void setNoiDung(String noiDung) {
        NoiDung = noiDung;
    }

    public String getTB() {
        return TB;
    }

    public void setTB(String TB) {
        this.TB = TB;
    }

    public String getLoaiTB() {
        return LoaiTB;
    }

    public void setLoaiTB(String loaiTB) {
        LoaiTB = loaiTB;
    }

    public String getMaDH() {
        return MaDH;
    }

    public void setMaDH(String maDH) {
        MaDH = maDH;
    }

    public String getMaND() {
        return maND;
    }

    public void setMaND(String maND) {
        this.maND = maND;
    }
}
