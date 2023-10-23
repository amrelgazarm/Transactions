package com.qceccenter.qcec.repositories;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.qceccenter.qcec.R;
import com.qceccenter.qcec.models.CompanyInfo;
import com.qceccenter.qcec.utils.retrofit.QCECClient;
import com.qceccenter.qcec.utils.retrofit.QCECService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompanyInfoRepository {
    private static CompanyInfoRepository instance;
    private CompanyInfo dataset;

    public static CompanyInfoRepository getInstance() {
        if (instance == null)
            return new CompanyInfoRepository();
        return instance;
    }

    public MutableLiveData<CompanyInfo> getCompanyInfo(Context context) {
        MutableLiveData<CompanyInfo> data = new MutableLiveData<>();
        QCECService qcecService = QCECClient.getQCECService();
        Call<CompanyInfo> call = qcecService.getCompanyInfo();
        call.enqueue(new Callback<CompanyInfo>() {
            @Override
            public void onResponse(Call<CompanyInfo> call, Response<CompanyInfo> response) {
                if (response.code() == 200) {
                    dataset = response.body();
                    data.setValue(dataset);
                } else {
                    Toast.makeText(context, context.getString(R.string.about_company_error_msg), Toast.LENGTH_LONG).show();
                    ((Activity) context).finish();
                }
            }

            @Override
            public void onFailure(Call<CompanyInfo> call, Throwable t) {
                Toast.makeText(context, context.getString(R.string.about_company_error_msg), Toast.LENGTH_LONG).show();
                ((Activity) context).finish();
            }
        });

        return data;
    }
}
