package com.creative.share.apps.rubbish.activity_sign_in_sign_up.fragments;


import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.creative.share.apps.rubbish.R;
import com.creative.share.apps.rubbish.activity_map.MapActivity;
import com.creative.share.apps.rubbish.activity_sign_in_sign_up.SignInActivity;
import com.creative.share.apps.rubbish.models.SelectedLocation;
import com.creative.share.apps.rubbish.models.UserModel;
import com.creative.share.apps.rubbish.preferences.Preference;
import com.creative.share.apps.rubbish.share.Common;
import com.creative.share.apps.rubbish.tags.Tags;
import com.creative.share.apps.rubbish.ui_clients.ClientHomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mukesh.countrypicker.Country;
import com.mukesh.countrypicker.CountryPicker;
import com.mukesh.countrypicker.listeners.OnCountryPickerListener;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;


public class Fragment_Signup extends Fragment implements OnCountryPickerListener {


    private ImageView image_back, image_phone_code;
    private EditText edt_name, edt_phone, edt_email, edt_user_name,edt_password;
    private TextView tv_code,tv_address;
    private FrameLayout fl;
    private CircleImageView image;
    private Button btn_sign_up;
    private CountryPicker picker;
    private SignInActivity activity;
    private String current_language;
    private String code = "";
    private Preference preferences;
    private SelectedLocation selectedLocation;
    private FirebaseAuth mAuth;
    private DatabaseReference dRef;
    private StorageReference sRef;
    private int req_code= 1;
    private final String READ_PERM   = Manifest.permission.READ_EXTERNAL_STORAGE;
    private final String WRITE_PERM  = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private final String CAMERA_PERM = Manifest.permission.CAMERA;
    private  int IMG1 = 3,IMG2=4 ;
    private Uri uri_image = null;



