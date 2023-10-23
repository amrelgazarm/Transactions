package com.qceccenter.qcec.viewmodels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.qceccenter.qcec.models.Transactions;
import com.qceccenter.qcec.repositories.TransactionsRepository;

public class HomePagerViewModel extends ViewModel {
    private TransactionsRepository mTransactionsRepo;

    public void init() {
        if (mTransactionsRepo == null)
            mTransactionsRepo = TransactionsRepository.getInstance();
    }

    public LiveData<Transactions> getAllTransactions(String accessToken, Context context) {
        return mTransactionsRepo.getAllTransactions(accessToken, context);
    }

    public LiveData<Transactions> getNewTransactions(String accessToken, Context context){
        return  mTransactionsRepo.getNewTransactions(context, accessToken);
    }
}
