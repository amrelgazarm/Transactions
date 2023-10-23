package com.qceccenter.qcec.repositories;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.qceccenter.qcec.R;
import com.qceccenter.qcec.models.ContactUs;
import com.qceccenter.qcec.ui.LoginActivity;
import com.qceccenter.qcec.utils.retrofit.QCECClient;
import com.qceccenter.qcec.utils.retrofit.QCECService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactUsRepository {
    private static ContactUsRepository instance;
    private ContactUs dataset;

    public static ContactUsRepository getInstance() {
        if (instance == null)
            return new ContactUsRepository();
        return instance;
    }

    public MutableLiveData<ContactUs> getContactInfo(Context context, String accessToken) {
        MutableLiveData<ContactUs> data = new MutableLiveData<>();
        QCECService qcecService = QCECClient.getQCECService();
        Call<ContactUs> call = qcecService.getContactInfo(accessToken);
        call.enqueue(new Callback<ContactUs>() {
            @Override
            public void onResponse(Call<ContactUs> call, Response<ContactUs> response) {
                if (response.code() == 200) {
                    dataset = response.body();
                    data.setValue(dataset);
                } else if (response.code() == 401) {
                    Toast.makeText(context, context.getString(R.string.loginRepo_unauthorized_msg), Toast.LENGTH_LONG).show();
                    context.startActivity(new Intent(context, LoginActivity.class));
                    ((Activity) context).finish();
                } else {
                    Toast.makeText(context, context.getString(R.string.loginRepo_error_msg), Toast.LENGTH_LONG).show();
                    ((Activity) context).finish();
                }
            }

            @Override
            public void onFailure(Call<ContactUs> call, Throwable t) {
                Toast.makeText(context, context.getString(R.string.about_company_error_msg), Toast.LENGTH_LONG).show();
                ((Activity) context).finish();
            }
        });

        return data;
    }
}
