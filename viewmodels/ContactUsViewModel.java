package com.qceccenter.qcec.viewmodels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.qceccenter.qcec.models.ContactUs;
import com.qceccenter.qcec.repositories.ContactUsRepository;

public class ContactUsViewModel extends ViewModel {
    private ContactUsRepository mContactRepo;

    public void init() {
        if (mContactRepo == null)
            mContactRepo = ContactUsRepository.getInstance();
    }

    public LiveData<ContactUs> getContactInfo(Context context, String accessToken) {
        return mContactRepo.getContactInfo(context, accessToken);
    }
}
