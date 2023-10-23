package com.qceccenter.qcec.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.content.ClipData;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.qceccenter.qcec.R;
import com.qceccenter.qcec.databinding.ActivityNewVisitBinding;
import com.qceccenter.qcec.models.VisitSubmission;
import com.qceccenter.qcec.ui.adpaters.NewImageRVAdapter;
import com.qceccenter.qcec.utils.ImageHelper;
import com.qceccenter.qcec.utils.LocaleHelper;
import com.qceccenter.qcec.utils.LocationHelper;
import com.qceccenter.qcec.utils.MultipartHelper;
import com.qceccenter.qcec.viewmodels.NewVisitViewModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.MultipartBody;

public class NewVisitActivity extends AppCompatActivity {
    private int REQUEST_PERMISSIONS_CODE = 132;
    private int CAPTURE_IMAGES_CAMERA_CODE = 100;
    private int PICK_IMAGES_GALLERY_CODE = 105;
    private int IMAGE_PICK_COUNT = 15;
    private ActivityNewVisitBinding binding;
    private NewVisitViewModel mViewModel;
    private NewImageRVAdapter mAttachmentAdapter;
    private List<String> attachmentUriList;
    private List<Bitmap> attachmentList;
    private LocationHelper locationHelper = new LocationHelper();
    private List<MultipartBody.Part> mAttachmentList = new ArrayList();
    private String USER_ACCESS_TOKEN;
    private int mTransactionID = 0;
    private double latitude = 0;
    private double longitude = 0;
    private Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewVisitBinding.inflate(getLayoutInflater());
        View rootView = binding.getRoot();
        setContentView(rootView);
        getSupportActionBar().hide();
        initAttachmentsRV();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!LocaleHelper.getLanguage(this).equals(getString(R.string.locale_check))){
            LocaleHelper.updateLocaleFromContext(this);
            this.recreate();
        }
        checkPermissions();
        getLocation();
        mViewModel = new ViewModelProvider(this).get(NewVisitViewModel.class);
        mViewModel.init();

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewVisitActivity.this.finish();
            }
        });

        Intent intent = getIntent();
        if (intent.hasExtra(getString(R.string.transaction_ID_intent_key))) {
            mTransactionID = intent.getIntExtra(getString(R.string.transaction_ID_intent_key), 0);
        }

        SharedPreferences pref = getSharedPreferences(getString(R.string.auth_shared_preference), MODE_PRIVATE);
        USER_ACCESS_TOKEN = pref.getString(getString(R.string.auth_shared_preference_authToken), "NO Token");


        binding.newVisitCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewVisitActivity.this.finish();
            }
        });


        binding.addAttachmentCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.create(NewVisitActivity.this).includeVideo(false).limit(IMAGE_PICK_COUNT).start();
            }
        });

        binding.newVisitAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkIfRequiredFieldsAreSet()) {
                    binding.progressBar2.setVisibility(View.VISIBLE);
                    binding.newVisitAddBtn.setEnabled(false);
                    String workDescription = binding.desciptionEt.getText().toString();
                    String notes = binding.notesEt.getText().toString();
                    int sendToOwner = binding.ownerCheckBox.isChecked() ? 1 : 0;
                    int sentToCont = binding.contCheckBox.isChecked() ? 1 : 0;
                    addImagesToAttachmentMultipartList();
                    mViewModel.submitVisit(NewVisitActivity.this, USER_ACCESS_TOKEN, mTransactionID, workDescription, notes, latitude, longitude, sendToOwner, sentToCont, mViewModel, mAttachmentList).observe(NewVisitActivity.this, new Observer<VisitSubmission>() {
                        @Override
                        public void onChanged(VisitSubmission visitSubmission) {
                            binding.newVisitAddBtn.setEnabled(true);
                            if (visitSubmission.getVisitSubmission().getSubmittedSuccessfully()) {
                                Toast.makeText(NewVisitActivity.this, getString(R.string.visit_submitted_successfully_msg), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(NewVisitActivity.this, getString(R.string.required_fields_msg), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA
                    , Manifest.permission.READ_EXTERNAL_STORAGE
                    , Manifest.permission.WRITE_EXTERNAL_STORAGE
                    , Manifest.permission.ACCESS_FINE_LOCATION
                    , Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_PERMISSIONS_CODE);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putInt(getString(R.string.transaction_ID_intent_key), this.mTransactionID);
    }

    @Override
    public void onRestoreInstanceState(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);
        this.mTransactionID = savedInstanceState.getInt(getString(R.string.transaction_ID_intent_key), 0);
        if (mTransactionID == 0) {
            finish();
        }
    }

    public void getLocation() {
        location = locationHelper.checkIsLocationProviderEnabled(this);
        if (location != null) {
            getGoogleMapsInfo(location.getLatitude(), location.getLongitude());
            this.latitude = location.getLatitude();
            this.longitude = location.getLongitude();
        }
    }

    private void getGoogleMapsInfo(double latitude, double longitude) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
//            addresses = geocoder.getFromLocation(30.0635023,31.3461133, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            if (addresses.size() > 0) {
                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                binding.newVisitLocationTv.setText(address);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createAddAttachmentDialog() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_attachments);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        String[] dialogArr = {getString(R.string.add_attachment_dialog_camera), getString(R.string.add_attachment_dialog_gallery)};
        ListView listView = dialog.findViewById(R.id.dialog_listView);
        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(this, R.layout.attachment_dialog_list_item, R.id.attachment_dialog_tv, dialogArr);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAPTURE_IMAGES_CAMERA_CODE);
                } else {
//                    // in onCreate or any event where your want the user to
//                    // select a file
//                    Intent intent = new Intent();
//                    intent.setType("image/*");
//                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//                    intent.setAction(Intent.ACTION_GET_CONTENT);
//                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGES_GALLERY_CODE);

                    ImagePicker.create(NewVisitActivity.this).includeVideo(false).limit(IMAGE_PICK_COUNT).start();
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void initAttachmentsRV() {
        this.attachmentUriList = new ArrayList();
//        mAttachmentAdapter = new NewImageRVAdapter(attachmentList, this);
        mAttachmentAdapter = new NewImageRVAdapter(attachmentUriList, this);
        LinearLayoutManager layoutManager = new GridLayoutManager(this, 1);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        binding.attachmentsRV.setLayoutManager(layoutManager);
        binding.attachmentsRV.setHasFixedSize(true);
        binding.attachmentsRV.setAdapter(mAttachmentAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGES_CAMERA_CODE) {
//            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
//            addImageToRV(bitmap);
            String imgUri = ImagePicker.getImages(data).get(0).getUri().toString();
            addImageToRV(imgUri);
        } else if (resultCode == RESULT_OK && requestCode == PICK_IMAGES_GALLERY_CODE) {
            try {
                if (data.getClipData() != null) {
                    ClipData mClipData = data.getClipData();
                    for (int i = 0; i < mClipData.getItemCount(); i++) {
                        Uri imageUri = mClipData.getItemAt(i).getUri();
//                        getImageBitmapFromUriAndAddToRV(imageUri);
                        addImageToRV(imageUri.toString());
                    }
                } else if (data.getData() != null) {
//                    getImageBitmapFromUriAndAddToRV(data.getData());
                    String imgUri = ImagePicker.getImages(data).get(0).getUri().toString();
                    addImageToRV(imgUri);
                }
            } catch (Exception e){}
//            catch (FileNotFoundException exception) {
//                exception.printStackTrace();
//            }
        } else if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            // Get a list of picked images
            List<Image> images = ImagePicker.getImages(data);
            IMAGE_PICK_COUNT -= images.size();
            for (int i = 0; i < images.size(); i++) {
                try {
//                    getImageBitmapFromUriAndAddToRV(images.get(i).getUri());
                    addImageToRV(images.get(i).getUri().toString());
                }
//                catch (FileNotFoundException e) {
//                    e.printStackTrace(); }
                catch (Exception e){

                }
            }
        }
    }

    public void increaseImageCount() {
        this.IMAGE_PICK_COUNT++;
    }

//    private void addImageToRV(Bitmap bitmap) {
//        this.attachmentList.add(bitmap);
//        this.mAttachmentAdapter.setAttachmentList(this.attachmentList);
//        this.mAttachmentAdapter.notifyDataSetChanged();
//        if (mAttachmentAdapter.getItemCount() > 0) {
//            binding.noImgImageView.setVisibility(View.GONE);
//            binding.noImgTv.setVisibility(View.GONE);
//        }
//    }
    private void addImageToRV(String imgURI) {
        this.attachmentUriList.add(imgURI);
        this.mAttachmentAdapter.setAttachmentList(this.attachmentUriList);
        this.mAttachmentAdapter.notifyDataSetChanged();
        if (mAttachmentAdapter.getItemCount() > 0) {
            binding.noImgImageView.setVisibility(View.GONE);
            binding.noImgTv.setVisibility(View.GONE);
        }
    }

//    private void addImageToAttachmentList(Uri imageUri) throws FileNotFoundException {
//        InputStream inputStream = getContentResolver().openInputStream(imageUri);
//        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//        File imageFile = ImageHelper.saveBitmapToStorage(bitmap, mAttachmentList.size() + 1);
//        this.mAttachmentList.add(MultipartHelper.prepareFilePart("images[" + Integer.toString(mAttachmentList.size() + 1) + "]", imageFile));
//        addImageToRV(bitmap);
//    }

//    private void getImageBitmapFromUriAndAddToRV(Uri uri) throws FileNotFoundException {
//        InputStream inputStream = getContentResolver().openInputStream(uri);
//        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//        addImageToRV(bitmap);
//    }

    private Bitmap getImageBitmapFromUri(Uri uri) throws FileNotFoundException {
        InputStream inputStream = getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        return bitmap;
    }

//    private boolean addImagesToAttachmentMultipartList() {
//        this.attachmentList = mAttachmentAdapter.getmAttachmentList();
//        for (int i = 0; i < this.attachmentList.size(); i++) {
//            File imageFile = ImageHelper.saveBitmapToStorage(this.attachmentList.get(i), mAttachmentList.size() + 1);
//            this.mAttachmentList.add(MultipartHelper.prepareFilePart("images[" + Integer.toString(mAttachmentList.size() + 1) + "]", imageFile));
//        }
//        return true;
//    }
    private boolean addImagesToAttachmentMultipartList() {
        this.attachmentUriList = mAttachmentAdapter.getmAttachmentList();
        for (int i = 0; i < this.attachmentUriList.size(); i++) {
            try {
                Bitmap imgBitmap = getImageBitmapFromUri(Uri.parse(this.attachmentUriList.get(i)));
                File imageFile = ImageHelper.saveBitmapToStorage(imgBitmap, mAttachmentList.size() + 1);
                this.mAttachmentList.add(MultipartHelper.prepareFilePart("images[" + Integer.toString(mAttachmentList.size() + 1) + "]", imageFile));
                imgBitmap.recycle();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            }
        return true;
    }

    private boolean checkIfRequiredFieldsAreSet() {
        if ((binding.desciptionEt.getText().toString() != null && !binding.desciptionEt.getText().toString().equals("")) && (binding.notesEt.getText().toString() != null && !binding.notesEt.getText().toString().equals("")))
            return true;
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocaleHelper.updateLocaleFromContext(this);
    }

    public Button getAddVisitBtnRef() {
        return binding.newVisitAddBtn;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS_CODE) {
            Boolean exitNewVisit = false;
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == -1) {
                    exitNewVisit = true;
                }
            }
            if (exitNewVisit) {
                Toast.makeText(this, getString(R.string.grant_persmission_toast), Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    public void showNoAttachmentIcon(){
        binding.noImgTv.setVisibility(View.VISIBLE);
        binding.noImgImageView.setVisibility(View.VISIBLE);
    }

}