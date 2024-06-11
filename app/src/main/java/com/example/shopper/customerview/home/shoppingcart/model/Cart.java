package com.example.shopper.customerview.home.shoppingcart.model;

public class Cart {
    private String MaSP;
    private String MaGH;
    private String dataImage;
    private String TenSanPham;
    private int GiaSP;
    private int GiaTien;
    private int SoLuong;
    private String Size;
    private String MauSac;
    private boolean Check;

    public Cart(String maSP, String MaGH, String dataImage, String TenSP, int GiaSP, int SoLuong, int GiaTien, String Size, String MauSac, boolean Check){
        this.MaSP = maSP;
        this.MaGH = MaGH;
        this.dataImage = dataImage;
        this.TenSanPham = TenSP;
        this.GiaSP = GiaSP;
        this.GiaTien = GiaTien;
        this.SoLuong = SoLuong;
        this.Size = Size;
        this.MauSac = MauSac;
        this.Check = Check;
    }

    public String getTenSanPham() {
        return TenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        TenSanPham = tenSanPham;
    }



    public int getSoLuong() {
        return SoLuong;
    }

    public void setSoLuong(int soLuong) {
        SoLuong = soLuong;
    }

    public boolean isCheck() {
        return Check;
    }

    public void setCheck(boolean check) {
        Check = check;
    }

    public String getDataImage() {
        return dataImage;
    }

    public void setDataImage(String dataImage) {
        this.dataImage = dataImage;
    }

    public String getSize() {
        return Size;
    }

    public void setSize(String size) {
        Size = size;
    }

    public String getMauSac() {
        return MauSac;
    }

    public void setMauSac(String mauSac) {
        MauSac = mauSac;
    }

    public String getMaSP() {
        return MaSP;
    }

    public void setMaSP(String maSP) {
        MaSP = maSP;
    }

    public String getMaGH() {
        return MaGH;
    }

    public void setMaGH(String maGH) {
        MaGH = maGH;
    }


    public int getGiaSP() {
        return GiaSP;
    }

    public void setGiaSP(int giaSP) {
        GiaSP = giaSP;
    }

    public int getGiaTien() {
        return GiaTien;
    }

    public void setGiaTien(int giaTien) {
        GiaTien = giaTien;
    }
}
