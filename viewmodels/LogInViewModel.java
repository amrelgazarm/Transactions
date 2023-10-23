package com.qceccenter.qcec.viewmodels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.qceccenter.qcec.models.Authentication;
import com.qceccenter.qcec.repositories.SignInRepository;

public class LogInViewModel extends ViewModel {
    private SignInRepository mSignInRepo;

    public void init() {
        if (mSignInRepo == null)
            mSignInRepo = SignInRepository.getInstance();
    }

    public LiveData<Authentication> logIn(String email, String password, Context context) {
        return mSignInRepo.signIn(email, password, context);
    }
}
