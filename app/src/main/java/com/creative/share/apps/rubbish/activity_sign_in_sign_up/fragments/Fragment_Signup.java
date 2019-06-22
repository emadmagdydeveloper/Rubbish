package com.creative.share.apps.rubbish.activity_sign_in_sign_up.fragments;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.creative.share.apps.rubbish.R;
import com.creative.share.apps.rubbish.activity_map.MapActivity;
import com.creative.share.apps.rubbish.ui_clients.ClientHomeActivity;
import com.creative.share.apps.rubbish.models.SelectedLocation;
import com.creative.share.apps.rubbish.models.UserModel;
import com.creative.share.apps.rubbish.preferences.Preference;
import com.creative.share.apps.rubbish.share.Common;
import com.creative.share.apps.rubbish.activity_sign_in_sign_up.SignInActivity;
import com.creative.share.apps.rubbish.tags.Tags;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mukesh.countrypicker.Country;
import com.mukesh.countrypicker.CountryPicker;
import com.mukesh.countrypicker.listeners.OnCountryPickerListener;

import java.util.Locale;


public class Fragment_Signup extends Fragment implements OnCountryPickerListener {


    private ImageView image_back, image_phone_code;
    private EditText edt_name, edt_phone, edt_email, edt_user_name,edt_password;
    private TextView tv_code,tv_address;
    private Button btn_sign_up;
    private CountryPicker picker;
    private SignInActivity activity;
    private String current_language;
    private String code = "";
    private Preference preferences;
    private SelectedLocation selectedLocation;
    private FirebaseAuth mAuth;
    private DatabaseReference dRef;
    private int req_code= 1;


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



        edt_name = view.findViewById(R.id.edt_name);
        edt_phone = view.findViewById(R.id.edt_phone);
        tv_code = view.findViewById(R.id.tv_code);
        edt_email = view.findViewById(R.id.edt_email);
        edt_user_name = view.findViewById(R.id.edt_user_name);
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

        String m_user_name = edt_user_name.getText().toString().trim();
        String m_password = edt_password.getText().toString().trim();

        if (!TextUtils.isEmpty(m_name) &&
                !TextUtils.isEmpty(m_phone) &&
                !TextUtils.isEmpty(m_email) &&

                Patterns.EMAIL_ADDRESS.matcher(m_email).matches() &&
                !TextUtils.isEmpty(m_user_name) &&
                m_password.length()>=6&&

                !TextUtils.isEmpty(code)&&
                selectedLocation!=null

        ) {
            Common.CloseKeyBoard(activity, edt_name);
            edt_name.setError(null);
            edt_phone.setError(null);
            edt_email.setError(null);
            edt_user_name.setError(null);
            tv_address.setError(null);
            edt_password.setError(null);

            sign_up(m_name, code, m_phone, m_email, m_user_name,m_password);
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

    private void sign_up(final String m_name, final String code, final String m_phone, final String m_email, final String m_user_name, final String m_password) {
        final ProgressDialog dialog = Common.createProgressDialog(activity,getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();

        mAuth.createUserWithEmailAndPassword(m_user_name+"@rubbish.com",m_password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful())
                        {
                            String user_id = task.getResult().getUser().getUid();
                            String phone = code+m_phone;
                            UserModel userModel = new UserModel(user_id,m_name,phone,selectedLocation.getAddress(),m_email,m_user_name,m_password,0,selectedLocation.getLat(),selectedLocation.getLng(),0,0, Tags.USER_TYPE_CLIENT,"",1);
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
        }
    }
}
