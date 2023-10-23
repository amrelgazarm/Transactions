package com.qceccenter.qcec.repositories;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.qceccenter.qcec.R;
import com.qceccenter.qcec.models.SearchResult;
import com.qceccenter.qcec.ui.LoginActivity;
import com.qceccenter.qcec.ui.SearchActivity;
import com.qceccenter.qcec.utils.retrofit.QCECClient;
import com.qceccenter.qcec.utils.retrofit.QCECService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchRepository {
    private static SearchRepository instance;
    private SearchResult dataset;

    public static SearchRepository getInstance() {
        if (instance == null)
            return new SearchRepository();
        return instance;
    }

    public MutableLiveData<SearchResult> search(Context context, String accessToken, String searchStr) {
        MutableLiveData<SearchResult> data = new MutableLiveData<>();
        QCECService qcecService = QCECClient.getQCECService();
        Call<SearchResult> call = qcecService.search(accessToken, searchStr);
        call.enqueue(new Callback<SearchResult>() {
            @Override
            public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                setProgressbarVisibility(context);
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
            public void onFailure(Call<SearchResult> call, Throwable t) {
                setProgressbarVisibility(context);
                Toast.makeText(context, context.getString(R.string.about_company_error_msg), Toast.LENGTH_LONG).show();
            }
        });
        return data;
    }

    private void setProgressbarVisibility(Context context) {
        if (context instanceof SearchActivity) {
            ((SearchActivity) context).getProgressBar().setVisibility(View.GONE);
        }
    }
}
