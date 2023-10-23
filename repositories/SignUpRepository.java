package com.qceccenter.qcec.repositories;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.qceccenter.qcec.R;
import com.qceccenter.qcec.models.VisitSubmission;
import com.qceccenter.qcec.ui.LoginActivity;
import com.qceccenter.qcec.utils.retrofit.QCECClient;
import com.qceccenter.qcec.utils.retrofit.QCECService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpRepository {
    private static SignUpRepository instance;
    private VisitSubmission dataset;

    public static SignUpRepository getInstance() {
        if (instance == null)
            instance = new SignUpRepository();
        return instance;
    }

    public MutableLiveData<VisitSubmission> signup(Context context, String username, String phone, String nationalID, String password) {
        MutableLiveData<VisitSubmission> data = new MutableLiveData<>();
        QCECService qcecService = QCECClient.getQCECService();
        Call<VisitSubmission> call = qcecService.signup(username, password, phone, nationalID);
        call.enqueue(new Callback<VisitSubmission>() {
            @Override
            public void onResponse(Call<VisitSubmission> call, Response<VisitSubmission> response) {
                if (response.code() == 201) {
                    dataset = response.body();
                    data.setValue(dataset);
                } else if (response.code() == 401) {
                    Toast.makeText(context, context.getString(R.string.loginRepo_unauthorized_msg), Toast.LENGTH_LONG).show();
                    context.startActivity(new Intent(context, LoginActivity.class));
                    ((Activity) context).finish();
                } else {
                    Toast.makeText(context, context.getString(R.string.loginRepo_error_msg), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<VisitSubmission> call, Throwable t) {
                Toast.makeText(context, context.getString(R.string.about_company_error_msg), Toast.LENGTH_LONG).show();
                ((Activity) context).finish();
            }
        });
        return data;
    }
}
