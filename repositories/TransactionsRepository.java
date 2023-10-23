package com.qceccenter.qcec.repositories;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.qceccenter.qcec.R;
import com.qceccenter.qcec.models.Transactions;
import com.qceccenter.qcec.ui.LoginActivity;
import com.qceccenter.qcec.utils.retrofit.QCECClient;
import com.qceccenter.qcec.utils.retrofit.QCECService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransactionsRepository {
    private static TransactionsRepository instance;
    private Transactions dataset;

    public static TransactionsRepository getInstance() {
        if (instance == null)
            return new TransactionsRepository();
        return instance;
    }

    public MutableLiveData<Transactions> getAllTransactions(String accessToken, Context context) {
        final MutableLiveData<Transactions> data = new MutableLiveData<>();
        QCECService qcecService = QCECClient.getQCECService();
        Call<Transactions> call = qcecService.getAllTransactions(accessToken);
        call.enqueue(new Callback<Transactions>() {
            @Override
            public void onResponse(Call<Transactions> call, Response<Transactions> response) {
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
            public void onFailure(Call<Transactions> call, Throwable t) {
                Toast.makeText(context, "Faliure", Toast.LENGTH_LONG).show();
            }
        });

        return data;
    }

    public MutableLiveData<Transactions> getNewTransactions(Context context, String accessToken) {
        MutableLiveData<Transactions> data = new MutableLiveData<>();
        QCECService qcecService = QCECClient.getQCECService();
        Call<Transactions> call = qcecService.getNewTransactions(accessToken);
        call.enqueue(new Callback<Transactions>() {
            @Override
            public void onResponse(Call<Transactions> call, Response<Transactions> response) {
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
            public void onFailure(Call<Transactions> call, Throwable t) {
                Toast.makeText(context, context.getString(R.string.about_company_error_msg), Toast.LENGTH_LONG).show();
                context.startActivity(new Intent(context, LoginActivity.class));
                ((Activity) context).finish();
            }
        });

        return data;
    }
}
