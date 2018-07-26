package com.konka.kktripclient.bean;

/**
 * Created by HP on 2017-6-1.
 */

public class ProgressBean {
    private int progress;
    private boolean isError = false;
    private int errorType = -1;
    private boolean isCompleted = false;
    public static final int ERROR_MD5 = 0;
    public static final int ERROR_DOWNLOAD = 1;

    public ProgressBean(){}

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public int getErrorType() {
        return errorType;
    }

    public void setErrorType(int errorType) {
        this.errorType = errorType;
    }

    public boolean isError() {
        return isError;
    }

    public void setError(boolean error) {
        isError = error;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}
