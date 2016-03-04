package com.meidebi.app.support.component.bus;

import com.meidebi.app.service.bean.user.UserinfoBean;

/**
 * Created by mdb-ii on 15-3-5.
 */
public class UserInfoRefreshEvent {
    public UserinfoBean userinfoBean;
    public UserInfoRefreshEvent(UserinfoBean userinfoBean){
        this.userinfoBean = userinfoBean;
    }
}
