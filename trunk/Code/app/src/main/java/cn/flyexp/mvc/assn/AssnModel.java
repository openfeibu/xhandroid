package cn.flyexp.mvc.assn;

import java.io.File;
import java.util.List;

import cn.flyexp.entity.AssnActivityPublishRequest;
import cn.flyexp.entity.CommonResponse;
import cn.flyexp.entity.EncodeData;
import cn.flyexp.entity.AssnActivityRequest;
import cn.flyexp.entity.AssnActivityResponse;
import cn.flyexp.entity.AssnInfoPublishRequest;
import cn.flyexp.entity.AssnInfoRequest;
import cn.flyexp.entity.AssnInfoResponse;
import cn.flyexp.entity.AssnNoticePublishRequest;
import cn.flyexp.entity.AssnProfilePublishRequest;
import cn.flyexp.entity.AssnProfileRequest;
import cn.flyexp.entity.AssnProfileResponse;
import cn.flyexp.entity.TokenToJson;
import cn.flyexp.entity.UploadImageResponse;
import cn.flyexp.net.NetWork;
import cn.flyexp.net.NetWorkService;
import cn.flyexp.util.GsonUtil;
import cn.flyexp.util.UploadFileHelper;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by txy on 2016/7/25 0025.
 */
public class AssnModel {

    private AssnModelCallBack callBack;
    private NetWorkService service;

    public AssnModel(AssnModelCallBack callBack) {
        this.callBack = callBack;
        service = NetWork.getInstance().getService();
    }

    public void getActivityList(AssnActivityRequest socialActivityRequest) {
        if (socialActivityRequest == null) {
            return;
        }
        String data = GsonUtil.toJson(socialActivityRequest);
        Call<EncodeData> respond = service.getActivityList(data);
        respond.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    AssnActivityResponse socialActivityResponse =
                            GsonUtil.fromJson(response.body().getData(), AssnActivityResponse
                                    .class);
                    callBack.assnActivityListResponse(socialActivityResponse);
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
                callBack.assnActivityListResponse(null);
            }
        });

    }

    public void getInformationList(AssnInfoRequest socialInfoRequest) {
        if (socialInfoRequest == null) {
            return;
        }
        String data = GsonUtil.toJson(socialInfoRequest);
        Call<EncodeData> respond = service.getInformationList(data);
        respond.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    AssnInfoResponse socialInfoResponse = GsonUtil
                            .fromJson(response.body().getData(), AssnInfoResponse.class);
                    callBack.assnInfoListResponse(socialInfoResponse);
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
                callBack.assnInfoListResponse(null);
            }
        });

    }

    public void getProfile(AssnProfileRequest assnProfileRequest, final int which) {
        if (assnProfileRequest == null) {
            return;
        }
        String data = GsonUtil.toJson(assnProfileRequest);
        Call<EncodeData> respond = service.getProfile(data);
        respond.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    AssnProfileResponse assnProfileResponse = GsonUtil
                            .fromJson(response.body().getData(), AssnProfileResponse.class);
                    callBack.assnProfileResponse(assnProfileResponse,which);
                } else {
                    callBack.assnProfileResponse(null,which);
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
                callBack.assnProfileResponse(null,which);
            }
        });

    }


    public void createInformation(AssnInfoPublishRequest socialInfoPublishRequest) {
        if (socialInfoPublishRequest == null) {
            return;
        }
        Call<EncodeData> call = service.createInformation(socialInfoPublishRequest);
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    CommonResponse commonResponse = GsonUtil.fromJson(response.body().getData(), CommonResponse.class);
                    callBack.assnInfoPublishResponse(commonResponse);
                } else {
                    callBack.assnInfoPublishResponse(null);
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
                callBack.assnInfoPublishResponse(null);
            }
        });
    }

    public void createMessage(AssnNoticePublishRequest socialNoticePublishRequest) {
        if (socialNoticePublishRequest == null) {
            return;
        }
        Call<EncodeData> call = service.createMessage(socialNoticePublishRequest);
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    CommonResponse commonResponse = GsonUtil.fromJson(response.body().getData(), CommonResponse.class);
                    callBack.assnNoticePublishResponse(commonResponse);
                } else {
                    callBack.assnNoticePublishResponse(null);
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
                callBack.assnNoticePublishResponse(null);
            }
        });
    }

    public void createActivity(AssnActivityPublishRequest assnActivityPublishRequest) {
        if (assnActivityPublishRequest == null) {
            return;
        }
        Call<EncodeData> call = service.createActivity(assnActivityPublishRequest);
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    CommonResponse commonResponse =
                            GsonUtil.fromJson(response.body()
                                    .getData(), CommonResponse.class);
                    callBack.assnActivityPublishResponse(commonResponse);
                } else {
                    callBack.assnActivityPublishResponse(null);
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
                callBack.assnActivityPublishResponse(null);
            }
        });
    }

    public void setProfile(AssnProfilePublishRequest socialProfilePublishRequest) {
        if (socialProfilePublishRequest == null) {
            return;
        }
        Call<EncodeData> call = service.setProfile(socialProfilePublishRequest);
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    CommonResponse commonResponse = GsonUtil.fromJson(response.body().getData(), CommonResponse.class);
                    callBack.assnProfilePublishResponse(commonResponse);
                } else {
                    callBack.assnProfilePublishResponse(null);
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
                callBack.assnProfilePublishResponse(null);
            }
        });
    }

    public void uploadImageInfo(String token, List<File> files) {
        MultipartBody body = UploadFileHelper.uploadMultipartFile(files);
        Call<EncodeData> call = service.uploadImageInfo(body, GsonUtil.toJson(new TokenToJson(token)));
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    UploadImageResponse uploadImageResponse = GsonUtil
                            .fromJson(response.body()
                                    .getData(), UploadImageResponse.class);
                    callBack.uploadInfoImageResponse(uploadImageResponse);
                } else {
                    callBack.uploadInfoImageResponse(null);
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
                callBack.uploadInfoImageResponse(null);
            }
        });

    }

    public void uploadImageActi(String token, List<File> files) {
        MultipartBody body = UploadFileHelper.uploadMultipartFile(files);
        Call<EncodeData> call = service.uploadImageActi(body, GsonUtil.toJson(new TokenToJson(token)));
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    UploadImageResponse uploadImageResponse = GsonUtil
                            .fromJson(response.body()
                                    .getData(), UploadImageResponse.class);
                    callBack.uploadActiImageResponse(uploadImageResponse);
                } else {
                    callBack.uploadActiImageResponse(null);
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
                callBack.uploadActiImageResponse(null);
            }
        });

    }


}
