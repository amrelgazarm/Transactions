package com.qceccenter.qcec.viewmodels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.qceccenter.qcec.models.SearchResult;
import com.qceccenter.qcec.repositories.SearchRepository;

public class SearchViewModel extends ViewModel {
    private SearchRepository mSearchRepo;

    public void init(){
        if(mSearchRepo == null)
            mSearchRepo = SearchRepository.getInstance();
    }

    public LiveData<SearchResult> search(Context context, String accessToken, String searchStr){
        return mSearchRepo.search(context, accessToken, searchStr);
    }
}
