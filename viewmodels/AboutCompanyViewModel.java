package com.qceccenter.qcec.viewmodels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.qceccenter.qcec.models.CompanyInfo;
import com.qceccenter.qcec.repositories.CompanyInfoRepository;

public class AboutCompanyViewModel extends ViewModel {
    private CompanyInfoRepository mCompanyInfoRepo;

    public void init(){
        if(mCompanyInfoRepo == null)
            mCompanyInfoRepo = CompanyInfoRepository.getInstance();
    }

    public LiveData<CompanyInfo> getCompanyInfo(Context context){
        return mCompanyInfoRepo.getCompanyInfo(context);
    }
}
