package com.example.safezone;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG ="MapsActivity" ;
    private GoogleMap mMap;
    private static final String fineLocation = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String coarseLocation = Manifest.permission.ACCESS_COARSE_LOCATION;
    private GoogleMap googleMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final float defaultZoom = 15;
    private static boolean locationPermissionGranted = false;
    private static final int locationPermissionRequestCode = 1;
    private EditText searchMap;
    private ImageView currentLocation ;
    DatabaseReference addLocationReference;
    cLocation location;
    String UserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        searchMap=findViewById(R.id.searchMap);
        currentLocation=findViewById(R.id.gps);
        UserId =  getIntent().getExtras().getString("ID");
        getLocationPermission();

    }
    @Override
    public void onMapReady(GoogleMap googleMapp) {
        Log.d(TAG, "onMapReady: map is ready");
        googleMap = googleMapp;
        if (locationPermissionGranted) {
            getDeviceLocation();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setMyLocationButtonEnabled(false);
            init();
        }
    }

    private void init(){
        Log.d(TAG, "init: initializing ");
        searchMap.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_SEARCH||actionId==EditorInfo.IME_ACTION_DONE||
                        event.getAction()==KeyEvent.ACTION_DOWN||event.getAction()==KeyEvent.KEYCODE_ENTER){
                    geoLocate();

                }
                return false;
            }
        });
        currentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDeviceLocation();
            }
        });
        hideSoftKeyboard();
    }

    private void geoLocate(){
        Log.d(TAG, "geoLocate: geoLocationg");

        String searchString = searchMap.getText().toString();
        Geocoder geocoder= new Geocoder(MapsActivity.this);
        List<Address> addresses = new ArrayList<>();
        try {
            addresses=geocoder.getFromLocationName(searchString,4);
        }
        catch (IOException e){
            Log.d(TAG, "geoLocate: "+e.getMessage());

        }
        if(addresses.size() > 0){
            Address address = addresses.get(0);

            Log.d(TAG, "geoLocate: found Location"+address.toString());
            moveCamera(new LatLng(address.getLatitude(),address.getLongitude()),defaultZoom,address.getAddressLine(0));
            addNewLocation(address.getAddressLine(0),address.getLatitude(),address.getLongitude());
            Log.d(TAG, "geoLocate: "+address.getPhone());
        }
    }

    private void getDeviceLocation(){
        Log.d(TAG, "getDeviceLocation: getting Device Location");
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this);
        try {
            if(locationPermissionGranted){
                Task location = fusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            if(location!=null){
                                Log.d(TAG, "onComplete: found Location");
                                Location currentLocation = (Location) task.getResult();
                                moveCamera(new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude()),defaultZoom,"My Location");
                                Geocoder geocoder= new Geocoder(MapsActivity.this);
                                List<Address> addresses = new ArrayList<>();
                                try {
                                    addresses=geocoder.getFromLocation(currentLocation.getLatitude(),currentLocation.getLongitude(),4);
                                }
                                catch (IOException e){
                                    Log.d(TAG, "geoLocate: "+e.getMessage());
                                }
                                if(addresses.size()>0){
                                    Address address =addresses.get(0);
                                    addNewLocation(address.getAddressLine(0), currentLocation.getLatitude() ,currentLocation.getLongitude());
                                    Log.d(TAG, "geoLocate: found Location"+address.toString());
                                    Log.d(TAG, "geoLocate addres: "+address.getAddressLine(0)+"   "+address.getFeatureName()+"  asda sd  "+address.getUrl());

                                }
                            }

                        }
                        else{
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(MapsActivity.this, "Unable To find Current Location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }catch (SecurityException e){
            Log.d(TAG, "getDeviceLocation: Security Exception :"+e.getMessage());

        }
    }

    private void moveCamera(LatLng latLng,float zoom,String title){
        Log.d(TAG, "moveCamera: moving the camera ");
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));
        if(!title.equals("My Location")){
            MarkerOptions options= new MarkerOptions().position(latLng).title(title);
            googleMap.addMarker(options);
        }
        hideSoftKeyboard();

    }

    private void initMap(){
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    private void getLocationPermission(){
        Log.d(TAG, "getLocationPermission: getting location permission ");
        String [] permissions ={Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,};
        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),fineLocation)== PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),coarseLocation)== PackageManager.PERMISSION_GRANTED){
                locationPermissionGranted=true;
                initMap();
            }else {
                ActivityCompat.requestPermissions(this,permissions,locationPermissionRequestCode);
            }
        }else {
            ActivityCompat.requestPermissions(this,permissions,locationPermissionRequestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called");
        locationPermissionGranted=false;
        switch (requestCode){
            case locationPermissionRequestCode: {
                if (grantResults.length>0){
                    for (int i = 0 ;i<grantResults.length;i++){
                        if(grantResults[0]!=PackageManager.PERMISSION_GRANTED){
                            locationPermissionGranted=false;
                            Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: permission granted");
                    locationPermissionGranted=true;
                    initMap();
                }
            }
        }
    }

    private void hideSoftKeyboard(){
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        searchMap.setText("");
    }

    public void addNewLocation(String LocationName , double lat , double log ){

        addLocationReference = FirebaseDatabase.getInstance().getReference("Locations").child(LocationName);
        addLocationReference.addListenerForSingleValueEvent(new ValueEventListener(){

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot){
                if(snapshot.exists()){
                    long DBCounter = (long) snapshot.child("Counter").getValue();
                    boolean x = false;
                    for(int i = 1 ; i <= DBCounter;i++) {
                         String DBID = snapshot.child("ID_"+i).getValue().toString();
                        if (DBID.equals(UserId)) {
                            Toast.makeText(MapsActivity.this, "location already added", Toast.LENGTH_LONG).show();
                            x = true;
                            break;
                        }
                    }
                    if(x == false)
                    {
                        DBCounter += 1.0;
                        addLocationReference.child("ID_"+DBCounter).setValue(UserId);
                        addLocationReference.child("Counter").setValue(DBCounter);
                        userLocationList(LocationName);
                        Toast.makeText(MapsActivity.this, "Counter incremented", Toast.LENGTH_LONG).show();
                    }

                }else{

                    location = new cLocation(LocationName , lat , log , 1, UserId ,0);
                    addLocationReference = FirebaseDatabase.getInstance().getReference("Locations");
                    addLocationReference.child(LocationName).setValue(location);
                    userLocationList(LocationName);
                    Toast.makeText(MapsActivity.this,"Location has been added",Toast.LENGTH_LONG).show();
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void userLocationList(String LocationName){

        addLocationReference = FirebaseDatabase.getInstance().getReference("User").child(UserId);
        addLocationReference.addListenerForSingleValueEvent(new ValueEventListener(){

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot){
                if(snapshot.exists()){

                    long DBCounter = (long) snapshot.child("locationCounter").getValue();
                    boolean x = false;
                    for(int i = 1 ; i <= DBCounter;i++) {
                        String DBLocationName = snapshot.child("locationName_"+i).getValue().toString();
                        if (DBLocationName.equals(LocationName)) {
                            Toast.makeText(MapsActivity.this, "location already added to your list", Toast.LENGTH_LONG).show();
                            x = true;
                            break;
                        }
                    }
                    if(x == false)
                    {
                        DBCounter += 1.0;
                        addLocationReference.child("locationName_"+DBCounter).setValue(LocationName);
                        addLocationReference.child("locationCounter").setValue(DBCounter);
                        Toast.makeText(MapsActivity.this, "Location Counter incremented", Toast.LENGTH_LONG).show();
                    }

                }else{

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
/*


 private void getDeviceLocation(){
        Log.d(TAG, "getDeviceLocation: getting Device Location");
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this);
        try {
            if(locationPermissionGranted){
                Task location = fusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            if(location!=null){

                                Log.d(TAG, "onComplete: found Location");
                                Location currentLocation = (Location) task.getResult();
                                moveCamera(new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude()),defaultZoom,"My Location");
                                addNewLocation("TESTING_LOCATION_04", currentLocation.getLatitude() ,currentLocation.getLongitude());
                                Toast.makeText(MapsActivity.this , "Adding current location ", Toast.LENGTH_SHORT).show();
                            }

                        }
                        else{
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(MapsActivity.this, "Unable To find Current Location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }catch (SecurityException e){
            Log.d(TAG, "getDeviceLocation: Security Exception :"+e.getMessage());

        }
    }

*
* */