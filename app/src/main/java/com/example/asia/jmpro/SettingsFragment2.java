package com.example.asia.jmpro;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.asia.jmpro.data.UserRealm;
import com.example.asia.jmpro.data.db.UserDao;
import com.example.asia.jmpro.logic.validation.EmailValidator;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

/**
 * Created by asia on 24/08/2017.
 *
 */

public class SettingsFragment2 extends Fragment {
    TextView login;
    TextView birthDate;
    TextView email;
    EditText oldPassword;
    EditText newPassword;
    EditText newPasswordRepeated;
    EditText newEmail;
    UserDao userDao = new UserDao();
    UserRealm userRealm;
    Button saveAccountChanges;
    Button removeAccount;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View fragmentLayout = inflater.inflate(R.layout.settings_fragment2, container, false);
        Settings.hideKeyboard(getActivity());
        login = fragmentLayout.findViewById(R.id.settingsLogin);
        birthDate = fragmentLayout.findViewById(R.id.settingsBirthDate);
        email = fragmentLayout.findViewById(R.id.settingsEmail);
        oldPassword = fragmentLayout.findViewById(R.id.settingsOldPassword);
        newPassword = fragmentLayout.findViewById(R.id.settingsNewPassword);
        newPasswordRepeated = fragmentLayout.findViewById(R.id.settingsNewPasswordRepeated);
        newEmail = fragmentLayout.findViewById(R.id.settingsNewEmail);
        saveAccountChanges = fragmentLayout.findViewById(R.id.saveAccount);
        removeAccount = fragmentLayout.findViewById(R.id.removeAccount);

        userRealm = userDao.getUserRealmFromDatabase();
        String loginString = getResources().getString(R.string.settings_login) + " " + getLoginFromDatabase();
        String emailFromDb = getResources().getString(R.string.e_mail) + ": " + getEmailFromDatabase();
        String birthDateFromDb = getResources().getString(R.string.birth_date) + " " + getBirthDateFromDatabase();
        login.setText(loginString);
        oldPassword.setText(getOldPasswordFromDatabase());
        email.setText(emailFromDb);
        birthDate.setText(birthDateFromDb);

        saveAccountChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUsersData(newPassword, newPasswordRepeated, newEmail);
            }
        });

        removeAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogMessage();
            }
        });


        return fragmentLayout;
    }

    private void showDialogMessage(){
        if(getActivity() != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.warning)
                    .setMessage(R.string.are_you_sure)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            userDao.deleteUser();
                            startActivity(new Intent(getContext(), MainActivity.class));
                        }
                    })
                    .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .create();
            builder.show();
        }
    }
    private void updateUsersData(EditText password, EditText repeatedPassword, EditText newEmail) {
        if (isValidNewPassword(password, repeatedPassword)) {
            userDao.updateUserPassword(getContext(),password.getText().toString().trim());
            showSuccessDialog(getString(R.string.password));
        } else if (isValidNewEmail(newEmail)) {
            userDao.updateUserEmail(newEmail.getText().toString().trim());
            showSuccessDialog(getString(R.string.e_mail));
        }
    }

    private boolean isValidNewEmail(EditText newEmail) {
        if (newEmail.getText().toString().trim().length() != 0) {
            EmailValidator validator = new EmailValidator();

            if (validator.validate(newEmail.getText().toString().trim())) {
                return true;
            } else {
                newEmail.setError(getString(R.string.incorrect_email));
                return false;
            }

        } else return false;
    }

    private boolean isValidNewPassword(EditText p1, EditText p2) {
        String password1 = p1.getText().toString().trim();
        String password2 = p2.getText().toString().trim();

        if (password1.length() != 0 || password2.length() != 0) {
            if (password1.length() < 6) {
                p1.setError(getString(R.string.password_signs));
                return false;
            } else if (!Objects.equals(p1.getText().toString(), password2)) {
                p1.setError(getString(R.string.passwords_not_matched));
                p2.setText("");
                return false;
            } else return true;

        } else return false;
    }

    private String getLoginFromDatabase() {
        return userRealm.getLogin();
    }

    private String getOldPasswordFromDatabase() {
        return userRealm.getPassword();
    }

    private String getEmailFromDatabase() {
        return userRealm.getEmail();
    }

    private String getBirthDateFromDatabase() {
        String DATE_FORMAT = "dd.MM.yyyy";
        Date date = userRealm.getBirthDate();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
        return simpleDateFormat.format(date);
    }

    private void showSuccessDialog(String name){
        if(getContext() != null) {
            AlertDialog builder = new AlertDialog.Builder(getContext())
                    .setTitle(R.string.eureka)
                    .setMessage(R.string.updated_successful + " " + name)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .create();
            builder.show();
        }
    }
}
