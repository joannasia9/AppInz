package com.example.asia.jmpro;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.asia.jmpro.MainActivity.ASK_MULTIPLE_PERMISSION_REQUEST_CODE;

public class PermissionsUtils {
    private Context context;
    private Activity currentActivity;

    private PermissionResultCallback permissionResultCallback;

    private ArrayList<String> permissionList = new ArrayList<>();
    private ArrayList<String> listPermissionsNeeded = new ArrayList<>();
    private int reqCode;

    PermissionsUtils(Context context, Activity activity)
    {
        this.context = context;
        this.currentActivity = activity;

        permissionResultCallback= (PermissionResultCallback) context;
    }



    public void checkPermission (ArrayList<String> permissions, int requestCode)
    {
        this.permissionList=permissions;
        this.reqCode=requestCode;

        if(Build.VERSION.SDK_INT >= 23)
        {
            if (checkAndRequestPermissions(permissions, requestCode))
            {
                permissionResultCallback.PermissionGranted(requestCode);
            }
        }
        else
        {
            permissionResultCallback.PermissionGranted(requestCode);
        }

    }


    private  boolean checkAndRequestPermissions(ArrayList<String> permissions,int requestCode) {

        if(permissions.size() > 0)
        {
            listPermissionsNeeded = new ArrayList<>();

            for(String item : permissions)
            {
                int hasPermission = ContextCompat.checkSelfPermission(context, item);

                if (hasPermission != PackageManager.PERMISSION_GRANTED) {
                    listPermissionsNeeded.add(item);
                }

            }

            if (!listPermissionsNeeded.isEmpty())
            {
                ActivityCompat.requestPermissions(currentActivity, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),requestCode);
                return false;
            }
        }

        return true;
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        switch (requestCode)
        {
            case ASK_MULTIPLE_PERMISSION_REQUEST_CODE :
                if(grantResults.length > 0)
                {
                    Map <String, Integer> perms = new HashMap<>();

                    for (int i = 0; i < permissions.length; i++)
                    {
                        perms.put(permissions[i], grantResults[i]);
                    }
                    final ArrayList<String> pendingPermissions=new ArrayList<>();

                    for (int i = 0; i < listPermissionsNeeded.size(); i++)
                    {
                        if (perms.get(listPermissionsNeeded.get(i)) != PackageManager.PERMISSION_GRANTED)
                        {
                            if(ActivityCompat.shouldShowRequestPermissionRationale(currentActivity,listPermissionsNeeded.get(i)))
                                pendingPermissions.add(listPermissionsNeeded.get(i));
                            else
                            {
                                permissionResultCallback.NeverAskAgain(reqCode);
                                Toast.makeText(currentActivity, R.string.go_to_settings, Toast.LENGTH_LONG).show();
                                return;
                            }
                        }

                    }

                    if(pendingPermissions.size() > 0)
                    {
                        for(String item : pendingPermissions){
                            if(pendingPermissions.size() == 2 && item.equals(android.Manifest.permission.ACCESS_COARSE_LOCATION) || item.equals(android.Manifest.permission.ACCESS_FINE_LOCATION)){
                                showLocationPermissionAlertDialog();
                                break;
                            } else if (item.equals(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                                showMemoryPermissionAlertDialog();
                            }

                        }
                    }
                    else
                    {
                        permissionResultCallback.PermissionGranted(reqCode);
                    }
                }
                break;
        }
    }

    private void showMemoryPermissionAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(currentActivity)
                .setMessage(R.string.force_memory_permissions)
                .setCancelable(false)
                .setPositiveButton(context.getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        checkPermission(permissionList,reqCode);
                        dialog.cancel();
                    }
                });
        builder.show();
    }

    private void showLocationPermissionAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(currentActivity)
                .setMessage(R.string.continue_with_permissions)
                .setCancelable(false)
                .setPositiveButton(context.getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        checkPermission(permissionList,reqCode);
                        dialog.cancel();
                    }
                });
        builder.show();
    }

}



