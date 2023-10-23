package com.qceccenter.qcec.repositories;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.qceccenter.qcec.R;
import com.qceccenter.qcec.models.Profile;
import com.qceccenter.qcec.ui.LoginActivity;
import com.qceccenter.qcec.utils.retrofit.QCECClient;
import com.qceccenter.qcec.utils.retrofit.QCECService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileRepository {
    private static ProfileRepository instance;
    private Profile dataset;

    public static ProfileRepository getInstance() {
        if(instance == null)
            instance = new ProfileRepository();
        return instance;
    }

    public MutableLiveData<Profile> getUserInfo(Context context, String accessToken){
        MutableLiveData<Profile> data = new MutableLiveData<>();
        QCECService qcecService = QCECClient.getQCECService();
        Call<Profile> call = qcecService.getUserInfo(accessToken);
        call.enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {
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
            public void onFailure(Call<Profile> call, Throwable t) {
                Toast.makeText(context, context.getString(R.string.about_company_error_msg), Toast.LENGTH_LONG).show();
                ((Activity) context).finish();
            }
        });
        return data;
    }
}
