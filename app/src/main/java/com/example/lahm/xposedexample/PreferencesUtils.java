package com.example.lahm.xposedexample;

import de.robv.android.xposed.XSharedPreferences;

/**
 * Project Name:XposedExample
 * Package Name:com.example.lahm.xposedexample
 * Created by lahm on 2018/3/28 下午4:48 .
 */

public class PreferencesUtils {
    private static XSharedPreferences instance = null;

    private static XSharedPreferences getInstance() {
        if (instance == null) {
            instance = new XSharedPreferences(PreferencesUtils.class.getPackage().getName());
            instance.makeWorldReadable();
        } else {
            instance.reload();
        }
        return instance;
    }

    public static boolean open() {
        return getInstance().getBoolean("open", false);
    }

    public static String notContains() {
        return getInstance().getString("not_contains", "").replace("，", ",");
    }

}