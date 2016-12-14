package cn.flyexp.api;

import android.preference.PreferenceManager;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

import cn.flyexp.constants.Config;
import cn.flyexp.constants.Constants;
import cn.flyexp.entity.EncodeData;
import cn.flyexp.util.EncodeUtil;
import cn.flyexp.util.GsonUtil;
import cn.flyexp.util.LogUtil;
import cn.flyexp.util.SharePresUtil;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.BufferedSink;
import okio.Okio;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by tanxinye on 2016/10/23.
 */
public class ApiManager {

    private static ApiManager apiManager;
    private OkHttpClient okHttpClient;
    private Retrofit retrofit;

    private ApiManager() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        okHttpClient = builder.retryOnConnectionFailure(true)
                .connectTimeout(20, TimeUnit.SECONDS)
                .addNetworkInterceptor(encodeInterceptor)
                .addNetworkInterceptor(addCookiesInterceptor)
                .addNetworkInterceptor(receivedCookiesInterceptor)
                .build();
        retrofit = new Retrofit.Builder().baseUrl(Config.SERVER_URL)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static ApiManager getInstance() {
        if (apiManager == null) {
            synchronized (ApiManager.class) {
                if (apiManager == null) {
                    apiManager = new ApiManager();
                }
            }
        }
        return apiManager;
    }

    private Interceptor encodeInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request requestBody = chain.request();
            RequestBody old = requestBody.body();
            if (old == null || old.contentType().type().equals("multipart")) {
                return chain.proceed(chain.request());
            }
            ByteArrayOutputStream iaos = new ByteArrayOutputStream();
            BufferedSink sink = Okio.buffer(Okio.sink(iaos));
            old.writeTo(sink);
            sink.flush();
            byte[] bs = iaos.toByteArray();
            String value = new String(bs, "utf-8");
            try {
                value = EncodeUtil.encode(value);
            } catch (Exception e) {
                e.printStackTrace();
            }
            String requestElement = GsonUtil.getInstance().toJson(new EncodeData(value));
            RequestBody newRb = RequestBody.create(old.contentType(), requestElement.getBytes("utf-8"));
            Request newRequest = requestBody.newBuilder().url(requestBody.url()).header("Content-Type", requestBody.headers().get("Content-Type")).header("Content-Length", "" + requestElement.getBytes().length).post(newRb).build();
            Response response = chain.proceed(newRequest);
            return response;
        }
    };

    private Interceptor addCookiesInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request.Builder builder = chain.request().newBuilder();
            HashSet<String> preferences = (HashSet) SharePresUtil.getStringSet(SharePresUtil.KEY_COOKIE);
            if (preferences != null) {
                for (String cookie : preferences) {
                    builder.addHeader("Cookie", cookie);
                }
            }
            return chain.proceed(builder.build());
        }
    };

    private Interceptor receivedCookiesInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Response originalResponse = chain.proceed(chain.request());
            if (!originalResponse.headers("Set-Cookie").isEmpty()) {
                HashSet<String> cookies = new HashSet<>();
                for (String header : originalResponse.headers("Set-Cookie")) {
                    cookies.add(header);
                }
                SharePresUtil.putStringSet(SharePresUtil.KEY_COOKIE, cookies);
            }
            return originalResponse;
        }
    };

    public static UserService getUserService() {
        return getInstance()._getUserService();
    }

    public static OtherService getOtherService() {
        return getInstance()._getOtherService();
    }

    public static WalletService getWalletService() {
        return getInstance()._getWalletService();
    }

    public static TopicService getTopicService() {
        return getInstance()._getTopicService();
    }

    public static AssnService getAssnService() {
        return getInstance()._getAssnService();
    }

    public static TaskService getTaskService() {
        return getInstance()._getTaskService();
    }

    public TaskService _getTaskService() {
        return retrofit.create(TaskService.class);
    }

    public AssnService _getAssnService() {
        return retrofit.create(AssnService.class);
    }

    public TopicService _getTopicService() {
        return retrofit.create(TopicService.class);
    }

    public WalletService _getWalletService() {
        return retrofit.create(WalletService.class);
    }

    public UserService _getUserService() {
        return retrofit.create(UserService.class);
    }

    public OtherService _getOtherService() {
        return retrofit.create(OtherService.class);
    }
}
