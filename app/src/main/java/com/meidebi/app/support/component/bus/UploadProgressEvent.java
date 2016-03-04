package com.meidebi.app.support.component.bus;

import com.meidebi.app.service.bean.show.Draft;

/**
 * Created by Administrator on 2015/2/23.
 */
public class UploadProgressEvent {
    public final String id;
    public final int position;
    public final int progress;
    public final Draft draft;
        public UploadProgressEvent(String  id,int position,int prgress, Draft draft){
        this.id=id;
            this.position=position;
            this.progress=prgress;
            this.draft = draft;
    }



}
