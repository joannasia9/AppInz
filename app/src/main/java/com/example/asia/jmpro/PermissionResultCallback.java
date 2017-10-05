package com.example.asia.jmpro;

import java.util.ArrayList;

/**
 * Created by asia on 05/10/2017.
 */

public interface PermissionResultCallback
        {
        void PermissionGranted(int requestCode);
        void PartialPermissionGranted(int requestCode, ArrayList<String> grantedPermissions);
        void PermissionDenied(int requestCode);
        void NeverAskAgain(int requestCode);
    }

