package com.combustiongroup.burntout.network;

/**
 * Created by William Matias on 8/18/16.
 */

public class STResponse<T> {

    String status;
    String message;
    T data;

    public boolean isSuccess() {
        return status.equals("OK");
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    @Override
    public String toString() {
        return "BurntOutResponse{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}