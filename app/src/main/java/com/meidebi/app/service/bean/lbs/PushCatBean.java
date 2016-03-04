package com.meidebi.app.service.bean.lbs;

import java.util.List;

public class PushCatBean {
    private int localstock;
    private List<String> cates;

    public int getLocalstock() {
        return localstock;
    }

    public void setLocalstock(int localstock) {
        this.localstock = localstock;
    }

    public List<String> getCates() {
        return cates;
    }

    public void setCates(List<String> cates) {
        this.cates = cates;
    }

}
