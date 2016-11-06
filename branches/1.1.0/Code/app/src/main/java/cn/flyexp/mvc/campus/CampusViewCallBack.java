package cn.flyexp.mvc.campus;

import cn.flyexp.entity.AdRequest;
import cn.flyexp.entity.AssnActivityRequest;
import cn.flyexp.entity.AssnActivityResponse;
import cn.flyexp.entity.AssnInfoRequest;
import cn.flyexp.entity.AssnInfoResponse;
import cn.flyexp.framework.AbstractWindow;

/**
 * Created by txy on 2016/7/17 0017.
 */
public interface CampusViewCallBack extends AbstractWindow.WindowCallBack {

    public void taskEnter();

    public void businessEnter();

    public void getAd(AdRequest adRequest);

    public void assnActivityEnter();

    public void getHotAssnActivity();

    public void getHotAssnInfo();

    public void shopEnter();

    public void assnEnter();

    public void infoDetailEnter(AssnInfoResponse.AssnInfoResponseData assnInfoResponseData);

    public void activityDetailEnter(AssnActivityResponse.AssnActivityResponseData assnActivityResponseData);
}
