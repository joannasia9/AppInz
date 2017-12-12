package com.example.asia.jmpro;

import android.Manifest;
import android.app.Activity;
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
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ShareCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asia.jmpro.adapters.SuggestedPlacesListAdapter;
import com.example.asia.jmpro.data.Place;
import com.example.asia.jmpro.data.SuggestedPlace;
import com.example.asia.jmpro.data.db.PlaceDao;
import com.example.asia.jmpro.data.db.UserDao;
import com.example.asia.jmpro.logic.language.PreferencesChangeObserver;
import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.MessageDialog;
import com.facebook.share.widget.ShareDialog;
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
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.asia.jmpro.MainActivity.MY_PERMISSIONS_REQUEST_ACCESS_LOCATION;


public class MainMenuPlaces extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    public static final int REQUEST_CODE = 112;
    PlaceDao placeDao = new PlaceDao(this);
    Dialog dialog;
    DrawerLayout drawer;
    TextView optionsTitle;
    EditText placeAddress;
    String[] placesOptions;
    GoogleMap map;
    GoogleApiClient googleApiClient;
    SuggestedPlacesListAdapter suggestedPlacesListAdapter;
    Location userLocation = new Location("");
    List<Place> placesList;
    ArrayList<Place> selectedPlacesList = new ArrayList<>();
    List<SuggestedPlace> suggestedPlacesList;
    PreferencesChangeObserver preferencesChangeObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(MyApp.getThemeId(this));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu_places);

        optionsTitle = findViewById(R.id.optionTitle);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab_add_to_fav);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLocation = getCurrentLocation();
                if (userLocation != null) {
                    showAddFavouritePlaceDialog();
                } else {
                    checkIfLocalizationEnabled();
                }
            }
        });

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        placesOptions = getApplicationContext().getResources().getStringArray(R.array.places_options);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        selectItem(0);
        initMap();

        preferencesChangeObserver = new PreferencesChangeObserver(this).start();


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_favourite_places) {
            selectItem(0);
            showAllFavouritePlaces(placeDao.getUsersFavouritePlacesList());
        } else if (id == R.id.nav_suggested_places) {
            selectItem(1);
        } else if (id == R.id.nav_suggest) {
            selectItem(2);
        } else if (id == R.id.nav_share_email) {
            selectItem(3);
        } else if (id == R.id.nav_share_facebook) {
            selectItem(4);
        } else if (id == R.id.nav_share_messenger) {
            selectItem(5);
        } else if (id == R.id.nav_share_hangouts) {
            selectItem(6);
        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
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
                showAllSuggestedPlaces(placeDao.getSuggestedPlaces());
                break;
            case 2:
                optionsTitle.setText(placesOptions[position]);
                userLocation = getCurrentLocation();
                if (userLocation != null) {
                    showAddSuggestedPlaceDialog();
                } else {
                    checkIfLocalizationEnabled();
                }
                break;

            case 3:
                showSelectPlacesDialog(3, SuggestedPlacesListAdapter.SELECT_CODE);
                break;

            case 4:
                showSelectPlacesDialog(4, SuggestedPlacesListAdapter.SELECT_FOR_SHARE_VIA_FACEBOOK_CODE);
                break;

            case 5:
                showSelectPlacesDialog(5, SuggestedPlacesListAdapter.SELECT_FOR_SHARE_VIA_FACEBOOK_CODE);
                break;
            case 6:
                showSelectPlacesDialog(6, SuggestedPlacesListAdapter.SELECT_CODE);
                break;

            default:
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
    }


    private void initMap() {
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);
    }

    private void goToLocationZoom(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 8);
        map.moveCamera(cameraUpdate);

    }

    private void setPlaceMarker(String locality, double lat, double lng) {
        String[] localityPartsArray = locality.split("\\s*,\\s*");
        MarkerOptions options = new MarkerOptions()
                .title(localityPartsArray[0])
                .position(new LatLng(lat, lng))
                .snippet(localityPartsArray[1]);
        map.addMarker(options);
    }

    private void showAllFavouritePlaces(List<Place> placesList) {
        map.clear();

        for (Place item : placesList) {
            setPlaceMarker(item.getName(), item.getLatitude(), item.getLongitude());
        }
    }

    private void showAllSuggestedPlaces(List<SuggestedPlace> suggestedPlacesList) {
        map.clear();

        if (suggestedPlacesList.size() != 0) {
            for (SuggestedPlace item : suggestedPlacesList) {
                setPlaceMarker(item.getName(), item.getLatitude(), item.getLongitude());
            }
        }
    }

    private String getCurrentPlaceAddress(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this);
        List<Address> list;
        String localityName = "";

        try {
            list = geocoder.getFromLocation(latitude, longitude, 1);
            Address address = list.get(0);

            localityName = getString(R.string.street) + address.getAddressLine(0) + ", \n" + address.getAddressLine(1);


        } catch (IOException e) {
            e.printStackTrace();
        }
        return localityName;
    }

    private void showAddFavouritePlaceDialog() {

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.add_place_dialog);
        dialog.setTitle(R.string.add_fav_place);
        dialog.show();

        final EditText placeName = dialog.findViewById(R.id.placeName);
        placeAddress = dialog.findViewById(R.id.placeAddress);
        Button addPlaceButton = dialog.findViewById(R.id.addPlaceButton);
        Button cancelButton = dialog.findViewById(R.id.button10);
        ImageView locationChooser = dialog.findViewById(R.id.locationChooserImageView);

        placeAddress.setText(getCurrentPlaceAddress(userLocation.getLatitude(), userLocation.getLongitude()));

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        addPlaceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (placeName.getText().toString().trim().length() == 0) {
                    placeName.setError(getString(R.string.required));
                } else {
                    placeDao.addFavouritePlaceToDatabase(placeName.getText().toString().trim() + ", " + placeAddress.getText().toString().trim(), userLocation);
                    showAllFavouritePlaces(placeDao.getUsersFavouritePlacesList());
                    dialog.cancel();
                }
            }
        });

        locationChooser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                Intent intent = new Intent(getApplicationContext(), ManualLocationSetter.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

    }

    private void showAddSuggestedPlaceDialog() {

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.add_suggested_place_dialog);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        Button okButton = dialog.findViewById(R.id.hideDialogButton);
        Button cancelButton = dialog.findViewById(R.id.cancelButtonDialog);
        ListView suggestedPlacesListView = dialog.findViewById(R.id.favouritePlacesList);
        TextView title = dialog.findViewById(R.id.suggestPlaceDialogTitle);
        title.setText(getString(R.string.add_sug_place));


        showPlacesList(suggestedPlacesListView, SuggestedPlacesListAdapter.SUGGEST_CODE);

        suggestedPlacesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Place model = placesList.get(position);
                placeDao.addSuggestedPlaceToDatabase(model);
                suggestedPlacesListAdapter.updateAdapter(placeDao.getAllSuggestedPlacesList(), selectedPlacesList);
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                showAllSuggestedPlaces(placeDao.getSuggestedPlaces());
            }
        });

    }

    private void showSelectPlacesDialog(final int choice, final int code) {
        selectedPlacesList.clear();
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.add_suggested_place_dialog);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        Button okButton = dialog.findViewById(R.id.hideDialogButton);
        Button cancelButton = dialog.findViewById(R.id.cancelButtonDialog);

        TextView title = dialog.findViewById(R.id.suggestPlaceDialogTitle);
        TextView title2 = dialog.findViewById(R.id.textView22);

        title.setText(getString(R.string.select_place_to_share));
        title2.setText(getString(R.string.select_place_to_share2));

        ListView suggestedPlacesListView = dialog.findViewById(R.id.favouritePlacesList);
        showPlacesList(suggestedPlacesListView, SuggestedPlacesListAdapter.SELECT_CODE);

        suggestedPlacesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Place model = placesList.get(position);
                selectedPlacesList.add(model);

                if (code == SuggestedPlacesListAdapter.SELECT_FOR_SHARE_VIA_FACEBOOK_CODE) {
                    selectedPlacesList.clear();
                    selectedPlacesList.add(model);
                }

                suggestedPlacesListAdapter.updateAdapter(placeDao.getAllSuggestedPlacesList(), selectedPlacesList);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //selectedPlacesList
                switch (choice) {
                    case 3:
                        if(selectedPlacesList.size() != 0) {
                            shareViaEmail(selectedPlacesList);
                        }
                        break;
                    case 4:
                        if(selectedPlacesList.size() != 0) {
                            shareViaFacebook(selectedPlacesList.get(0));
                        }
                        break;
                    case 5:
                        if(selectedPlacesList.size() != 0) {
                            shareViaMessenger(selectedPlacesList.get(0));
                        }
                        break;
                    case 6:
                        if(selectedPlacesList.size() != 0) {
                            shareViaHangouts(selectedPlacesList);
                        }
                    default:
                        if(selectedPlacesList.size() != 0) {
                            shareSelectedPlaces(selectedPlacesList);
                        }
                        break;
                }
                dialog.cancel();
            }
        });
    }

    private void shareSelectedPlaces(ArrayList<Place> selectedPlaces){
        StringBuilder builder = new StringBuilder();
        builder.append(getString(R.string.recommended_places))
                .append("\n")
                .append("\n")
                .append(getString(R.string.addresses))
                .append("\n");

        for (Place item : selectedPlaces) {
            builder.append(getString(R.string.arrow)).append(item.getName()).append("\n").append("\n");
        }
        String sharedText = builder.toString();

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_TITLE, getString(R.string.recommended_places));
        sharingIntent.putExtra(Intent.EXTRA_TEXT, sharedText);
        startActivity(Intent.createChooser(sharingIntent, getString(R.string.share_using)));


    }

    private void shareViaHangouts(ArrayList<Place> selectedPlaces) {
        StringBuilder builder = new StringBuilder();
        builder.append(getString(R.string.recommended_places))
                .append("\n")
                .append("\n")
                .append(getString(R.string.addresses))
                .append("\n");

        for (Place item : selectedPlaces) {
            builder.append(getString(R.string.arrow)).append(item.getName()).append("\n").append("\n");
        }
        String sharedText = builder.toString();

        Intent shareIntent = ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setText(sharedText)
                .getIntent();

        if (isAppInstalled("com.google.android.talk")) {
            shareIntent.setPackage("com.google.android.talk");
            startActivity(shareIntent);
        }
    }

    private void shareViaFacebook(Place selectedPlace) {
        String singleUri;
        singleUri = "https://www.google.com/maps/search/?api=1&query=" + selectedPlace.getLatitude() + "," + selectedPlace.getLongitude();

        if (isAppInstalled("com.facebook.katana")) {

            ShareDialog shareDialog = new ShareDialog(this);
            if (ShareDialog.canShow(ShareLinkContent.class)) {
                ShareHashtag hashTag = new ShareHashtag.Builder().setHashtag("#" + getString(R.string.app_name)).build();

                ShareLinkContent linkContent = new ShareLinkContent.Builder()
                        .setContentUrl(Uri.parse(singleUri))
                        .setShareHashtag(hashTag)
                        .build();

                shareDialog.show(linkContent);

            }
        }
    }

    private void shareViaMessenger(Place selectedPlace) {
        String singleUri;
        singleUri = "https://www.google.com/maps/search/?api=1&query=" + selectedPlace.getLatitude() + "," + selectedPlace.getLongitude();

        if (isAppInstalled("com.facebook.orca")) {

            MessageDialog messageDialog = new MessageDialog(this);
            if (MessageDialog.canShow(ShareLinkContent.class)) {
                ShareHashtag hashTag = new ShareHashtag.Builder().setHashtag("#" + getString(R.string.app_name)).build();
                ShareLinkContent linkContent = new ShareLinkContent.Builder()
                        .setContentUrl(Uri.parse(singleUri))
                        .setShareHashtag(hashTag)
                        .build();

                messageDialog.show(linkContent);

            }
        }
    }

    private void shareViaEmail(ArrayList<Place> selectedPlaces) {
        UserDao user = new UserDao();

        StringBuilder builder = new StringBuilder();
        builder.append(getString(R.string.html_pack))
                .append("</b></font>");
        for (Place item : selectedPlaces) {
            builder.append("<p><font color=\"6699ff\" face =\"Times New Roman\">").append(item.getName()).append("</font></p>");
        }
        String sharedText = builder.toString();

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setData(Uri.parse(getString(R.string.mailto)));
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, user.getUserRealmFromDatabase().getLogin() + getString(R.string.html_pack_subject));
        shareIntent.putExtra(Intent.EXTRA_TEXT, fromHtml(sharedText));
        shareIntent.setType("text/html");

        Intent chooser = Intent.createChooser(shareIntent, getString(R.string.e_mail));

        if (shareIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(chooser);
        }
    }

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html) {
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }

    private boolean isAppInstalled(final String uri) {
        PackageManager pm = getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.app_does_not_exist))
                    .setMessage(getString(R.string.want_to_install_question))
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            openPlayStoreToGetApp(uri);
                        }
                    })
                    .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .create();
            builder.show();
            return false;
        }
    }

    private void openPlayStoreToGetApp(String uri) {
        final String appPackageName = "https://play.google.com/store/apps/details?id=" + uri;
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            Log.e("ActivityNotFound", "openGooglePlayToGetMaps: " + anfe.getMessage());
        }
    }

    private void showPlacesList(ListView list, int code) {
        placesList = placeDao.getUsersFavouritePlacesList();
        suggestedPlacesList = placeDao.getAllSuggestedPlacesList();

        suggestedPlacesListAdapter = new SuggestedPlacesListAdapter(this, placesList, suggestedPlacesList, selectedPlacesList, code);
        list.setAdapter(suggestedPlacesListAdapter);
    }

    private Location getCurrentLocation() {
        Location location = new Location("");
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        if(locationManager!=null) {
            String provider = locationManager.getBestProvider(criteria, false);
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_LOCATION);
            }
            location = locationManager.getLastKnownLocation(provider);
        }
        return location;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            double latitude = data.getDoubleExtra("lat", getCurrentLocation().getLatitude());
            double longitude = data.getDoubleExtra("lng", getCurrentLocation().getLongitude());

            userLocation.setLatitude(latitude);
            userLocation.setLongitude(longitude);

            showAddFavouritePlaceDialog();
            placeAddress.setText(getCurrentPlaceAddress(latitude, longitude));
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        if(getCurrentLocation()!= null) {
            goToLocationZoom(getCurrentLocation());
        } else {
            checkIfLocalizationEnabled();
        }

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

        showAllFavouritePlaces(placeDao.getUsersFavouritePlacesList());
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
        if (locationManager!=null && !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
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

