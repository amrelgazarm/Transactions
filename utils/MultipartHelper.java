package com.qceccenter.qcec.utils;

import androidx.annotation.NonNull;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class MultipartHelper {
    private static final String MULTIPART_FORM_DATA = "multipart/form-data";

    public RequestBody createRequestBody(@NonNull File file) {
        return RequestBody.create(
                MediaType.parse(MULTIPART_FORM_DATA), file);
    }

    @NonNull
    public RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(
                okhttp3.MultipartBody.FORM, descriptionString);
    }

    @NonNull
    public static MultipartBody.Part prepareFilePart(String partName, File file) {
        // create RequestBody instance from file
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }
}
