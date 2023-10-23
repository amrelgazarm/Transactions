package com.qceccenter.qcec.viewmodels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.qceccenter.qcec.models.TransactionDetails;
import com.qceccenter.qcec.repositories.TransactionDetailsRepository;

public class TransactionDetailsViewModel extends ViewModel {
    private TransactionDetailsRepository mTransDetailsRepo;

    public void init() {
        if (mTransDetailsRepo == null)
            mTransDetailsRepo = TransactionDetailsRepository.getInstance();
    }

    public LiveData<TransactionDetails> getTranactionById(Context context, int transactionId, String accessToken) {
        return mTransDetailsRepo.getTransactionById(context, transactionId, accessToken);
    }
}
