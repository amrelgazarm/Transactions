package com.qceccenter.qcec.viewmodels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.qceccenter.qcec.models.Profile;
import com.qceccenter.qcec.repositories.ProfileRepository;

public class ProfileViewModel extends ViewModel {
    private ProfileRepository mProfileRepo;

    public void init() {
        if (mProfileRepo == null)
            mProfileRepo = ProfileRepository.getInstance();
    }

    public LiveData<Profile> getUserInfo(Context context, String accessToken) {
        return mProfileRepo.getUserInfo(context, accessToken);
    }
}
