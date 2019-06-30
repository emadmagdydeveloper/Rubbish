package com.creative.share.apps.rubbish.general_fragments;

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
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.creative.share.apps.rubbish.R;
import com.creative.share.apps.rubbish.models.UserModel;
import com.creative.share.apps.rubbish.preferences.Preference;
import com.creative.share.apps.rubbish.share.Common;
import com.creative.share.apps.rubbish.ui_clients.ClientHomeActivity;
import com.creative.share.apps.rubbish.ui_employee.EmployeeHomeActivity;
import com.creative.share.apps.rubbish.ui_financial_manager.FinancialHomeActivity;
import com.creative.share.apps.rubbish.ui_head_manager.HeadMangerHomeActivity;
import com.creative.share.apps.rubbish.ui_supervisor.SupervisorHomeActivity;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
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

public class Fragment_Edit_Profile extends Fragment implements OnCountryPickerListener {

    private AppCompatActivity activity;
    private TextView tv_code;
    private KenBurnsView image_cover;
    private ImageView image_name_edit,image_email_edit,image_phone_edit,image,image_back;
    private EditText edt_name,edt_email,edt_phone;
    private Button btn_update_name,btn_update_email,btn_update_phone;
    private AppBarLayout app_bar;
    private LinearLayout ll_edit_cover;
    private Preference preference;
    private UserModel userModel;
    private CountryPicker picker;
    private String code;
    private DatabaseReference dRef;
    private final String READ_PERM   = Manifest.permission.READ_EXTERNAL_STORAGE;
    private final String WRITE_PERM  = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private final String CAMERA_PERM = Manifest.permission.CAMERA;
    private  int IMG1 = 3,IMG2=4,IMG3 = 5,IMG4=6;

    private Uri uri_image,uri_cover;
    private String current_language;

    private FirebaseAuth mAuth;



