package com.example.asia.jmpro;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asia.jmpro.data.UserRealm;
import com.example.asia.jmpro.data.db.UserDao;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.asia.jmpro.R.id.emailEditText;

public class Registration extends AppCompatActivity {
    Date birthDateDate = null;
    TextView birthDate;
    EditText login, password, repeatedPassword, email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        login = (EditText) findViewById(R.id.loginValueEditText);
        password=(EditText) findViewById(R.id.passwordValueEditText);
        repeatedPassword=(EditText) findViewById(R.id.passwordValue2EditText);
        email = (EditText) findViewById(emailEditText);
        birthDate = (TextView) findViewById(R.id.birthDateTextView);

    }

    public void registerUser(View view) {
        UserRealm userRealm = new UserRealm();
        UserDao userDao = new UserDao(this);

        userRealm.setLogin(login.getText().toString());
        userRealm.setEmail(email.getText().toString());
        userRealm.setPassword(password.getText().toString());
        userRealm.setBirthDate(birthDateDate);
        userDao.insertUser(userRealm);
    }

    private boolean isValidEmail(String s) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email.getText().toString());
        return matcher.matches();
    }

    public void showDialogOnClick(View view) {
        Calendar calendar;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            calendar = Calendar.getInstance();

            DatePickerDialog dialog = new DatePickerDialog(this,android.R.style.Theme_Holo_Light_Dialog_MinWidth,datePickerListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        }
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int birthYear, int monthOfAYear, int dayOfMonth) {
            birthDate.setText(dayOfMonth + "."+ (monthOfAYear+1) + "." + birthYear);

            Calendar calendar;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                calendar = Calendar.getInstance();
                calendar.set(birthYear, monthOfAYear, dayOfMonth);
                birthDateDate = calendar.getTime();
            }
            Toast.makeText(Registration.this, birthDateDate.toString(),Toast.LENGTH_LONG).show();
        }
    };


}
