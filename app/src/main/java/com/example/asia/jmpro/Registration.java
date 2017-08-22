package com.example.asia.jmpro;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.asia.jmpro.data.UserRealm;
import com.example.asia.jmpro.data.db.UserDao;

import java.util.Date;
import java.util.Objects;
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

        if(isValidLogin(userDao,login)) {
            userRealm.setLogin(login.getText().toString());
        }

        if (isValidEmail(email.getText().toString())) {
            userRealm.setEmail(email.getText().toString());
        } else{
            email.setError(getString(R.string.incorrect_email));
        }

        if(isValidPassword(password,repeatedPassword)) {
            userRealm.setPassword(password.getText().toString());
        }

        if(birthDateDate!=null) {
            userRealm.setBirthDate(birthDateDate);
        } else {
            birthDate.setText(getString(R.string.choose_date));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                birthDate.setTextColor(getResources().getColor(R.color.errorColor,getTheme()));
            }
        }

        if(isUserCompleted(userRealm)) {
            userDao.insertUser(userRealm);
            Intent intent = new Intent();
            intent.putExtra("registeredLogin",login.getText().toString());
            intent.putExtra("registeredPassword",password.getText().toString());
            setResult(RESULT_OK,intent);
            finish();
        }
    }

    private boolean isUserCompleted(UserRealm user){
        return user.getLogin() != null && user.getPassword() != null && user.getBirthDate() != null && user.getEmail() != null;
}

    private boolean isValidLogin(UserDao u, EditText l){
        if(l.getText().toString().equals("")){
            l.setError(getString(R.string.required));
            return false;
        } else if(u.getUserByLogin(l.getText().toString())){
            l.setError(getString(R.string.occupied_login));
            return false;
        } else return true;
    }

    private boolean isValidPassword(EditText p1, EditText p2){
        if(p1.getText().toString().length() < 6){
            p1.setError(getString(R.string.password_signs));
            return false;
        } else if (!Objects.equals(p1.getText().toString(), p2.getText().toString())){
            p1.setError(getString(R.string.passwords_not_matched));
            return false;
        } else return true;
    }

    private boolean isValidEmail(String s) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(s);
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
        }
    };
}
