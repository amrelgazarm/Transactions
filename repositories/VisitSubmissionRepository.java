package com.qceccenter.qcec.repositories;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.qceccenter.qcec.R;
import com.qceccenter.qcec.models.Attachments;
import com.qceccenter.qcec.models.VisitSubmission;
import com.qceccenter.qcec.services.UploadAttachmentsIntentService;
import com.qceccenter.qcec.ui.LoginActivity;
import com.qceccenter.qcec.utils.retrofit.QCECClient;
import com.qceccenter.qcec.utils.retrofit.QCECService;
import com.qceccenter.qcec.viewmodels.NewVisitViewModel;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VisitSubmissionRepository {
    private static VisitSubmissionRepository instance;
    private VisitSubmission dataset;

    public static VisitSubmissionRepository getInstance() {
        if (instance == null)
            return new VisitSubmissionRepository();
        return instance;
    }

    public MutableLiveData<VisitSubmission> submitVisit(Context context, String accessToken, int transactionID, String workDesc, String notes, double lat, double longitude, int sendToOwner, int sendToCont, NewVisitViewModel newVisitViewModel, List<MultipartBody.Part> attachmentList) {
        MutableLiveData<VisitSubmission> data = new MutableLiveData<>();
        QCECService qcecService = QCECClient.getQCECService();
        Call<VisitSubmission> call = qcecService.addVisit(accessToken, transactionID, workDesc, notes, lat, longitude, sendToOwner, sendToCont);
        call.enqueue(new Callback<VisitSubmission>() {
            @Override
            public void onResponse(Call<VisitSubmission> call, Response<VisitSubmission> response) {
                if (response.code() == 201) {
                    dataset = response.body();
                    data.setValue(dataset);
                    if (attachmentList.size() > 0) {
                        UploadAttachmentsIntentService.startActionUploadImages(context, dataset.getVisitSubmission().getVisitID(), accessToken, new Attachments(attachmentList));
                    } else {
                        ((Activity) context).finish();
                    }
                } else if (response.code() == 401) {
                    Toast.makeText(context, context.getString(R.string.loginRepo_unauthorized_msg), Toast.LENGTH_LONG).show();
                    context.startActivity(new Intent(context, LoginActivity.class));

                } else {
                    Toast.makeText(context, context.getString(R.string.loginRepo_error_msg), Toast.LENGTH_LONG).show();
                    ((Activity) context).finish();
                }
            }

            @Override
            public void onFailure(Call<VisitSubmission> call, Throwable t) {
//                Log.e("New visit submission", t.toString());
                Toast.makeText(context, context.getString(R.string.about_company_error_msg), Toast.LENGTH_LONG).show();
                ((Activity) context).finish();
            }
        });
        return data;
    }

}
