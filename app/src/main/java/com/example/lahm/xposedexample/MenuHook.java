package com.example.lahm.xposedexample;

import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Project Name:XposedExample
 * Package Name:com.example.lahm.xposedexample
 * Created by lahm on 2017/12/25 下午2:39 .
 * <p>
 * Copyright (c) 2016—2017 https://www.lizhiweike.com all rights reserved.
 */

public class MenuHook implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        if (loadPackageParam.packageName.contains("com.tencent")) {
            XposedHelpers.findAndHookMethod("com.tencent.mm.ui.LauncherUI",
                    loadPackageParam.classLoader,
                    "onCreateOptionsMenu",
                    Menu.class,
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            super.beforeHookedMethod(param);
                            final Context applicationContext = null;
                            Menu menu = (Menu) param.args[0];
                            menu.add(0, 3, 0, "csb11");
                            menu.add(0, 3, 0, "csb12");
                            menu.add(0, 3, 0, "csb13");
                            for (int i = 0; i < menu.size(); i++) {
                                final int finalI = i;
                                menu.getItem(i).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                                    @Override
                                    public boolean onMenuItemClick(MenuItem item) {
                                        XposedBridge.log("fucku--" + finalI);
//                                        Toast.makeText(applicationContext, "click" + finalI, Toast.LENGTH_SHORT).show();
                                        return false;
                                    }
                                });
                            }
                        }
                    });
        }
    }
}




















