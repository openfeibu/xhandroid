package cn.flyexp.entity;

import java.io.Serializable;

/**
 * Created by admin on 2016/7/22.
 */
public class ApprovalRequest implements Serializable {
    private String token;
    private int targetID;

    public ApprovalRequest(String token, int targetID) {
        this.token = token;
        this.targetID = targetID;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getTargetID() {
        return targetID;
    }

    public void setTargetID(int targetID) {
        this.targetID = targetID;
    }
}
