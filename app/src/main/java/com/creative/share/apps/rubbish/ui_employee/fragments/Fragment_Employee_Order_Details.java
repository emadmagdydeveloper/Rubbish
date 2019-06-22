package com.creative.share.apps.rubbish.ui_employee.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.creative.share.apps.rubbish.R;
import com.creative.share.apps.rubbish.models.AcceptanceOrderModel;
import com.creative.share.apps.rubbish.models.NotificationModel;
import com.creative.share.apps.rubbish.models.OrderModel;
import com.creative.share.apps.rubbish.models.UserModel;
import com.creative.share.apps.rubbish.preferences.Preference;
import com.creative.share.apps.rubbish.share.Common;
import com.creative.share.apps.rubbish.ui_employee.EmployeeHomeActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.ui.IconGenerator;

import java.util.Calendar;
import java.util.Locale;

public class Fragment_Employee_Order_Details extends Fragment implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, LocationListener {

    public static final String TAG = "ORDER";
    private ImageView image_back;
    private TextView tv_name, tv_address, tv_order_state;
    private Preference preference;
    private Button btn_accept;
    private UserModel userModel;
    private EmployeeHomeActivity activity;
    private String current_language;

    private Marker marker_client, marker_employee;
    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private final String fineLocPerm = Manifest.permission.ACCESS_FINE_LOCATION;
    private final int loc_req = 1225;
    private double lat = 0.0, lng = 0.0;
    private OrderModel orderModel;
    private DatabaseReference dRef;


