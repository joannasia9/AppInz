package com.example.asia.jmpro;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asia.jmpro.data.DbConnector;
import com.example.asia.jmpro.data.db.UserDao;
import com.example.asia.jmpro.logic.language.LanguageChangeObserver;
import com.example.asia.jmpro.viewholders.MyBaseActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import io.realm.Realm;
import io.realm.SyncUser;

public class MainActivity extends MyBaseActivity {
    private static final int REQUEST_CODE = 123;
    public static final int MY_PERMISSIONS_REQUEST_ACCESS_LOCATION = 111;
    public static final int MY_PERMISSIONS_REQUEST_ACCESS_MEMORY = 112;
    public static final int ASK_MULTIPLE_PERMISSION_REQUEST_CODE = 113;
    TextView welcome;
    EditText login, password;
    UserDao userDao;
    LanguageChangeObserver languageChangeObserver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userDao = new UserDao();
        welcome = (TextView) findViewById(R.id.textView2);
        login = (EditText) findViewById(R.id.loginEditText);
        password = (EditText) findViewById(R.id.passwordEditText);

        checkAllPermissions();
        languageChangeObserver = new LanguageChangeObserver(this).start();

    }

    public void signIn(View view) {
        if (login.getText().toString().trim().equals("")) {
            login.setError(getResources().getString(R.string.required));
        } else if (password.getText().toString().length() == 0) {
            password.setError(getResources().getString(R.string.required));
        } else {
            final DbConnector dbConnector = DbConnector.getInstance();
            dbConnector.clearData();

            dbConnector.dbConnect(login.getText().toString().trim(), password.getText().toString(), new DbConnector.DBConnectorLoginCallback() {
                @Override
                public void onSuccess(SyncUser user) {

                    dbConnector.connectToDatabase(new DbConnector.DBConnectorDatabaseCallback() {
                        @Override
                        public void onSuccess(Realm realm) {
                            showSignedInUserMainMenuScreen();
                        }

                        @Override
                        public void onError(Throwable exception) {
                            Toast.makeText(MainActivity.this, R.string.db_error, Toast.LENGTH_LONG).show();
                        }
                    });

                    dbConnector.connectToPrivateDatabase(new DbConnector.DBConnectorDatabaseCallback() {
                        @Override
                        public void onSuccess(Realm realm) {
                        }

                        @Override
                        public void onError(Throwable exception) {
                        }
                    });

                }

                @Override
                public void onError(RuntimeException error) {
                    login.setError(getString(R.string.wrong_credentials));
                    Toast.makeText(MainActivity.this, getString(R.string.wrong_credentials), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public void goToRegistrationPanel(View view) {
        startActivityForResult(new Intent(this, Registration.class), REQUEST_CODE);
    }

    private void showSignedInUserMainMenuScreen() {
        startActivity(new Intent(getApplicationContext(), MainMenu.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        welcome.setText(getResources().getString(R.string.welcome));
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            welcome.setText(data.getStringExtra("success"));
            login.setText(data.getStringExtra("registeredLogin"));
            password.setText(data.getStringExtra("registeredPassword"));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("login", login.getText().toString());
        outState.putString("password", password.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        this.login.setText(savedInstanceState.getString("login", ""));
        this.password.setText(savedInstanceState.getString("password", ""));
    }


    ////// PERMISSIONS //////
    private void checkAllPermissions() {
        if (!googleServicesAvailable()) {
            showPlayServicesAlertDialog();
        }
        grantMultiplePermissions();
    }

    private void grantMultiplePermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    ASK_MULTIPLE_PERMISSION_REQUEST_CODE);
        }
    }

    private void grantLocationPermissions() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_LOCATION);
        }
    }

    private void grantMemoryPermissions() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_ACCESS_MEMORY);
        }
    }

    private void grantResultsPermissionsCheck(String[] permissions, int[] grantResults){
        if(grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_DENIED){
            showLocationPermissionAlertDialog();
        }

        if (grantResults.length == 2 && grantResults[1] == PackageManager.PERMISSION_DENIED){
            showMemoryPermissionAlertDialog();
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case ASK_MULTIPLE_PERMISSION_REQUEST_CODE: {
                grantResultsPermissionsCheck(permissions,grantResults);
                break;
            }

            case MY_PERMISSIONS_REQUEST_ACCESS_LOCATION: {
                if (grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    showLocationPermissionAlertDialog();
                }
                break;
            }

            case MY_PERMISSIONS_REQUEST_ACCESS_MEMORY: {
                if (grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    showMemoryPermissionAlertDialog();
                }
                break;
            }

        }
    }

    private boolean googleServicesAvailable() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int isAvailable = googleApiAvailability.isGooglePlayServicesAvailable(this);
        if (isAvailable == ConnectionResult.SUCCESS) {
            return true;
        } else if (googleApiAvailability.isUserResolvableError(isAvailable)) {
            Dialog dialog = googleApiAvailability.getErrorDialog(this, isAvailable, 0);
            dialog.show();
        } else {
            Toast.makeText(this, "Play Services are not available.", Toast.LENGTH_LONG).show();
        }

        return false;
    }

    private void showPlayServicesAlertDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.warning))
                .setMessage(R.string.playservices_required)
                .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        openGooglePlayToGetPlayServices();
                        dialog.cancel();
                    }
                })
                .setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .create();
        builder.show();
    }

    private void openGooglePlayToGetPlayServices() {
        final String appPackageName = "http://google-play-services.en.uptodown.com/android/download";
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            Log.e("ActivityNotFound", "openGooglePlayToGetMaps: " + anfe.getMessage());
        }
    }

    private void showLocationPermissionAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setMessage(R.string.continue_with_permissions)
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        grantLocationPermissions();
                        dialog.cancel();
                    }
                });
        builder.show();
    }

    private void showMemoryPermissionAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setMessage(R.string.force_memory_permissions)
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        grantMemoryPermissions();
                        dialog.cancel();
                    }
                });
        builder.show();
    }
}

