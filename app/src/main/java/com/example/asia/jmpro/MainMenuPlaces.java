package com.example.asia.jmpro;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asia.jmpro.data.db.PlaceDao;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

import static com.example.asia.jmpro.MainActivity.MY_PERMISSIONS_REQUEST_ACCESS_LOCATION;


public class MainMenuPlaces extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    PlaceDao placeDao = new PlaceDao(this);
    Dialog dialog;
    DrawerLayout drawer;
    TextView optionsTitle;
    String[] placesOptions;
    GoogleMap map;
    GoogleApiClient googleApiClient;
    Location userLocation = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu_places);

        optionsTitle = (TextView) findViewById(R.id.optionTitle);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add_to_fav);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLocation = getCurrentLocation();
                if(userLocation!=null) {
                    showAddFavouritePlaceDialog(userLocation);
                } else {
                    checkIfLocalizationEnabled();
                }
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        placesOptions = getApplicationContext().getResources().getStringArray(R.array.places_options);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        selectItem(0);

        initMap();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_favourite_places) {
            selectItem(0);
        } else if (id == R.id.nav_suggested_places) {
            selectItem(1);
        } else if (id == R.id.nav_suggest) {
            selectItem(2);
        } else if (id == R.id.nav_share_email) {
            Toast.makeText(this, "Share via email clicked", Toast.LENGTH_LONG).show();
        } else if (id == R.id.nav_share_facebook) {
            Toast.makeText(this, "Share via fb clicked", Toast.LENGTH_LONG).show();
        } else if (id == R.id.nav_share_messenger) {
            Toast.makeText(this, "Share via msn clicked", Toast.LENGTH_LONG).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    private void selectItem(int position) {

        switch (position) {
            case 0:
                optionsTitle.setText(placesOptions[position]);


                break;
            case 1:
                optionsTitle.setText(placesOptions[position]);

                break;
            case 2:
                optionsTitle.setText(placesOptions[position]);
                userLocation = getCurrentLocation();
                if(userLocation!=null){
                    showAddSuggestedPlaceDialog(userLocation);
                } else {
                    checkIfLocalizationEnabled();
                }
                break;

            case 3:
                break;

            case 4:
                break;

            case 5:
                break;

            default:
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
    }


    public void initMap() {
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);
    }

    private void goToLocationZoom(double lat, double lng, float zoom) {
        LatLng latLng = new LatLng(lat, lng);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, zoom);
        map.moveCamera(cameraUpdate);
    }

    private String getCurrentPlaceAddress(Location location){
    Geocoder geocoder = new Geocoder(this);
    List<Address> list;
    String localityName = "";

    try {
            list = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            Address address = list.get(0);

            localityName = getString(R.string.street) + address.getAddressLine(0) + ", \n" + address.getAddressLine(1);


    } catch (IOException e) {
        e.printStackTrace();
    }
    return localityName;
}

    private void showAddFavouritePlaceDialog(final Location location) {

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.add_place_dialog);
        dialog.setTitle(R.string.add_fav_place);
        dialog.show();

        final EditText placeName = (EditText) dialog.findViewById(R.id.placeName);
        final EditText placeAddress = (EditText) dialog.findViewById(R.id.placeAddress);
        Button addPlaceButton = (Button) dialog.findViewById(R.id.addPlaceButton);

            placeAddress.setText(getCurrentPlaceAddress(location));
            Toast.makeText(this, location.toString(), Toast.LENGTH_LONG).show();


        addPlaceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placeDao.addFavouritePlaceToDatabase(placeName.getText().toString().trim() + ", "+ placeAddress.getText().toString().trim(),location);
                dialog.cancel();
            }
        });
    }

    private void showAddSuggestedPlaceDialog(final Location location){
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.add_place_dialog);
        dialog.setTitle(R.string.add_sug_place);
        dialog.show();

        final EditText placeName = (EditText) dialog.findViewById(R.id.placeName);
        final EditText placeAddress = (EditText) dialog.findViewById(R.id.placeAddress);
        Button addPlaceButton = (Button) dialog.findViewById(R.id.addPlaceButton);

        placeAddress.setText(getCurrentPlaceAddress(location));
        Toast.makeText(this, userLocation.toString(), Toast.LENGTH_LONG).show();


        addPlaceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placeDao.addSuggestedPlaceToDatabase(placeName.getText().toString().trim() + ", "+ placeAddress.getText().toString().trim(),location);
                dialog.cancel();
            }
        });
    }

    private Location getCurrentLocation() {

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, false);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_LOCATION);
        }
        return locationManager.getLastKnownLocation(provider);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        goToLocationZoom(52.23, 21.01, 8);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            return;
        }
        map.setMyLocationEnabled(true);
        map.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                checkIfLocalizationEnabled();
                setGoogleApiClient();

                return false;
            }
        });
    }

    private void setGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        googleApiClient.connect();
    }

    private void checkIfLocalizationEnabled() {
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.gps_off);
            builder.setMessage(R.string.wanna_gps_on);
            builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                }
            });
            builder.setNegativeButton(R.string.no, null);
            builder.create().show();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //location request:::
        //        locationRequest=LocationRequest.create();
        //        locationRequest.setPriority(locationRequest.PRIORITY_HIGH_ACCURACY);
        //        locationRequest.setInterval(1000); //miliseconds
        //        locationServices.FocusedLocationAPI.requestLocationUpdates(googleApiClient,locationRequest,this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @Override
    public void onLocationChanged(Location location) {
        if (location == null) {
            Toast.makeText(this, R.string.cant_get_current_location, Toast.LENGTH_LONG).show();
        } else {

            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, 15);
            map.animateCamera(update);
        }
    }
}

