package com.example.asia.jmpro;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asia.jmpro.data.db.UserDao;

import io.realm.ObjectServerError;
import io.realm.Realm;
import io.realm.SyncCredentials;
import io.realm.SyncUser;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 123;
    TextView welcome;
    EditText login, password;
    UserDao userDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userDao = new UserDao(this);
        welcome= (TextView) findViewById(R.id.textView2);
        login= (EditText) findViewById(R.id.loginEditText);
        password= (EditText) findViewById(R.id.passwordEditText);
    }

    public void signIn(View view) {
        if(login.getText().toString().trim().equals("")){
            login.setError(getResources().getString(R.string.required));
        }
        else if(password.getText().toString().length()==0){
            password.setError(getResources().getString(R.string.required));
        } else
        {
            Realm.init(this);
            String authUrl="http://192.168.0.12:9080/auth";
            //SyncCredentials userCredentials=SyncCredentials.usernamePassword(login.getText().toString().trim(),password.getText().toString(),false);

            ////////// temp credentials - only to test app: //////////
            SyncCredentials userCredentials=SyncCredentials.usernamePassword("joannasia.maciak@gmail.com","przysietnica",false);

            SyncUser.loginAsync(userCredentials, authUrl, new SyncUser.Callback() {
                @Override
                public void onSuccess(SyncUser user) {
                    Toast.makeText(getApplicationContext(), R.string.correct_credentials,Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(),MainMenu.class));
                }

                @Override
                public void onError(ObjectServerError error) {
                    login.setError(getString(R.string.wrong_credentials));
                    Toast.makeText(MainActivity.this,getString(R.string.wrong_credentials),Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public void goToRegistrationPanel(View view) {
        startActivityForResult(new Intent(this,Registration.class),REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        welcome.setText(getResources().getString(R.string.welcome));
        if(requestCode==REQUEST_CODE && resultCode==RESULT_OK){
            welcome.setText(data.getStringExtra("success"));
            login.setText(data.getStringExtra("registeredLogin"));
            password.setText(data.getStringExtra("registeredPassword"));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("login", login.getText().toString());
        outState.putString("password",password.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        this.login.setText(savedInstanceState.getString("login", ""));
        this.password.setText(savedInstanceState.getString("password", ""));
    }
}
