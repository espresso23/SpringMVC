package com.model;

import jakarta.persistence.*;

@Entity
public class TinhThanh {
    @Id
    private String maTinhThanh;

    @Column(columnDefinition = "NVARCHAR(50)")
    private String tenTinhThanh;

    public TinhThanh() {
    }

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
