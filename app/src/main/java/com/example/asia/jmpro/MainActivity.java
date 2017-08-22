package com.example.asia.jmpro;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.asia.jmpro.data.db.UserDao;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 123;
    TextView welcome;
    EditText login, password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        welcome= (TextView) findViewById(R.id.textView2);
        login= (EditText) findViewById(R.id.loginEditText);
        password= (EditText) findViewById(R.id.passwordEditText);


    }

    public void signIn(View view) {
       UserDao userDao = new UserDao(this);

        if(login.getText().toString().equals("")){
            login.setError(getResources().getString(R.string.required));
        } else if(!userDao.getUserByLogin(login.getText().toString())){
            login.setError(getString(R.string.user_doesnt_exist));
        } else
        {
            if(!userDao.getUserByLoginAndPassword(login.getText().toString(),password.getText().toString())&&password.getText().toString().equals("")){
                password.setError(getString(R.string.wrong_password));
            } else {
                startActivity(new Intent(this,MainMenu.class));
            }
        }

    }

    public void goToRegistrationPanel(View view) {
        startActivityForResult(new Intent(this,Registration.class),REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE && resultCode==RESULT_OK){
            welcome.setText(R.string.registered);
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
