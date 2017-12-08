package com.example.asia.jmpro.logic;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by asia on 07/11/2017.
 *
 */

public class DrawableResourceExtrator {


    public static int getResIdFromAttribute(final Context activity, final int attr)
    {
        if(attr==0)
            return 0;
        final TypedValue typedvalueattr=new TypedValue();
        activity.getTheme().resolveAttribute(attr,typedvalueattr,true);
        return typedvalueattr.resourceId;
    }
}