    public static Fragment_Employee_Order_Details newInstance(OrderModel orderModel) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(TAG, orderModel);
        Fragment_Employee_Order_Details fragment_employee_order_details = new Fragment_Employee_Order_Details();
        fragment_employee_order_details.setArguments(bundle);
        return fragment_employee_order_details;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_employee_order_details, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {

        dRef = FirebaseDatabase.getInstance().getReference();
        activity = (EmployeeHomeActivity) getActivity();
        current_language = Locale.getDefault().getLanguage();
        preference = Preference.newInstance();
        userModel = preference.getUserData(activity);

        image_back = view.findViewById(R.id.image_back);

        if (current_language.equals("ar")) {
            image_back.setRotation(180.0f);
        }

        tv_name = view.findViewById(R.id.tv_name);
        tv_address = view.findViewById(R.id.tv_address);
        tv_order_state = view.findViewById(R.id.tv_order_state);


        btn_accept = view.findViewById(R.id.btn_accept);

        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AcceptOrder();
            }
        });

        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.Back();
            }
        });

        updateUI();

        CheckPermission();

        Bundle bundle = getArguments();
        if (bundle != null) {
            orderModel = (OrderModel) bundle.getSerializable(TAG);
            if (orderModel != null) {
                tv_name.setText(orderModel.getFrom_name());
                tv_address.setText(orderModel.getFrom_address());

            }

        }
        getOrderState();




    }

    private void getOrderState() {
        final ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        dRef.child("Accepted_Orders")
                .child(orderModel.getOrder_id())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() != null) {
                            AcceptanceOrderModel acceptanceOrderModel = dataSnapshot.getValue(AcceptanceOrderModel.class);
                            if (acceptanceOrderModel.getOrder_state() == 1) {
                                tv_order_state.setVisibility(View.GONE);
                                btn_accept.setVisibility(View.VISIBLE);
                                dialog.dismiss();
                            } else{
                                dialog.dismiss();

                                tv_order_state.setVisibility(View.GONE);
                                btn_accept.setVisibility(View.GONE);

                            }
                        } else {
                            tv_order_state.setVisibility(View.GONE);
                            btn_accept.setVisibility(View.VISIBLE);
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        dialog.dismiss();
                    }
                });
    }

    private void AcceptOrder() {
        final ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        AcceptanceOrderModel acceptanceOrderModel = new AcceptanceOrderModel(userModel.getUser_id(), userModel.getName(),orderModel.getOrder_id(), 2);

        dRef.child("Accepted_Orders")
                .child(orderModel.getOrder_id())
                .setValue(acceptanceOrderModel)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            btn_accept.setVisibility(View.GONE);
                            AddNotificationForClient(dialog);
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

    private void AddNotificationForClient(final ProgressDialog dialog) {

        Calendar calendar = Calendar.getInstance();
        String notification_id = dRef.child("Notification_Client").child(orderModel.getFrom_id()).push().getKey();
        NotificationModel notificationModel = new NotificationModel(notification_id,userModel.getName(),"تم الموافقة على طلبك",calendar.getTimeInMillis());
        dRef.child("Notification_Client").child(orderModel.getFrom_id()).child(notification_id)
                .setValue(notificationModel)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            dialog.dismiss();
                            Toast.makeText(activity, getString(R.string.suc), Toast.LENGTH_SHORT).show();
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

    private void CheckPermission() {
        if (ActivityCompat.checkSelfPermission(activity, fineLocPerm) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{fineLocPerm}, loc_req);
        } else {

            initGoogleApi();
        }
    }

    private void initGoogleApi() {
        googleApiClient = new GoogleApiClient.Builder(activity)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        googleApiClient.connect();
    }


    private void updateUI() {

        SupportMapFragment fragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        fragment.getMapAsync(this);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        if (googleMap != null) {
            mMap = googleMap;
            mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(activity, R.raw.maps));
            mMap.setTrafficEnabled(false);
            mMap.setBuildingsEnabled(false);
            mMap.setIndoorEnabled(true);



        }
    }

    private void AddMarkerForClient(double lat, double lng) {


        if (marker_client == null) {
            IconGenerator iconGenerator = new IconGenerator(activity);
            iconGenerator.setBackground(null);
            View view = LayoutInflater.from(activity).inflate(R.layout.client_map_icon, null);
            iconGenerator.setContentView(view);
            marker_client = mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).icon(BitmapDescriptorFactory.fromBitmap(iconGenerator.makeIcon())).anchor(iconGenerator.getAnchorU(), iconGenerator.getAnchorV()));

        } else {
            marker_client.setPosition(new LatLng(lat, lng));


        }


    }

    private void AddMarkerForEmployee(double lat, double lng) {


        if (marker_employee == null) {
            IconGenerator iconGenerator = new IconGenerator(activity);
            iconGenerator.setBackground(null);
            View view = LayoutInflater.from(activity).inflate(R.layout.search_map_icon, null);
            iconGenerator.setContentView(view);
            marker_employee = mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).icon(BitmapDescriptorFactory.fromBitmap(iconGenerator.makeIcon())).anchor(iconGenerator.getAnchorU(), iconGenerator.getAnchorV()));

        } else {
            marker_employee.setPosition(new LatLng(lat, lng));


        }

        AddMarkerForClient(orderModel.getFrom_latitude(),orderModel.getFrom_longitude());

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        Log.e("lat_client",marker_client.getPosition().latitude+"_");
        Log.e("lat_emp",marker_employee.getPosition().latitude+"_");

        builder.include(marker_client.getPosition());
        builder.include(marker_employee.getPosition());

        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 200));
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        initLocationRequest();
    }

    private void initLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setFastestInterval(1000);
        locationRequest.setInterval(60000);
        LocationSettingsRequest.Builder request = new LocationSettingsRequest.Builder();
        request.addLocationRequest(locationRequest);
        request.setAlwaysShow(false);


        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, request.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
                Status status = locationSettingsResult.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        startLocationUpdate();
                        break;

                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            status.startResolutionForResult(activity, 100);
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                        }
                        break;

                }
            }
        });

    }

    @Override
    public void onConnectionSuspended(int i) {
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @SuppressLint("MissingPermission")
    private void startLocationUpdate() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                onLocationChanged(locationResult.getLastLocation());
            }
        };
        LocationServices.getFusedLocationProviderClient(activity)
                .requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
    }

    @Override
    public void onLocationChanged(Location location) {
        lat = location.getLatitude();
        lng = location.getLongitude();

        AddMarkerForEmployee(lat, lng);
        //AddMarker(lat,lng);
        LocationServices.getFusedLocationProviderClient(activity)
                .removeLocationUpdates(locationCallback);
        googleApiClient.disconnect();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (googleApiClient != null) {
            LocationServices.getFusedLocationProviderClient(activity).removeLocationUpdates(locationCallback);
            googleApiClient.disconnect();
            googleApiClient = null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == loc_req) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initGoogleApi();
            } else {
                Toast.makeText(activity, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
