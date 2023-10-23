package com.qceccenter.qcec.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.qceccenter.qcec.BuildConfig;
import com.qceccenter.qcec.R;
import com.qceccenter.qcec.databinding.ActivityTransactionDetailsBinding;
import com.qceccenter.qcec.databinding.ActivityTransactionsDetails2Binding;
import com.qceccenter.qcec.models.Transaction;
import com.qceccenter.qcec.ui.adpaters.VisitsRVAdapter;
import com.qceccenter.qcec.utils.LocaleHelper;
import com.qceccenter.qcec.utils.LocationHelper;
import com.qceccenter.qcec.viewmodels.TransactionDetailsViewModel;

import java.util.ArrayList;
import java.util.List;

public class TransactionDetailsActivity extends AppCompatActivity implements VisitsRVAdapter.VisitItemClickListener {
    private TransactionDetailsViewModel mtransDetailsViewModel;
    private VisitsRVAdapter mVisitsRVAdapter;
    //    private ActivityTransactionDetailsBinding binding;
    private ActivityTransactionsDetails2Binding binding;
    private Transaction transactionDetails;
    private int mTransactionID = 0;
    private boolean isEng;
    private boolean isTest = false;
    String USER_ACCESS_TOKEN;
    boolean isBackFromNewVisit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        binding = ActivityTransactionDetailsBinding.inflate(getLayoutInflater());
        binding = ActivityTransactionsDetails2Binding.inflate(getLayoutInflater());
        View rootView = binding.getRoot();
        setContentView(rootView);

        getSupportActionBar().hide();

        binding.transDetailsBackImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TransactionDetailsActivity.this.finish();
            }
        });

        mtransDetailsViewModel = new ViewModelProvider(this).get(TransactionDetailsViewModel.class);
        mtransDetailsViewModel.init();

        SharedPreferences pref = getSharedPreferences(getString(R.string.auth_shared_preference), MODE_PRIVATE);
        USER_ACCESS_TOKEN = pref.getString(getString(R.string.auth_shared_preference_authToken), "NO Token");
        isEng = pref.getBoolean(getString(R.string.auth_shared_preference_is_eng), false);

        if (!isEng && !isTest) {
            binding.addNewVisitBtn.setVisibility(View.GONE);
        }

        Intent intent = getIntent();
        if (intent.hasExtra(getString(R.string.transaction_ID_intent_key))) {
            mTransactionID = intent.getIntExtra(getString(R.string.transaction_ID_intent_key), 0);
        }

        binding.addNewVisitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!((LocationManager) getSystemService(LOCATION_SERVICE)).isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    new LocationHelper().requestGPSEnabling(TransactionDetailsActivity.this);
                } else {
                    isBackFromNewVisit = true;
                    Intent intent = new Intent(TransactionDetailsActivity.this, NewVisitActivity.class);
                    intent.putExtra(getString(R.string.transaction_ID_intent_key), mTransactionID);
                    startActivity(intent);
                }
            }
        });

        binding.transDetailsPdfLinkTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (transactionDetails.getPdfFilesList().size() == 0)
                    Toast.makeText(TransactionDetailsActivity.this, getString(R.string.no_pdf_files_toast), Toast.LENGTH_LONG).show();
                else {
                    List<String> filesTitles = new ArrayList<>();
                    for (int i = 0; i < transactionDetails.getPdfFilesList().size(); i++) {
                        filesTitles.add(transactionDetails.getPdfFilesList().get(i).getFileName());
                    }

                    Dialog pdfDialog = new Dialog(TransactionDetailsActivity.this);
//                    pdfDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    pdfDialog.setContentView(R.layout.dialog_pdf_files);

                    TextView fCountTv = pdfDialog.findViewById(R.id.files_count_tv);
                    fCountTv.setText(getString(R.string.fragment_transactions_count) + " " + filesTitles.size());

                    ListView listView = pdfDialog.findViewById(R.id.pdf_list_view);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(pdfDialog.getContext(), R.layout.dialog_pdf_list_item, R.id.pdf_list_tv, filesTitles);
                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Toast.makeText(TransactionDetailsActivity.this, filesTitles.get(position), Toast.LENGTH_LONG).show();
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(transactionDetails.getPdfFilesList().get(position).getFileURL()));
                            startActivity(browserIntent);
                        }
                    });
                    pdfDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    pdfDialog.show();
                }
            }
        });
    }

    void openPdfFile(String fileUri) {
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(fileUri));
        intent.setType("application/pdf");
        PackageManager pm = getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(intent, 0);
        if (activities.size() > 0) {
            startActivity(intent);
        } else {
            // Do something else here. Maybe pop up a Dialog or Toast
        }
    }

    private void updateViews() {
        binding.trancsactionDetailsProjectTv.setText(transactionDetails.getProjectName());
        binding.transDetailsDateTv.setText(transactionDetails.getCreationDate());
        binding.transDetailsContractorTv.setText(transactionDetails.getContractorName());
        binding.transDetailsConPhoneTv.setText(transactionDetails.getContractorPhone());
        binding.transDetailsOwnerTv.setText(transactionDetails.getOwnerName());
        binding.transDetailsOwnerPhoneTv.setText(transactionDetails.getOwnerPhone());
        binding.transDetailsLocationTv.setText(transactionDetails.getLocation());
        binding.engNameTv.setText(transactionDetails.getEngineerName());
        binding.transDetailsVisitCountTv.setText(getString(R.string.transactionsDetails_visitCount) + " : " + Integer.toString(transactionDetails.getVisitsList().size()));
        initVisitRV();
        binding.transDetailsProgressBar.setVisibility(View.GONE);
    }

    private void initVisitRV() {
        mVisitsRVAdapter = new VisitsRVAdapter(this, transactionDetails.getVisitsList(), this);
        LinearLayoutManager layoutManager = new GridLayoutManager(this, 1);
        binding.transDetailsVisitsRV.setLayoutManager(layoutManager);
        binding.transDetailsVisitsRV.setHasFixedSize(true);
        binding.transDetailsVisitsRV.setAdapter(mVisitsRVAdapter);
    }

    @Override
    public void onVisitItemClicked(int itemPosition) {
        Intent intent = new Intent(this, VisitActivity.class);
        intent.putExtra(getString(R.string.visit_ID_intent_key), this.transactionDetails.getVisitsList().get(itemPosition).getVisitID());
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        LocaleHelper.setLocale(this, LocaleHelper.getLanguage(this));
        binding.transDetailsProgressBar.setVisibility(View.VISIBLE);
        updateData();
    }

    private void updateData() {
        mtransDetailsViewModel.getTranactionById(this, mTransactionID, USER_ACCESS_TOKEN).observe(this, transactionDetails -> {
            this.transactionDetails = transactionDetails.getTransactionsDetails().getTransactionDetails();
            this.updateViews();
            binding.transDetailsProgressBar.setVisibility(View.GONE);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
//        LocaleHelper.updateLocaleFromContext(this);
        if (!LocaleHelper.getLanguage(this).equals(getString(R.string.locale_check))) {
            LocaleHelper.updateLocaleFromContext(this);
            this.recreate();
        }
//        updateData();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(getString(R.string.transaction_ID_intent_key), this.mTransactionID);
    }

    @Override
    public void onRestoreInstanceState(@Nullable Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        this.mTransactionID = savedInstanceState.getInt(getString(R.string.transaction_ID_intent_key), 0);
        if (mTransactionID == 0)
            finish();
    }
}