    public static Fragment_Signup newInstance() {
        return new Fragment_Signup();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {

        preferences = Preference.newInstance();
        sRef = FirebaseStorage.getInstance().getReference();
        dRef = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();

        activity = (SignInActivity) getActivity();
        current_language = Locale.getDefault().getLanguage();

        image_back = view.findViewById(R.id.image_back);
        image_phone_code = view.findViewById(R.id.image_phone_code);

        if (current_language.equals("ar")) {
            image_back.setRotation(180.0f);
            image_phone_code.setRotation(180.0f);
        }

        image = view.findViewById(R.id.image);
        fl = view.findViewById(R.id.fl);


        edt_name = view.findViewById(R.id.edt_name);
        edt_phone = view.findViewById(R.id.edt_phone);
        tv_code = view.findViewById(R.id.tv_code);
        edt_email = view.findViewById(R.id.edt_email);
        //edt_user_name = view.findViewById(R.id.edt_user_name);
        edt_password = view.findViewById(R.id.edt_password);
        tv_address = view.findViewById(R.id.tv_address);

        btn_sign_up = view.findViewById(R.id.btn_sign_up);

        CreateCountryDialog();

        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.Back();
            }
        });


        image_phone_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picker.showDialog(activity);
            }
        });

        btn_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkData();
            }
        });

        tv_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, MapActivity.class);
                startActivityForResult(intent,req_code);

            }
        });

        edt_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String phone = edt_phone.getText().toString().trim();
                if (phone.startsWith("0"))
                {
                    edt_phone.setText("");
                    Common.CreateSignAlertDialog(activity,getString(R.string.ph_num_not_start));
                }
            }
        });

        fl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateChooseImageDialog();
            }
        });

    }

    private void CreateChooseImageDialog() {

        final AlertDialog dialog = new AlertDialog.Builder(activity)
                .setCancelable(true)
                .create();


        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_select_image,null);
        Button btn_camera = view.findViewById(R.id.btn_camera);
        Button btn_gallery = view.findViewById(R.id.btn_gallery);
        Button btn_cancel = view.findViewById(R.id.btn_cancel);

        btn_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                CheckReadPermission();



            }
        });

        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Check_CameraPermission();

            }
        });



        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.getWindow().getAttributes().windowAnimations= R.style.dialog_congratulation_animation;
        dialog.setCanceledOnTouchOutside(false);
        dialog.setView(view);
        dialog.show();
    }
    private void CheckReadPermission()
    {
        if (ActivityCompat.checkSelfPermission(activity, READ_PERM) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{READ_PERM}, IMG1);
        } else {
            SelectImage(IMG1);
        }
    }

    private void Check_CameraPermission()
    {
        if (ContextCompat.checkSelfPermission(activity,CAMERA_PERM)!= PackageManager.PERMISSION_GRANTED&&ContextCompat.checkSelfPermission(activity,WRITE_PERM)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(activity,new String[]{CAMERA_PERM,WRITE_PERM},IMG2);
        }else
        {
            SelectImage(IMG2);

        }

    }
    private void SelectImage(int img_req) {

        Intent intent = new Intent();

        if (img_req == 3)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            {
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            }else
            {
                intent.setAction(Intent.ACTION_GET_CONTENT);

            }

            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setType("image/*");
            startActivityForResult(intent,img_req);

        }else if (img_req ==4)
        {
            try {
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,img_req);
            }catch (SecurityException e)
            {
                Toast.makeText(activity,R.string.perm_image_denied, Toast.LENGTH_SHORT).show();
            }
            catch (Exception e)
            {
                Toast.makeText(activity,R.string.perm_image_denied, Toast.LENGTH_SHORT).show();

            }


        }
    }


    private void CreateCountryDialog() {
        CountryPicker.Builder builder = new CountryPicker.Builder()
                .canSearch(true)
                .with(activity)
                .listener(this);
        picker = builder.build();

        TelephonyManager telephonyManager = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);


        if (picker.getCountryFromSIM() != null) {
            updateUi(picker.getCountryFromSIM());

        } else if (telephonyManager != null && picker.getCountryByISO(telephonyManager.getNetworkCountryIso()) != null) {
            updateUi(picker.getCountryByISO(telephonyManager.getNetworkCountryIso()));


        } else if (picker.getCountryByLocale(Locale.getDefault()) != null) {
            updateUi(picker.getCountryByLocale(Locale.getDefault()));

        } else {
            tv_code.setText("+20");
            code = "+20";
        }


    }

    @Override
    public void onSelectCountry(Country country) {
        updateUi(country);
    }

    private void updateUi(Country country) {

        tv_code.setText(country.getDialCode());
        code = country.getDialCode();


    }

    private void checkData() {

        String m_name = edt_name.getText().toString().trim();
        String m_phone = edt_phone.getText().toString().trim();
        String m_email = edt_email.getText().toString().trim();

        //String m_user_name = edt_user_name.getText().toString().trim();
        String m_password = edt_password.getText().toString().trim();

        if (!TextUtils.isEmpty(m_name) &&
                !TextUtils.isEmpty(m_phone) &&
                !TextUtils.isEmpty(m_email) &&

                Patterns.EMAIL_ADDRESS.matcher(m_email).matches() &&
                m_password.length()>=6&&

                !TextUtils.isEmpty(code)&&
                selectedLocation!=null

        ) {
            Common.CloseKeyBoard(activity, edt_name);
            edt_name.setError(null);
            edt_phone.setError(null);
            edt_email.setError(null);
            //edt_user_name.setError(null);
            tv_address.setError(null);
            edt_password.setError(null);

            if (uri_image==null)
            {
                sign_up(m_name, code, m_phone, m_email,m_password);

            }else
                {
                    sign_up_with_image(m_name, code, m_phone, m_email,m_password);

                }
        } else {
            if (TextUtils.isEmpty(m_name)) {
                edt_name.setError(getString(R.string.field_req));
            } else {
                edt_name.setError(null);

            }


            if (TextUtils.isEmpty(m_phone)) {
                edt_phone.setError(getString(R.string.field_req));
            } else {
                edt_phone.setError(null);

            }


            if (TextUtils.isEmpty(m_email)) {
                edt_email.setError(getString(R.string.field_req));
            } else if (!Patterns.EMAIL_ADDRESS.matcher(m_email).matches()) {
                edt_email.setError(getString(R.string.inv_email));

            } else {
                edt_email.setError(null);

            }

            if (TextUtils.isEmpty(m_password)) {
                edt_password.setError(getString(R.string.field_req));
            }else if (m_password.length()<6)
            {
                edt_password.setError(getString(R.string.pas_short));
            }

            else {
                edt_password.setError(null);

            }


            if (TextUtils.isEmpty(code)) {
                tv_code.setError(getString(R.string.field_req));
            } else {
                tv_code.setError(null);

            }

        }

    }

    private void sign_up_with_image(final String m_name, final String code, final String m_phone, final String m_email, final String m_password) {
        final ProgressDialog dialog = Common.createProgressDialog(activity,getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();


        mAuth.createUserWithEmailAndPassword(m_email,m_password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful())
                        {

                            String user_id = task.getResult().getUser().getUid();
                            UserModel userModel = new UserModel(user_id,m_name,code,m_phone,selectedLocation.getAddress(),"","",m_email,m_password,0,selectedLocation.getLat(),selectedLocation.getLng(),0,0, Tags.USER_TYPE_CLIENT,"",1);
                            SaveUserData(userModel,dialog);


                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
                Toast.makeText(activity,e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });









    }

    private void sign_up(final String m_name, final String code, final String m_phone, final String m_email, final String m_password) {
        final ProgressDialog dialog = Common.createProgressDialog(activity,getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();

        mAuth.createUserWithEmailAndPassword(m_email,m_password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful())
                        {
                            String user_id = task.getResult().getUser().getUid();
                            UserModel userModel = new UserModel(user_id,m_name,code,m_phone,selectedLocation.getAddress(),"","",m_email,m_password,0,selectedLocation.getLat(),selectedLocation.getLng(),0,0, Tags.USER_TYPE_CLIENT,"",1);
                            SaveUserData(userModel,dialog);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
                Toast.makeText(activity,e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void SaveUserData(final UserModel userModel, final ProgressDialog dialog) {


        try {

            if (uri_image!=null)
            {
                final StorageReference storageReference = sRef.child("images/"+userModel.getUser_id()+"/"+System.currentTimeMillis()+".jpg");
                storageReference.putStream(new FileInputStream(new File(Common.getImagePath(activity,uri_image))))
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(final Uri uri) {
                                        userModel.setImage(uri_image.toString());

                                        dRef.child(userModel.getUser_id()).setValue(userModel)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful())
                                                        {


                                                            dialog.dismiss();
                                                            preferences.create_update_user_data(activity,userModel);
                                                            Intent intent = new Intent(activity, ClientHomeActivity.class);
                                                            activity.startActivity(intent);
                                                            activity.finish();
                                                        }
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                dialog.dismiss();
                                                Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                        Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }else
                {
                    dRef.child(userModel.getUser_id()).setValue(userModel)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful())
                                    {


                                        dialog.dismiss();
                                        preferences.create_update_user_data(activity,userModel);
                                        Intent intent = new Intent(activity, ClientHomeActivity.class);
                                        activity.startActivity(intent);
                                        activity.finish();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dialog.dismiss();
                            Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

        } catch (FileNotFoundException e) {

        }


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == req_code&&resultCode==Activity.RESULT_OK&&data!=null)
        {
            if (data.hasExtra("location"))
            {
                selectedLocation = (SelectedLocation) data.getSerializableExtra("location");
                tv_address.setText(selectedLocation.getAddress());
            }
        }else if (requestCode == IMG1 &&resultCode==Activity.RESULT_OK&&data!=null)
        {
            uri_image = data.getData();
            File file = new File(Common.getImagePath(activity,uri_image));
            Picasso.with(activity).load(file).fit().into(image);
        }
        else if (requestCode == IMG2 &&resultCode==Activity.RESULT_OK&&data!=null)
        {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");

            image.setImageBitmap(bitmap);

            uri_image = getUri(bitmap);
        }

    }

    private Uri getUri(Bitmap bitmap)
    {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream);
            String path = MediaStore.Images.Media.insertImage(activity.getContentResolver(),bitmap,"title",null);

            return Uri.parse(path);
        }catch (Exception e)
        {
            Toast.makeText(activity, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();
        }
            return null;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == IMG1)
        {

            if (grantResults.length>0)
            {

                if (grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {

                    SelectImage(IMG1);

                }else
                {
                    Toast.makeText(activity,R.string.perm_image_denied, Toast.LENGTH_SHORT).show();

                }
            }

        }else if (requestCode == IMG2)
        {
            if (grantResults.length>0)
            {
                if (grantResults[0]==PackageManager.PERMISSION_GRANTED&&grantResults[1]==PackageManager.PERMISSION_GRANTED)
                {
                    SelectImage(IMG2);

                }else
                {
                    Toast.makeText(activity,R.string.perm_image_denied, Toast.LENGTH_SHORT).show();

                }
            }


        }
    }
}
