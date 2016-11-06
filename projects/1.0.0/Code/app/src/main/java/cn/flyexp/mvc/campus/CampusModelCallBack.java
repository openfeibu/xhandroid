package cn.flyexp.mvc.campus;

import cn.flyexp.entity.AdResponse;
import cn.flyexp.entity.AssnActivityResponse;
import cn.flyexp.entity.AssnInfoResponse;

/**
 * Created by txy on 2016/7/25 0025.
 */
public interface CampusModelCallBack {

    public void AdListResponse(AdResponse adResponse);

    public void assnActivityResponse(AssnActivityResponse socialActivityResponse);

    public void assnInfoResponse(AssnInfoResponse socialInfoResponse);
}
