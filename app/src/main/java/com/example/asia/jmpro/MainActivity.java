package com.example.asia.jmpro;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.asia.jmpro.data.db.UserDao;

public class MainActivity extends AppCompatActivity {
    EditText login, password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login= (EditText) findViewById(R.id.loginEditText);
        password= (EditText) findViewById(R.id.passwordEditText);

    }

    public void signIn(View view) {
       UserDao userDao = new UserDao(this);
        //does user exist?
       if(userDao.getUserByLogin(login.getText().toString())){
           startActivity(new Intent(this,MainMenu.class));
       } else{
           Toast.makeText(this, "User does not exist, register now.", Toast.LENGTH_LONG).show();
       }
    }

    public void goToRegistrationPanel(View view) {
        startActivity(new Intent(this,Registration.class));
    }
}
