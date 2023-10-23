package com.qceccenter.qcec.viewmodels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.qceccenter.qcec.models.VisitDetails;
import com.qceccenter.qcec.repositories.VisitDetailsRepository;

public class VisitViewModel extends ViewModel {
    private VisitDetailsRepository mVisitDetailsRepo;

    public void init() {
        if (mVisitDetailsRepo == null)
            mVisitDetailsRepo = VisitDetailsRepository.getInstance();
    }

    public LiveData<VisitDetails> getVisitById(int visitID, Context context, String accessToken) {
        return mVisitDetailsRepo.getVisitByID(context, visitID, accessToken);
    }
}
