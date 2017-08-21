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

import com.example.asia.jmpro.data.UserRealm;
import com.example.asia.jmpro.data.db.UserDao;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.asia.jmpro.R.id.emailEditText;

public class Registration extends AppCompatActivity {
    String birthDateString;
    TextView birthDate;
    EditText login, password, repeatedPassword, email;
    int day,month,year;

    static final int DIALOG_ID = 0;

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

    public void registerUser(View view){
        UserRealm userRealm = new UserRealm();
        UserDao userDao = new UserDao(this);
        userRealm.setLogin(login.getText().toString());
        userRealm.setEmail("joasia42@interia.eu");
        userRealm.setPassword(password.getText().toString());
        userRealm.setBirthDate("16.07.1993");
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
            day= calendar.get(Calendar.DAY_OF_MONTH);
            month=calendar.get(Calendar.MONTH);
            year=calendar.get(Calendar.YEAR);

            DatePickerDialog dialog = new DatePickerDialog(this,android.R.style.Theme_Holo_Light_Dialog_MinWidth,datePickerListener,year,month,day);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        }
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int birthYear, int monthOfAYear, int dayOfMonth) {
            year = birthYear;
            month = monthOfAYear+1;
            day = dayOfMonth;

            birthDate.setText(day + "."+ month + "." + year);
        }
    };


}