    public static Fragment_Edit_Profile newInstance()
    {
        return new Fragment_Edit_Profile();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile,container,false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mAuth = FirebaseAuth.getInstance();
        dRef = FirebaseDatabase.getInstance().getReference();
        activity = (AppCompatActivity) getActivity();
        preference = Preference.newInstance();
        userModel = preference.getUserData(activity);
        current_language = Locale.getDefault().getLanguage();
        app_bar = view.findViewById(R.id.app_bar);

        image_back = view.findViewById(R.id.image_back);

        if (current_language.equals("ar"))
        {
            image_back.setRotation(180.0f);
        }
        ll_edit_cover = view.findViewById(R.id.ll_edit_cover);

        image = view.findViewById(R.id.image);
        image_cover = view.findViewById(R.id.image_cover);

        tv_code = view.findViewById(R.id.tv_code);
        image_name_edit = view.findViewById(R.id.image_name_edit);
        image_email_edit = view.findViewById(R.id.image_email_edit);
        image_phone_edit = view.findViewById(R.id.image_phone_edit);

        edt_name = view.findViewById(R.id.edt_name);
        edt_email = view.findViewById(R.id.edt_email);
        edt_phone = view.findViewById(R.id.edt_phone);

        btn_update_name = view.findViewById(R.id.btn_update_name);
        btn_update_email = view.findViewById(R.id.btn_update_email);
        btn_update_phone = view.findViewById(R.id.btn_update_phone);

        edt_name.setEnabled(false);
        edt_email.setEnabled(false);
        edt_phone.setEnabled(false);
        tv_code.setEnabled(false);

        image_name_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_name.setEnabled(true);
                btn_update_name.setVisibility(View.VISIBLE);
                edt_name.requestFocus();
                InputMethodManager manager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                manager.showSoftInput(edt_name,InputMethodManager.SHOW_IMPLICIT);

            }
        });

        image_email_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_email.setEnabled(true);
                btn_update_email.setVisibility(View.VISIBLE);
                edt_email.requestFocus();
                InputMethodManager manager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                manager.showSoftInput(edt_email,InputMethodManager.SHOW_IMPLICIT);

            }
        });

        image_phone_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_phone.setEnabled(true);
                tv_code.setEnabled(true);
                btn_update_phone.setVisibility(View.VISIBLE);
                edt_phone.requestFocus();
                InputMethodManager manager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                manager.showSoftInput(edt_phone,InputMethodManager.SHOW_IMPLICIT);

            }
        });



        tv_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picker.showDialog(activity);
            }
        });

        btn_update_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edt_name.getText().toString().trim();
                if (!TextUtils.isEmpty(name)&&!userModel.getName().equals(name)){
                    Common.CloseKeyBoard(activity,edt_name);
                    edt_name.setError(null);
                    updateName(name);
                }else
                    {
                        edt_name.setError(getString(R.string.field_req));
                    }
            }
        });

        btn_update_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = edt_phone.getText().toString().trim();
                if (!TextUtils.isEmpty(phone)){
                    Common.CloseKeyBoard(activity,edt_phone);
                    edt_phone.setError(null);
                    updatePhone(code,phone);
                }else
                {
                    edt_phone.setError(getString(R.string.field_req));
                }
            }
        });




        btn_update_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edt_email.getText().toString().trim();
                if (!TextUtils.isEmpty(email)&&
                        Patterns.EMAIL_ADDRESS.matcher(email).matches()&&
                        !userModel.getEmail().equals(email)
                ){
                    Common.CloseKeyBoard(activity,edt_email);
                    edt_email.setError(null);
                    updateEmail(email);
                }else
                {
                    if (TextUtils.isEmpty(email))
                    {
                        edt_email.setError(getString(R.string.field_req));

                    }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                    {
                        edt_email.setError(getString(R.string.inv_email));

                    }else
                        {
                            edt_email.setError(null);

                        }
                }
            }
        });

        app_bar.addOnOffsetChangedListener(new AppBarLayout.BaseOnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                int total_range = appBarLayout.getTotalScrollRange();
                if ((total_range+i)<60)
                {
                    image.setVisibility(View.GONE);
                }else
                {
                    image.setVisibility(View.VISIBLE);


                }
            }
        });

        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activity instanceof ClientHomeActivity)
                {
                    ClientHomeActivity clientHomeActivity = (ClientHomeActivity) activity;
                    clientHomeActivity.Back();

                }else if (activity instanceof EmployeeHomeActivity)
                {
                    EmployeeHomeActivity employeeHomeActivity = (EmployeeHomeActivity) activity;
                    employeeHomeActivity.Back();

                }
                else if (activity instanceof SupervisorHomeActivity)
                {
                    SupervisorHomeActivity supervisorHomeActivity = (SupervisorHomeActivity) activity;
                    supervisorHomeActivity.Back();

                }

                else if (activity instanceof FinancialHomeActivity)
                {
                    FinancialHomeActivity financialHomeActivity = (FinancialHomeActivity) activity;
                    financialHomeActivity.Back();

                }
                else if (activity instanceof HeadMangerHomeActivity)
                {
                    HeadMangerHomeActivity headMangerHomeActivity = (HeadMangerHomeActivity) activity;
                    headMangerHomeActivity.Back();

                }
            }
        });

        ll_edit_cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateChooseImageDialog(1);
            }
        });

        ll_edit_cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateChooseImageDialog(2);
            }
        });
        CreateCountryDialog();

        updateUI();

    }



    private void updateUI() {
        tv_code.setText(userModel.getPhone_code());
        edt_phone.setText(userModel.getPhone());
        edt_name.setText(userModel.getName());
        edt_email.setText(userModel.getEmail());
        code = userModel.getPhone_code();

        if (!userModel.getImage().isEmpty())
        {
            Picasso.with(activity).load(userModel.getImage()).placeholder(R.drawable.user_avatar).fit().into(image);


        }else
        {
            Picasso.with(activity).load(R.drawable.user_avatar).fit().into(image);

        }
        if (!userModel.getCover().isEmpty())
        {
            Picasso.with(activity).load(userModel.getCover()).fit().into(image_cover);

        }
        /*if (userModel.getUser_type() == Tags.USER_TYPE_CLIENT)
        {
            Picasso.with(activity).load(R.drawable.user_cover).fit().into(image_cover);
            Picasso.with(activity).load(R.drawable.employee_user_image).fit().into(image);

        }else if (userModel.getUser_type() == Tags.USER_TYPE_EMPLOYEE)
        {
            Picasso.with(activity).load(R.drawable.employee_cover).fit().into(image_cover);
            Picasso.with(activity).load(R.drawable.employee_user_image).fit().into(image);

        }
        else if (userModel.getUser_type() == Tags.USER_TYPE_SUPERVISOR)
        {
            Picasso.with(activity).load(R.drawable.supervisor_cover).fit().into(image_cover);
            Picasso.with(activity).load(R.drawable.supervisor_image).fit().into(image);

        }
        else if (userModel.getUser_type() == Tags.USER_TYPE_FINANCIAL_MANAGER)
        {
            Picasso.with(activity).load(R.drawable.financial_cover).fit().into(image_cover);
            Picasso.with(activity).load(R.drawable.financial_image).fit().into(image);

        }
        else if (userModel.getUser_type() == Tags.USER_TYPE_HEAD_MANAGER)
        {
            Picasso.with(activity).load(R.drawable.head_cover).fit().into(image_cover);
            Picasso.with(activity).load(R.drawable.head_image).fit().into(image);

        }*/
    }

    private void CreateChooseImageDialog(final int type) {

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
                if (type == 1)
                {
                    CheckReadPermission(IMG1);

                }else
                    {
                        CheckReadPermission(IMG2);

                    }



            }
        });

        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (type==1)
                {
                    Check_CameraPermission(IMG3);

                }else
                    {
                        Check_CameraPermission(IMG4);

                    }

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
    private void CheckReadPermission(int IMG_REQ)
    {
        if (ActivityCompat.checkSelfPermission(activity, READ_PERM) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{READ_PERM}, IMG_REQ);
        } else {
            SelectImage(IMG_REQ);
        }
    }

    private void Check_CameraPermission(int IMG_REQ)
    {
        if (ContextCompat.checkSelfPermission(activity,CAMERA_PERM)!= PackageManager.PERMISSION_GRANTED&&ContextCompat.checkSelfPermission(activity,WRITE_PERM)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(activity,new String[]{CAMERA_PERM,WRITE_PERM},IMG_REQ);
        }else
        {
            SelectImage(IMG_REQ);

        }

    }
    private void SelectImage(int img_req) {

        Intent intent = new Intent();

        if (img_req == IMG1||img_req == IMG2)
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

        }else if (img_req ==IMG3||img_req ==IMG4)
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



    }

    @Override
    public void onSelectCountry(Country country) {
        updatePhoneCodeUi(country);
    }

    private void updatePhoneCodeUi(Country country) {

        tv_code.setText(country.getDialCode());
        code = country.getDialCode();


    }

    private void updateName(final String name)
    {
        final ProgressDialog dialog = Common.createProgressDialog(activity,getString(R.string.wait));
        dialog.show();

        dRef.child("Users")
                .child(userModel.getUser_id())
                .child("name")
                .setValue(name).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    edt_name.setText(name);
                    edt_name.setEnabled(false);
                    btn_update_name.setVisibility(View.GONE);
                    userModel.setName(name);
                    updateUserData(userModel);
                    dialog.dismiss();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                edt_name.setText(userModel.getName());
                dialog.dismiss();
                Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updatePhone(final String phone_code, final String phone)
    {
        final ProgressDialog dialog = Common.createProgressDialog(activity,getString(R.string.wait));
        dialog.show();

        if (!userModel.getPhone_code().equals(phone_code))
        {


            dRef.child("Users")
                    .child(userModel.getUser_id())
                    .child("phone_code")
                    .setValue(phone_code).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful())
                    {
                        tv_code.setText(phone_code);
                        userModel.setPhone_code(phone_code);
                        updateUserData(userModel);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    dialog.dismiss();
                    tv_code.setText(userModel.getPhone_code());
                    Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }

        if (!userModel.getPhone().equals(phone))
        {
            dRef.child("Users")
                    .child("phone")
                    .child(userModel.getUser_id())
                    .setValue(phone).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful())
                    {
                        edt_phone.setText(phone);
                        userModel.setPhone(phone);
                        updateUserData(userModel);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    dialog.dismiss();
                    edt_phone.setText(userModel.getPhone());
                    Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }






        tv_code.setEnabled(false);
        edt_phone.setEnabled(false);
        btn_update_phone.setVisibility(View.GONE);
        dialog.dismiss();
    }

    private void updateEmail(final String email)
    {
        final ProgressDialog dialog = Common.createProgressDialog(activity,getString(R.string.wait));
        dialog.show();

        dRef.child("Users")
                .child(userModel.getUser_id())
                .child("email")
                .setValue(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    edt_email.setText(email);
                    edt_email.setEnabled(false);
                    btn_update_email.setVisibility(View.GONE);
                    userModel.setEmail(email);
                    updateUserData(userModel);
                    dialog.dismiss();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
                edt_email.setText(userModel.getEmail());
                Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void updateUserData(UserModel userModel) {

        if (activity instanceof ClientHomeActivity)
        {
            ClientHomeActivity clientHomeActivity = (ClientHomeActivity) activity;
            clientHomeActivity.UpdateUserData(userModel);



        }else if (activity instanceof EmployeeHomeActivity)
        {
            EmployeeHomeActivity employeeHomeActivity = (EmployeeHomeActivity) activity;
            employeeHomeActivity.UpdateUserData(userModel);
        }else if (activity instanceof SupervisorHomeActivity)
        {
            SupervisorHomeActivity supervisorHomeActivity = (SupervisorHomeActivity) activity;
            supervisorHomeActivity.UpdateUserData(userModel);

        }
        else if (activity instanceof FinancialHomeActivity)
        {
            FinancialHomeActivity financialHomeActivity = (FinancialHomeActivity) activity;
            financialHomeActivity.UpdateUserData(userModel);

        }
        else if (activity instanceof HeadMangerHomeActivity)
        {
            HeadMangerHomeActivity headMangerHomeActivity = (HeadMangerHomeActivity) activity;
            headMangerHomeActivity.UpdateUserData(userModel);

        }
    }


    private void updateImage(final Uri uri_image)
    {
        if (!userModel.getImage().isEmpty())
        {
            final ProgressDialog dialog = Common.createProgressDialog(activity,getString(R.string.wait));
            dialog.show();
            StorageReference storage = FirebaseStorage.getInstance().getReferenceFromUrl(userModel.getImage());
            storage.delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful())
                            {
                                final StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("images/profile/"+userModel.getUser_id()+"/"+System.currentTimeMillis()+".jpg");
                                try {
                                    storageReference.putStream(new FileInputStream(new File(Common.getImagePath(activity,uri_image))))
                                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                    storageReference.getDownloadUrl()
                                                            .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                @Override
                                                                public void onSuccess(Uri uri) {
                                                                    saveUserData(uri.toString(),dialog,1);
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
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }


                            }
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
                final ProgressDialog dialog = Common.createProgressDialog(activity,getString(R.string.wait));
                dialog.show();

                final StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("images/profile/"+userModel.getUser_id()+"/"+System.currentTimeMillis()+".jpg");
                try {
                    storageReference.putStream(new FileInputStream(new File(Common.getImagePath(activity,uri_image))))
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    storageReference.getDownloadUrl()
                                            .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    saveUserData(uri.toString(),dialog,1);
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
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }


    }

    private void updateImageCover(final Uri uri_cover) {
        if (!userModel.getCover().isEmpty())
        {
            final ProgressDialog dialog = Common.createProgressDialog(activity,getString(R.string.wait));
            dialog.show();
            StorageReference storage = FirebaseStorage.getInstance().getReferenceFromUrl(userModel.getCover());
            storage.delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful())
                            {
                                final StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("images/cover/"+userModel.getUser_id()+"/"+System.currentTimeMillis()+".jpg");
                                try {
                                    storageReference.putStream(new FileInputStream(new File(Common.getImagePath(activity,uri_cover))))
                                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                    storageReference.getDownloadUrl()
                                                            .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                @Override
                                                                public void onSuccess(Uri uri) {
                                                                    saveUserData(uri.toString(),dialog,2);
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
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }


                            }
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
            Log.e("2","2");

            final ProgressDialog dialog = Common.createProgressDialog(activity,getString(R.string.wait));
            dialog.show();

            final StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("images/cover/"+userModel.getUser_id()+"/"+System.currentTimeMillis()+".jpg");
            try {
                storageReference.putStream(new FileInputStream(new File(Common.getImagePath(activity,uri_cover))))
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                storageReference.getDownloadUrl()
                                        .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                saveUserData(uri.toString(),dialog,2);
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
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
    private void saveUserData(final String image_path, final ProgressDialog dialog,int type) {

        if (type ==1)
        {
            Picasso.with(activity).load(Uri.parse(image_path)).placeholder(R.drawable.user_avatar).fit().into(image);
            dRef.child("Users").child(userModel.getUser_id())
                    .child("image")
                    .setValue(image_path)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                            {
                                dialog.dismiss();
                                userModel.setImage(image_path);
                                updateUserData(userModel);
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }else
            {
                Picasso.with(activity).load(Uri.parse(image_path)).fit().into(image_cover);

                dRef.child("Users").child(userModel.getUser_id())
                        .child("cover")
                        .setValue(image_path)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful())
                                {
                                    dialog.dismiss();
                                    userModel.setCover(image_path);
                                    updateUserData(userModel);
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMG1 &&resultCode==Activity.RESULT_OK&&data!=null)
        {
            uri_image = data.getData();
            updateImage(uri_image);
        }
        else if (requestCode == IMG2 &&resultCode==Activity.RESULT_OK&&data!=null)
        {

            uri_cover = data.getData();
            updateImageCover(uri_cover);

        }
        else if (requestCode == IMG3 &&resultCode==Activity.RESULT_OK&&data!=null)
        {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");

            uri_image = getUri(bitmap);
            updateImage(uri_image);
        }
        else if (requestCode == IMG4 &&resultCode==Activity.RESULT_OK&&data!=null)
        {

            Bitmap bitmap = (Bitmap) data.getExtras().get("data");

            uri_cover = getUri(bitmap);
            updateImageCover(uri_cover);
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
        else if (requestCode == IMG3)
        {
            if (grantResults.length>0)
            {
                if (grantResults[0]==PackageManager.PERMISSION_GRANTED&&grantResults[1]==PackageManager.PERMISSION_GRANTED)
                {
                    SelectImage(IMG3);

                }else
                {
                    Toast.makeText(activity,R.string.perm_image_denied, Toast.LENGTH_SHORT).show();

                }
            }



        }
        else if (requestCode == IMG4)
        {
            if (grantResults.length>0)
            {
                if (grantResults[0]==PackageManager.PERMISSION_GRANTED&&grantResults[1]==PackageManager.PERMISSION_GRANTED)
                {
                    SelectImage(IMG4);

                }else
                {
                    Toast.makeText(activity,R.string.perm_image_denied, Toast.LENGTH_SHORT).show();

                }
            }



        }
    }

}
