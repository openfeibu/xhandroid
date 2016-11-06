package cn.flyexp.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by admin on 2016/7/23.
 */
public class UploadFileHelper {
    public static MultipartBody uploadMultipartFile(List<File> list) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        for (File file : list) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
            builder.addFormDataPart("uploadfile[]", file.getName(), requestBody);
        }
        builder.setType(MultipartBody.FORM);
        MultipartBody multipartBody = builder.build();
        return multipartBody;
    }

    public static MultipartBody.Part uploadSingleFile(File file) {
        RequestBody body = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("image", file.getName(), body);
        return part;
    }

    public static List<MultipartBody.Part> uploadFiles(List<File> files) {
        List<MultipartBody.Part> parts = new ArrayList<>();
        for (File file : files) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part part = MultipartBody.Part.create(requestBody);
            parts.add(part);
        }
        return parts;
    }


}
