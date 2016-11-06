package cn.flyexp.net;


import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by admin on 2016/7/22.
 */
public class StringConverter implements Converter<ResponseBody,String> {
    public static final StringConverter INSTANCE=new StringConverter();

    @Override
    public String convert(ResponseBody value) throws IOException {
        return value.string();
    }
}
