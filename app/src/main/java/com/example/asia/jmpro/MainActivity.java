package com.example.asia.jmpro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asia.jmpro.data.DbConnector;
import com.example.asia.jmpro.data.db.UserDao;
import com.example.asia.jmpro.logic.language.LanguageChangeObserver;
import com.example.asia.jmpro.viewholders.MyBaseActivity;

import io.realm.Realm;
import io.realm.SyncUser;

public class MainActivity extends MyBaseActivity {
    private static final int REQUEST_CODE = 123;
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
}
