package com.example.shopper.customerview.home.address.model;

public class Address {
    private String MaDC;
    private String Name;
    private String Sdt;
    private String DiaChi;
    private String PhuongXa;
    private Boolean check;

    public Address(String MaDC, String Name, String Sdt, String DiaChi, String PhuongXa, Boolean check) {
        this.MaDC = MaDC;
        this.Name = Name;
        this.Sdt = Sdt;
        this.DiaChi = DiaChi;
        this.PhuongXa = PhuongXa;
        this.check = check;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getSdt() {
        return Sdt;
    }

    public void setSdt(String sdt) {
        Sdt = sdt;
    }

    public String getDiaChi() {
        return DiaChi;
    }

    public void setDiaChi(String diaChi) {
        DiaChi = diaChi;
    }

    public String getPhuongXa() {
        return PhuongXa;
    }

    public void setPhuongXa(String phuongXa) {
        PhuongXa = phuongXa;
    }


    public Boolean getCheck() {
        return check;
    }

    public void setCheck(Boolean check) {
        this.check = check;
    }

    public String getMaDC() {
        return MaDC;
    }

    public void setMaDC(String maDC) {
        MaDC = maDC;
    }
}
