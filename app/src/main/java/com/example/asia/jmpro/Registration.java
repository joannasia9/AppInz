package com.example.asia.jmpro;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.asia.jmpro.data.DbConnector;
import com.example.asia.jmpro.data.db.UserDao;
import com.example.asia.jmpro.logic.language.LanguageChangeObserver;
import com.example.asia.jmpro.logic.validation.EmailValidator;
import com.example.asia.jmpro.viewholders.MyBaseActivity;

import java.util.Date;
import java.util.Objects;

import io.realm.Realm;
import io.realm.SyncUser;

import static com.example.asia.jmpro.R.id.emailEditText;

public class Registration extends MyBaseActivity {
    Date birthDateDate = null;
    TextView birthDate;
    EditText login, password, repeatedPassword, email;
    LanguageChangeObserver languageChangeObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        login = (EditText) findViewById(R.id.loginEditText);
        password = (EditText) findViewById(R.id.passwordEditText);
        repeatedPassword = (EditText) findViewById(R.id.passwordValue2EditText);
        email = (EditText) findViewById(emailEditText);
        birthDate = (TextView) findViewById(R.id.birthDateTextView);

        languageChangeObserver = new LanguageChangeObserver(this).start();

    }

    public void registerUser(View view) {

        if (isUserValid(login, password, repeatedPassword, email, birthDateDate)) {
            final DbConnector dbConnector = DbConnector.getInstance();
            dbConnector.clearData();

            dbConnector.registerNewUser(login, password, new DbConnector.DBConnectorRegistrationCallback() {
                @Override
                public void onRegistrationSuccess(final SyncUser user) {
                    dbConnector.setSyncUser(user);
                    dbConnector.setConfiguration(user);

                    dbConnector.connectToDatabase(new DbConnector.DBConnectorDatabaseCallback() {
                        @Override
                        public void onSuccess(Realm realm) {
                            UserDao userDao = new UserDao();

                            userDao.insertUser(login, password, email, birthDateDate, new UserDao.UserRegistrationCallback() {
                                @Override
                                public void onUserRegistrationSuccess() {
                                    showRegisterUserSuccessfulScreen();
                                }
                            });

                        }

                        @Override
                        public void onError(Throwable exception) {

                        }
                    });
                }

                @Override
                public void onRegistrationFailure(Throwable error) {
                    login.setError(getResources().getString(R.string.occupied_login));
                }
            });
        }
    }

    private void showRegisterUserSuccessfulScreen() {
        Intent intent = new Intent();
        intent.putExtra("registeredLogin", login.getText().toString().trim());
        intent.putExtra("registeredPassword", password.getText().toString().trim());
        intent.putExtra("success", getResources().getString(R.string.registered));
        setResult(RESULT_OK, intent);
        finish();
    }

    private boolean isUserValid(EditText login, EditText password, EditText rPassword, EditText email, Date birthDate) {
        return isValidLogin(login) && isValidPassword(password, rPassword) && isValidEmail(email) && isValidBirthDate(birthDate);
    }

    private boolean isValidBirthDate(Date bDate) {
        if (bDate == null) {
            birthDate.setText(getString(R.string.choose_date));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                birthDate.setTextColor(getResources().getColor(R.color.errorColor, getTheme()));
            }
            return false;
        } else return true;
    }

    private boolean isValidLogin(EditText l) {
        String login = l.getText().toString().trim();
        if (login.equals("")) {
            l.setError(getString(R.string.required));
            return false;
        } else return true;
    }

    private boolean isValidPassword(EditText p1, EditText p2) {
        if (p1.getText().toString().trim().length() < 6) {
            p1.setError(getString(R.string.password_signs));
            return false;
        } else if (!Objects.equals(p1.getText().toString(), p2.getText().toString())) {
            p1.setError(getString(R.string.passwords_not_matched));
            p2.setText("");
            return false;
        } else return true;
    }

    private boolean isValidEmail(EditText s) {
        EmailValidator validator = new EmailValidator();
        String emailValue = s.getText().toString().trim();

        if (validator.validate(emailValue)) {
            return true;
        } else {
            email.setError(getString(R.string.incorrect_email));
            return false;
        }
    }

    public void showDialogOnClick(View view) {
        Calendar calendar;
            calendar = Calendar.getInstance();

            DatePickerDialog dialog = new DatePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, datePickerListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int birthYear, int monthOfAYear, int dayOfMonth) {
            birthDate.setText(dayOfMonth + "." + (monthOfAYear + 1) + "." + birthYear);
            birthDate.setTextColor(getResources().getColor(R.color.colorBlack, null));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(birthYear, monthOfAYear, dayOfMonth);
                birthDateDate = calendar.getTime();
            }
        }
    };
}
