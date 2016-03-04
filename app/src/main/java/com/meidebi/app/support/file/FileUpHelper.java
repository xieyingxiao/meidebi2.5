package com.meidebi.app.support.file;


public class FileUpHelper {

    public static interface ProgressListener {
        public void transferred(long data);

        public void waitServerResponse();

        public void completed();
    }
}
