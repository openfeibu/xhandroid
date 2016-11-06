package cn.flyexp.net;

import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

import cn.flyexp.FBApplication;
import cn.flyexp.util.Constants;
import cn.flyexp.entity.EncodeData;
import cn.flyexp.util.CommonUtil;
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
 * Created by guo on 2016/5/24.
 * Modified by txy on 2016/8/4.
 */
public class NetWork {

    private NetWorkService service;
    private static NetWork instance;
    private Gson gson = new GsonBuilder().disableHtmlEscaping().create();

    public static NetWork getInstance() {
        if (instance == null) {
            synchronized (NetWork.class) {
                if (instance == null) {
                    instance = new NetWork();
                }
            }
        }
        return instance;
    }

    private NetWork() {
        HttpLoggingInterceptor httpIntercepter = new HttpLoggingInterceptor();
        httpIntercepter.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(httpIntercepter)
                .addNetworkInterceptor(intercepter)
                .addNetworkInterceptor(new AddCookiesInterceptor())
                .addNetworkInterceptor(new ReceivedCookiesInterceptor())
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.SERVER_URL)
                .client(client)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(StringConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .build();
        service = retrofit.create(NetWorkService.class);
    }


    public NetWorkService getService() {
        return service;
    }

    Interceptor intercepter = new Interceptor() {
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
            String aa = new String(bs, "utf-8");

            String value = CommonUtil.encode(aa);
            EncodeData data = new EncodeData(value);
            String requestElement = gson.toJson(data);
            RequestBody newRb = RequestBody.create(old.contentType(), requestElement.getBytes("utf-8"));
            Request newRequest = requestBody.newBuilder().url(requestBody.url()).header("Content-Type", requestBody.headers().get("Content-Type")).header("Content-Length", "" + requestElement.getBytes().length).post(newRb).build();
            Response response = chain.proceed(newRequest);
            return response;
        }
    };


    /**
     * 发送的拦截器
     */
    public class AddCookiesInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request.Builder builder = chain.request().newBuilder();
            HashSet<String> preferences = (HashSet) PreferenceManager.getDefaultSharedPreferences(FBApplication.APPLICATION_CONTEXT).getStringSet("cookies", new HashSet<String>());
            for (String cookie : preferences) {
                builder.addHeader("Cookie", cookie);
                Log.v("OkHttp", "Adding Header: " + cookie); // This is done so I know which headers are being added; this interceptor is used after the normal logging of OkHttp
            }

            return chain.proceed(builder.build());
        }
    }

    /**
     * 接收的拦截器
     */
    public class ReceivedCookiesInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Response originalResponse = chain.proceed(chain.request());

            if (!originalResponse.headers("Set-Cookie").isEmpty()) {
                HashSet<String> cookies = new HashSet<>();

                for (String header : originalResponse.headers("Set-Cookie")) {
                    cookies.add(header);
                }

                PreferenceManager.getDefaultSharedPreferences(FBApplication.APPLICATION_CONTEXT).edit()
                        .putStringSet("cookies", cookies)
                        .apply();
            }

            return originalResponse;
        }
    }

}
