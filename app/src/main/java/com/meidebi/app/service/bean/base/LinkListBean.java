package com.meidebi.app.service.bean.base;


import java.util.List;

/**
 * Created by mdb-ii on 15-1-30.
 */
public class LinkListBean<T> {
    private List<T> linklist;

    public List<T> getLinklist() {
        return linklist;
    }

    public void setLinklist(List<T> linklist) {
        this.linklist = linklist;
    }
}

