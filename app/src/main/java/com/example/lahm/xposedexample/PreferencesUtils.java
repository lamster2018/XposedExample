package com.example.lahm.xposedexample;

import de.robv.android.xposed.XSharedPreferences;

/**
 * Project Name:XposedExample
 * Package Name:com.example.lahm.xposedexample
 * Created by lahm on 2018/3/28 下午4:48 .
 */

public class PreferencesUtils {
    private static XSharedPreferences instance = null;

    //加上这个XSP就不能修改成功，这里应该有bug
    private static XSharedPreferences getInstance() {
        if (instance == null) {
            instance = new XSharedPreferences(PreferencesUtils.class.getPackage().getName());
            instance.makeWorldReadable();
        } else {
            instance.reload();
        }
        return instance;
    }

    public static boolean isOpen() {
        return getInstance().getBoolean("open", true);
    }

    public static int getRatio() {
        return Integer.parseInt(getInstance().getString("ratio", "").replace("，", ","));
    }

}
