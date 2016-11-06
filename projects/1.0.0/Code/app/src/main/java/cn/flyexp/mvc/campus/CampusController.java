package cn.flyexp.mvc.campus;

import android.os.Message;

import cn.flyexp.entity.AdRequest;
import cn.flyexp.entity.AdResponse;
import cn.flyexp.entity.ResponseCode;
import cn.flyexp.entity.AssnActivityResponse;
import cn.flyexp.entity.AssnInfoResponse;
import cn.flyexp.framework.AbstractController;
import cn.flyexp.framework.MessageIDDefine;
import cn.flyexp.framework.NotifyIDDefine;
import cn.flyexp.framework.NotifyManager;


/**
 * Created by zlk on 2016/3/27.
 */
public class CampusController extends AbstractController implements CampusViewCallBack, NotifyManager.Notify, CampusModelCallBack {

    public CampusWindow campusWindow;
    private CampusModel campusModel;

    public CampusController() {
        super();
        registerNotify(NotifyIDDefine.ON_ACTIVITY_DESTROY, this);
        campusModel = new CampusModel(this);
        campusWindow = new CampusWindow(this);
    }

    @Override
    protected void handleMessage(Message mes) {
    }

    @Override
    protected void registerMessages() {
    }

    @Override
    public void taskEnter() {
        sendMessage(MessageIDDefine.TASK_OPEN);
    }

    @Override
    public void businessEnter() {
        sendMessage(MessageIDDefine.BUSINESS_OPEN);
    }

    @Override
    public void assnActivityEnter() {
        sendMessage(MessageIDDefine.ASSN_OPEN, "intoActivity");
    }

    @Override
    public void shopEnter() {
        sendMessage(MessageIDDefine.SHOP_OPEN);
    }

    @Override
    public void assnEnter() {
        sendMessage(MessageIDDefine.ASSN_OPEN);
    }

    @Override
    public void infoDetailEnter(AssnInfoResponse.AssnInfoResponseData assnInfoResponseData) {
        sendMessage(MessageIDDefine.ASSN_INFO_DETAIL_OPEN, assnInfoResponseData);
    }

    @Override
    public void activityDetailEnter(AssnActivityResponse.AssnActivityResponseData
                                            assnActivityResponseData) {
        sendMessage(MessageIDDefine.ASSN_ACTIVITY_DETAIL_OPEN, assnActivityResponseData);
    }


    @Override
    public void onNotify(Message mes) {
        if (mes.what == NotifyIDDefine.ON_ACTIVITY_DESTROY) {
            if (campusWindow != null) {
                campusWindow.removeRunn();
            }
        }
    }

    @Override
    public void getAd(AdRequest adRequest) {
        campusModel.getAdList(adRequest);
    }

    @Override
    public void getHotAssnActivity() {
        campusModel.getHotAssnActivity();
    }

    @Override
    public void getHotAssnInfo() {
        campusModel.getHotAssnInfo();
    }


    @Override
    public void AdListResponse(AdResponse adResponse) {
        if (adResponse != null) {
            campusWindow.adResponse(adResponse.getData());
        }
    }

    @Override
    public void assnActivityResponse(AssnActivityResponse assnActivityResponse) {
        if (assnActivityResponse == null) {
            return;
        }
        int code = assnActivityResponse.getCode();
        switch (code) {
            case ResponseCode.RESPONSE_200:
                campusWindow.assnActivityResponse(assnActivityResponse.getData());
                break;
            case ResponseCode.RESPONSE_110:
                campusWindow.showToast(assnActivityResponse.getDetail());
                break;
        }
    }

    @Override
    public void assnInfoResponse(AssnInfoResponse assnInfoResponse) {
        if (assnInfoResponse == null) {
            return;
        }
        int code = assnInfoResponse.getCode();
        switch (code) {
            case ResponseCode.RESPONSE_200:
                campusWindow.assnInfoResponse(assnInfoResponse.getData());
                break;
            case ResponseCode.RESPONSE_110:
                campusWindow.showToast(assnInfoResponse.getDetail());
                break;
        }
    }
}
