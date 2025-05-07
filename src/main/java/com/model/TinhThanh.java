package com.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class TinhThanh {
    @Id
    private String maTinhThanh;

    private String tenTinhThanh;


    public String getMaTinhThanh() {
        return maTinhThanh;
    }

    public void setMaTinhThanh(String maTinhThanh) {
        this.maTinhThanh = maTinhThanh;
    }

    public String getTenTinhThanh() {
        return tenTinhThanh;
    }

    public void setTenTinhThanh(String tenTinhThanh) {
        this.tenTinhThanh = tenTinhThanh;
    }
}
