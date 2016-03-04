package com.meidebi.app.service.bean.base;

import java.util.List;

/**
 * Created by mdb-ii on 15-1-30.
 */
public class ListJson <T>{
    private List<T> data;
    private int  status;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
