package com.qceccenter.qcec.viewmodels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.qceccenter.qcec.models.VisitSubmission;
import com.qceccenter.qcec.repositories.SignUpRepository;

public class CreateAccountViewModel extends ViewModel {
    private SignUpRepository mRepo;

    public void init() {
        if (mRepo == null)
            mRepo = SignUpRepository.getInstance();
    }

    public LiveData<VisitSubmission> signUp(Context context, String username, String password, String phone, String nationalID){
        return mRepo.signup(context, username, phone, nationalID, password);
    }
}
