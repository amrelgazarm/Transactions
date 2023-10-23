package com.qceccenter.qcec.repositories;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.qceccenter.qcec.R;
import com.qceccenter.qcec.models.TransactionDetails;
import com.qceccenter.qcec.ui.LoginActivity;
import com.qceccenter.qcec.utils.retrofit.QCECClient;
import com.qceccenter.qcec.utils.retrofit.QCECService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransactionDetailsRepository {
    private static TransactionDetailsRepository instance;
    private TransactionDetails dataset;

    public static TransactionDetailsRepository getInstance() {
        if (instance == null)
            return new TransactionDetailsRepository();
        return instance;
    }

    public MutableLiveData<TransactionDetails> getTransactionById(Context context, int transactionID, String accessToken) {
        MutableLiveData<TransactionDetails> data = new MutableLiveData<>();
        QCECService qcecService = QCECClient.getQCECService();
        Call<TransactionDetails> call = qcecService.getTransactionByID(transactionID, accessToken);
        call.enqueue(new Callback<TransactionDetails>() {
            @Override
            public void onResponse(Call<TransactionDetails> call, Response<TransactionDetails> response) {
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
            public void onFailure(Call<TransactionDetails> call, Throwable t) {
                Log.e("GetTransactionByID", t.toString());
                Toast.makeText(context, context.getString(R.string.about_company_error_msg), Toast.LENGTH_LONG).show();
                ((Activity) context).finish();
            }
        });
        return data;
    }
}
