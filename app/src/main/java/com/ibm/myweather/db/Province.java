package com.ibm.myweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by 刘旭 on 2016/12/31.
 */

public class Province extends DataSupport{
    private int id;
    private String ProvinceName;
    private int ProvinceCode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProvinceName() {
        return ProvinceName;
    }

    public void setProvinceName(String provinceName) {
        ProvinceName = provinceName;
    }

    public int getProvinceCode() {
        return ProvinceCode;
    }

    public void setProvinceCode(int provinceCode) {
        ProvinceCode = provinceCode;
    }
}
