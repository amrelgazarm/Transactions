package com.qceccenter.qcec.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;

import com.qceccenter.qcec.R;
import com.qceccenter.qcec.databinding.ActivityAboutCompanyBinding;
import com.qceccenter.qcec.models.CompanyInfo;
import com.qceccenter.qcec.utils.LocaleHelper;
import com.qceccenter.qcec.viewmodels.AboutCompanyViewModel;

public class AboutCompanyActivity extends AppCompatActivity {
    private ActivityAboutCompanyBinding binding;
    private AboutCompanyViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAboutCompanyBinding.inflate(getLayoutInflater());
        View rootView = binding.getRoot();
        setContentView(rootView);

        getSupportActionBar().hide();

        binding.backImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AboutCompanyActivity.this.finish();
            }
        });

        mViewModel = new ViewModelProvider(this).get(AboutCompanyViewModel.class);
        mViewModel.init();
        mViewModel.getCompanyInfo(this).observe(this, new Observer<CompanyInfo>() {
            @Override
            public void onChanged(CompanyInfo companyInfo) {
                if (LocaleHelper.getLanguage(AboutCompanyActivity.this).contains("en")) {
                    binding.introductionTitleTv.setText(companyInfo.getData().getIntroduction().getTitleEn());
                    binding.introductionTv.setText(companyInfo.getData().getIntroduction().getDescriptionEn());
                    binding.servicesTitleTv.setText(companyInfo.getData().getServices().getTitleEn());
                    binding.servicesTv.setText(companyInfo.getData().getServices().getDescriptionEn());
                } else {
                    binding.introductionTitleTv.setText(companyInfo.getData().getIntroduction().getTitleAr());
                    binding.introductionTv.setText(companyInfo.getData().getIntroduction().getDescriptionAr());
                    binding.servicesTitleTv.setText(companyInfo.getData().getServices().getTitleAr());
                    binding.servicesTv.setText(companyInfo.getData().getServices().getDescriptionAr());
                }
                binding.progressBar4.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!LocaleHelper.getLanguage(this).equals(getString(R.string.locale_check))){
            LocaleHelper.updateLocaleFromContext(this);
            this.recreate();
        }
    }
}