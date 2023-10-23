package com.qceccenter.qcec.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.qceccenter.qcec.R;
import com.qceccenter.qcec.databinding.ActivityContactUsBinding;
import com.qceccenter.qcec.models.ContactUs;
import com.qceccenter.qcec.utils.LocaleHelper;
import com.qceccenter.qcec.viewmodels.ContactUsViewModel;

public class ContactUsActivity extends AppCompatActivity {
    private ActivityContactUsBinding binding;
    private ContactUsViewModel mViewModel;
    private String USER_ACCESS_TOKEN;
    private String phoneNumberStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityContactUsBinding.inflate(getLayoutInflater());
        View rootView = binding.getRoot();
        setContentView(rootView);

        getSupportActionBar().hide();

        SharedPreferences pref = getSharedPreferences(getString(R.string.auth_shared_preference), MODE_PRIVATE);
        USER_ACCESS_TOKEN = pref.getString(getString(R.string.auth_shared_preference_authToken), "NO Token");

        mViewModel = new ViewModelProvider(this).get(ContactUsViewModel.class);
        mViewModel.init();
        mViewModel.getContactInfo(this, USER_ACCESS_TOKEN).observe(this, new Observer<ContactUs>() {
            @Override
            public void onChanged(ContactUs contactUs) {
                binding.progressBar5.setVisibility(View.GONE);
                binding.addressTv.setText(contactUs.getContacts().getContactInfo().getAddress());
                binding.phoneTv.setText("\u200e" + contactUs.getContacts().getContactInfo().getPhone());
                binding.emailTv.setText(contactUs.getContacts().getContactInfo().getEmail());
                phoneNumberStr = contactUs.getContacts().getContactInfo().getPhone();
                binding.emailTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent email = new Intent(Intent.ACTION_SEND);
                        email.putExtra(Intent.EXTRA_EMAIL, new String[]{ contactUs.getContacts().getContactInfo().getEmail()});
                        //need this to prompts email client only
                        email.setType("message/rfc822");
                        startActivity(Intent.createChooser(email, getString(R.string.choose_email_app)));
                    }
                });
            }
        });

        binding.callImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uri = "tel:" + phoneNumberStr.trim() ;
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(uri));
                startActivity(intent);
            }
        });

        binding.backImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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