package com.qceccenter.qcec.viewmodels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.qceccenter.qcec.models.VisitSubmission;
import com.qceccenter.qcec.repositories.ImagesUploadRepository;
import com.qceccenter.qcec.repositories.VisitSubmissionRepository;

import java.util.List;

import okhttp3.MultipartBody;

public class NewVisitViewModel extends ViewModel {
    private VisitSubmissionRepository mVisitSubmissionRepo;
    private ImagesUploadRepository mImageUploadRepo;

    public void init() {
        if (mVisitSubmissionRepo == null)
            mVisitSubmissionRepo = VisitSubmissionRepository.getInstance();

        if (mImageUploadRepo == null)
            mImageUploadRepo = ImagesUploadRepository.getInstance();
    }

    public LiveData<VisitSubmission> submitVisit(Context context, String accessToken, int transactionID, String workDesc, String notes, double latitude, double longtude, int sendToOwner, int sendToCont, NewVisitViewModel viewModel, List<MultipartBody.Part> attachmentList) {
        return mVisitSubmissionRepo.submitVisit(context, accessToken, transactionID, workDesc, notes, latitude, longtude, sendToOwner, sendToCont, viewModel, attachmentList);
    }

//    public LiveData<VisitSubmission> uploadImages(Context context, int visitID, String accessToken, List<MultipartBody.Part> attachmentList) {
//        return mImageUploadRepo.uploadVisitAttachments(visitID, accessToken, context, attachmentList);
//    }
}
