package com.qceccenter.qcec.services;

import android.app.Dialog;
import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.qceccenter.qcec.R;
import com.qceccenter.qcec.models.Attachments;
import com.qceccenter.qcec.repositories.ImagesUploadRepository;

public class UploadAttachmentsIntentService extends IntentService {
    private static final String ACTION_UPLOAD_IMAGES = "com.example.qcec.services.action.UPLOAD_IMAGES";

    private static final String EXTRA_PARAM_VISIT_ID = "com.example.qcec.services.extra.VISIT_ID";
    private static final String EXTRA_PARAM_ACCESS_TOKEN = "com.example.qcec.services.extra.ACCESS_TOKEN";
    private static final String EXTRA_PARAM_ATTACHMENT_LIST = "com.example.qcec.services.extra.ATTACHMENT_LIST";

    private static Attachments attachmentsObj;
    private static Context visitActivityContext;

    private static ProgressBar progressBar;
    private static TextView percentageTV;
    private static TextView attachmentCountTV;

    public UploadAttachmentsIntentService() {
        super("UploadAttachmentsIntentService");
    }

    public static void startActionUploadImages(Context context, int visitID, String accessToken, Attachments attachments) {
        Intent intent = new Intent(context, UploadAttachmentsIntentService.class);
        intent.setAction(ACTION_UPLOAD_IMAGES);
        intent.putExtra(EXTRA_PARAM_VISIT_ID, visitID);
        intent.putExtra(EXTRA_PARAM_ACCESS_TOKEN, accessToken);
        attachmentsObj = attachments;
        visitActivityContext = context;
        createUploadDialog(context);
        context.startService(intent);
//        context.sendBroadcast(intent);
    }

    private static void createUploadDialog(Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.upload_dialog);
        progressBar = dialog.findViewById(R.id.upload_progressbar);
        percentageTV = dialog.findViewById(R.id.percentage_tv);
        attachmentCountTV = dialog.findViewById(R.id.attachments_count_tv);
        attachmentCountTV.setText("0/" + Integer.toString(attachmentsObj.getAttachmentList().size()));
        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UPLOAD_IMAGES.equals(action)) {
                int visitID = intent.getIntExtra(EXTRA_PARAM_VISIT_ID, 0);
                String accessToken = intent.getStringExtra(EXTRA_PARAM_ACCESS_TOKEN);
                Attachments attachments = (Attachments) intent.getSerializableExtra(EXTRA_PARAM_ATTACHMENT_LIST);
                handleActionUploadImages(visitID, accessToken, attachments);
            }
        }
    }

    private void handleActionUploadImages(int visitID, String accessToken, Attachments attachments) {
        ImagesUploadRepository.getInstance().uploadVisitAttachments(visitID, accessToken, visitActivityContext, attachmentsObj.getAttachmentList(), progressBar, percentageTV, attachmentCountTV);
    }
}