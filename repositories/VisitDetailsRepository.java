package com.qceccenter.qcec.repositories;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.qceccenter.qcec.R;
import com.qceccenter.qcec.models.VisitDetails;
import com.qceccenter.qcec.ui.LoginActivity;
import com.qceccenter.qcec.utils.retrofit.QCECClient;
import com.qceccenter.qcec.utils.retrofit.QCECService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VisitDetailsRepository {
    public static VisitDetailsRepository instance;
    private VisitDetails dataset;

    public static VisitDetailsRepository getInstance() {
        if (instance == null)
            return new VisitDetailsRepository();
        return instance;
    }

    public MutableLiveData<VisitDetails> getVisitByID(Context context, int visitID, String accessToken) {
        MutableLiveData<VisitDetails> data = new MutableLiveData<>();
        QCECService qcecService = QCECClient.getQCECService();
        Call<VisitDetails> call = qcecService.getVisitByID(visitID, accessToken);
        call.enqueue(new Callback<VisitDetails>() {
            @Override
            public void onResponse(Call<VisitDetails> call, Response<VisitDetails> response) {
                if (response.code() == 200) {
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
            public void onFailure(Call<VisitDetails> call, Throwable t) {
                Toast.makeText(context, context.getString(R.string.about_company_error_msg), Toast.LENGTH_LONG).show();
                ((Activity) context).finish();
            }
        });
        return data;
    }
}
