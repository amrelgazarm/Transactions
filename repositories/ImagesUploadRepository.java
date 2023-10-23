package com.qceccenter.qcec.repositories;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.qceccenter.qcec.R;
import com.qceccenter.qcec.models.VisitSubmission;
import com.qceccenter.qcec.ui.LoginActivity;
import com.qceccenter.qcec.utils.retrofit.QCECClient;
import com.qceccenter.qcec.utils.retrofit.QCECService;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImagesUploadRepository {
    private static ImagesUploadRepository instance;
    private List<MultipartBody.Part> attachmentsList;
    private static int status = 0;
    private static int uploaded = 0;
    private static int attachmentListSize = 1;

    public static ImagesUploadRepository getInstance() {
        if (instance == null)
            return new ImagesUploadRepository();
        return instance;
    }

    public void uploadVisitAttachments(int visitID, String accessToken, Context context, List<MultipartBody.Part> attachmentsList, ProgressBar progressBar, TextView percentageTV, TextView attachCountTV) {
        this.attachmentsList = attachmentsList;
        QCECService qcecService = QCECClient.getQCECService();
        attachmentListSize = attachmentsList.size();
        submitRequest(context, accessToken, visitID, progressBar, percentageTV, attachCountTV, qcecService);
    }

    private void submitRequest(Context context, String accessToken, int visitID, ProgressBar progressBar, TextView percentageTV, TextView attachCountTV, QCECService qcecService) {
        Call<VisitSubmission> call = qcecService.submitImages(accessToken, visitID, getChunk());
        call.enqueue(new Callback<VisitSubmission>() {
            @Override
            public void onResponse(Call<VisitSubmission> call, Response<VisitSubmission> response) {
                if (response.code() == 201) {
                    status += (int) ((2.0 / attachmentListSize) * 100);
                    percentageTV.setText(Integer.toString(status) + "%");
                    progressBar.setProgress(status);
                    if (status >= 99) {
                        attachCountTV.setText(Integer.toString(attachmentListSize) + "/" + Integer.toString(attachmentListSize));
                        percentageTV.setText("100%");
                        if (status == 99)
                            progressBar.setProgress(100);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ((Activity) context).finish();
                            }
                        }, 1000);
                        Toast.makeText(context, context.getString(R.string.attachments_submitted_successfully_msg), Toast.LENGTH_LONG).show();
                    } else {
                        uploaded += 2;
                        attachCountTV.setText(Integer.toString(uploaded) + "/" + Integer.toString(attachmentListSize));
                        percentageTV.setText(Integer.toString(status) + "%");
                        submitRequest(context, accessToken, visitID, progressBar, percentageTV, attachCountTV, qcecService);
                    }

                } else if (response.code() == 401) {
                    Toast.makeText(context, context.getString(R.string.loginRepo_unauthorized_msg), Toast.LENGTH_LONG).show();
                    context.startActivity(new Intent(context, LoginActivity.class));
                    ((Activity) context).finish();
                } else {
                    Toast.makeText(context, context.getString(R.string.loginRepo_error_msg), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<VisitSubmission> call, Throwable t) {
//                Log.e("Upload Images", t.getMessage());
            }
        });
    }

    private List<MultipartBody.Part> getChunk() {
        List<MultipartBody.Part> tempList;
        if (this.attachmentsList.size() >= 2) {
            tempList = new ArrayList<>(this.attachmentsList.subList(0, 2));
            for (int i = 0; i < 2; i++) {
                this.attachmentsList.remove(0);
            }
            return tempList;
        } else {
            tempList = new ArrayList<>(this.attachmentsList.subList(0, this.attachmentsList.size()));
            this.attachmentsList.clear();
            return tempList;
        }
    }
}
