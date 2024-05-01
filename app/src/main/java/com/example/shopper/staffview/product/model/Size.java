package com.example.shopper.staffview.product.model;

public class Size {
    private String sizeName;
    private boolean checked;
    private String sizeCode;

    public Size(String SizeName, String SizeCode, boolean Checked) {
        this.sizeName = SizeName;
        this.sizeCode = SizeCode;
        this.checked = Checked;
    }

    public String getSizeName() {
        return sizeName;
    }

    public String getSizeCode() {
        return sizeCode;
    }

    public void setSizeCode(String SizeCode) {
        SizeCode = SizeCode;
    }

    public void setSizeName(String SizeName) {
        this.sizeName = SizeName;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean Checked) {
        this.checked = Checked;
    }
}
