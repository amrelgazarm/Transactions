package com.qceccenter.qcec.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.qceccenter.qcec.R;
import com.qceccenter.qcec.databinding.ActivityVisitBinding;
import com.qceccenter.qcec.models.Visit;
import com.qceccenter.qcec.models.VisitDetails;
import com.qceccenter.qcec.ui.adpaters.AttachmentsRVAdapter;
import com.qceccenter.qcec.utils.LocaleHelper;
import com.qceccenter.qcec.viewmodels.VisitViewModel;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class VisitActivity extends AppCompatActivity implements AttachmentsRVAdapter.AttachmentCardClickListener {
    private ActivityVisitBinding binding;
    private VisitViewModel mVisitViewModel;
    private Visit mVisit;
    private AttachmentsRVAdapter mAttachmentsRVAdapter;
    private String USER_ACCESS_TOKEN;
    private int mVisitID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVisitBinding.inflate(getLayoutInflater());
        View rootView = binding.getRoot();
        setContentView(rootView);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        LocaleHelper.setLocale(this, LocaleHelper.getLanguage(this));
        mVisitViewModel.getVisitById(mVisitID, this, USER_ACCESS_TOKEN).observe(this, new Observer<VisitDetails>() {
            @Override
            public void onChanged(VisitDetails visitDetails) {
                mVisit = visitDetails.getVisitDetails().getVisitDetails();
                updateViews();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        getSupportActionBar().hide();
//        LocaleHelper.updateLocaleFromContext(this);

        if(!LocaleHelper.getLanguage(this).equals(getString(R.string.locale_check))){
            LocaleHelper.updateLocaleFromContext(this);
            this.recreate();
        }

        binding.visitBackImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VisitActivity.this.finish();
            }
        });

        mVisitViewModel = new ViewModelProvider(this).get(VisitViewModel.class);
        mVisitViewModel.init();

        Intent intent = getIntent();
        if (intent.hasExtra(getString(R.string.visit_ID_intent_key))) {
            mVisitID = intent.getIntExtra(getString(R.string.visit_ID_intent_key), 0);
        }

        SharedPreferences pref = getSharedPreferences(getString(R.string.auth_shared_preference), MODE_PRIVATE);
        USER_ACCESS_TOKEN = pref.getString(getString(R.string.auth_shared_preference_authToken), "NO Token");

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putInt(getString(R.string.visit_ID_intent_key), this.mVisitID);
    }

    @Override
    public void onRestoreInstanceState(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);
        this.mVisitID = savedInstanceState.getInt(getString(R.string.visit_ID_intent_key), 0);
        if (mVisitID == 0) {
            finish();
        }
    }

    private void updateViews() {
        binding.visitVisitDateTv.setText(this.mVisit.getVisitDate());
        binding.visitAttachmentsTv.setText(Integer.toString(this.mVisit.getAttachedImagesCount()) + " " + getString(R.string.visit_photo));
        binding.workDescriptionTv.setText(mVisit.getWorkDescription());
        binding.notesTv.setText(mVisit.getNotes());
        binding.locationTv.setText(mVisit.getLatitude());
        binding.locationCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Double.parseDouble(mVisit.getLatitude()) != 0.0 && Double.parseDouble(mVisit.getLongitude()) != 0.0) {
                    openGoogleMapsApp();
                } else {
                    Toast.makeText(VisitActivity.this, getString(R.string.no_location), Toast.LENGTH_SHORT).show();
                }
            }
        });

        initAttachmentsRV();
        getGoogleMapsInfo();
        binding.progressBar3.setVisibility(View.GONE);
    }

    private void getGoogleMapsInfo() {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(Double.parseDouble(mVisit.getLatitude()), Double.parseDouble(mVisit.getLongitude()), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
//            addresses = geocoder.getFromLocation(30.0635023,31.3461133, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            if (addresses.size() > 0) {
                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                binding.locationTv.setText(address);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initAttachmentsRV() {
        mAttachmentsRVAdapter = new AttachmentsRVAdapter(this, mVisit.getAttachedImagesURLsList(), this);
        LinearLayoutManager layoutManager = new GridLayoutManager(this, 1);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        binding.attachmentsRV.setLayoutManager(layoutManager);
        binding.attachmentsRV.setHasFixedSize(true);
        binding.attachmentsRV.setAdapter(mAttachmentsRVAdapter);
    }

    private void openGoogleMapsApp() {
//        String uri = String.format(Locale.ENGLISH, "geo:%f,%f", Double.parseDouble(mVisit.getLatitude()), Double.parseDouble(mVisit.getLongitude()));
//        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        String url = "https://www.google.com/maps/search/?api=1&query=" + mVisit.getLatitude() + "," + mVisit.getLongitude();
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    @Override
    public void onAttachmentItemClicked(int itemPosition) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.maximize_attachment_dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ImageView imgView = dialog.findViewById(R.id.dialog_imgView);
        Picasso.with(this).load(mVisit.getAttachedImagesURLsList().get(itemPosition).getImageURL()).into(imgView);
        ImageButton cancelImgBtn = dialog.findViewById(R.id.dialog_cancel_imgBtn);
        cancelImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


}