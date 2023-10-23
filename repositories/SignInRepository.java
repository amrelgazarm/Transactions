package com.qceccenter.qcec.repositories;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.qceccenter.qcec.R;
import com.qceccenter.qcec.models.Authentication;
import com.qceccenter.qcec.ui.LoginActivity;
import com.qceccenter.qcec.utils.retrofit.QCECClient;
import com.qceccenter.qcec.utils.retrofit.QCECService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInRepository {
    private static SignInRepository instance;
    private Authentication dataset;

    public static SignInRepository getInstance() {
        if (instance == null)
            return new SignInRepository();
        return instance;
    }

    public MutableLiveData<Authentication> signIn(String email, String password, final Context context) {
        final MutableLiveData<Authentication> data = new MutableLiveData<>();
        QCECService qcecService = QCECClient.getQCECService();
        Call<Authentication> call = qcecService.signIn(email, password);
        call.enqueue(new Callback<Authentication>() {
            @Override
            public void onResponse(Call<Authentication> call, Response<Authentication> response) {
                if (response.code() == 200) {
                    dataset = response.body();
                    data.setValue(dataset);
                    Toast.makeText(context, context.getString(R.string.loginRepo_successful_msg), Toast.LENGTH_SHORT).show();
                } else if (response.code() == 401) {
                    Toast.makeText(context, context.getString(R.string.loginRepo_unauthorized_msg), Toast.LENGTH_LONG).show();
                    setLogInBtnEnabled(context);
                } else if (response.code() == 422) {
                    Toast.makeText(context, context.getString(R.string.loginRepo_check_user_info), Toast.LENGTH_LONG).show();
                    setLogInBtnEnabled(context);
                } else {
                    Toast.makeText(context, context.getString(R.string.loginRepo_error_msg), Toast.LENGTH_LONG).show();
                    setLogInBtnEnabled(context);
                }
            }

            @Override
            public void onFailure(Call<Authentication> call, Throwable t) {
                Toast.makeText(context, context.getString(R.string.loginRepo_failed_msg), Toast.LENGTH_LONG).show();
                setLogInBtnEnabled(context);
            }
        });
        return data;
    }

    private void setLogInBtnEnabled(Context context) {
        if (context instanceof LoginActivity) {
            ((LoginActivity) context).getLogInBtnRef().setEnabled(true);
            ((LoginActivity) context).getProgressBarRef().setVisibility(View.GONE);
        }
    }
}
