package com.meidebi.app.base.error;

import com.meidebi.app.R;
import android.content.res.Resources;
import android.text.TextUtils;

import com.meidebi.app.XApplication;

 
public class XException extends Exception {

 

    private String error;
     private String oriError;
    private int error_code;

    public String getError() {

        String result;

        if (!TextUtils.isEmpty(error)) {
            result = error;
        } else {

            String name = "code" + error_code;
            int i = XApplication.getInstance().getResources()
                    .getIdentifier(name, "string", XApplication.getInstance().getPackageName());

            try {
                result = XApplication.getInstance().getString(i);

            } catch (Resources.NotFoundException e) {

                if (!TextUtils.isEmpty(oriError)) {
                    result = oriError;
                } else {
                    result = XApplication.getInstance().getString(R.string.unknown_error_error_code) + error_code;
                }
            }
        }

        return result;
    }

    @Override
    public String getMessage() {
        return getError();
    }


    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public int getError_code() {
        return error_code;
    }

    public XException() {

    }

    public XException(String detailMessage) {
        error = detailMessage;
    }

    public XException(String detailMessage, Throwable throwable) {
        error = detailMessage;
    }


    public void setOriError(String oriError) {
        this.oriError = oriError;
    }

}
