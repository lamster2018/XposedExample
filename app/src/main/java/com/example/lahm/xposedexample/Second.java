package com.example.lahm.xposedexample;

import android.view.View;
import android.widget.TextView;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Project Name:XposedExample
 * Package Name:com.example.lahm.xposedexample
 * Created by lahm on 2018/6/5 15:55 .
 * <p>
 * Copyright (c) 2016—2017 https://www.lizhiweike.com all rights reserved.
 */
public class Second implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        if (loadPackageParam.packageName.contains("com.lahm.easyprotector")) {
            try {
                XposedHelpers.findAndHookMethod(View.class,
                        "setOnClickListener",
                        View.OnClickListener.class,
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                super.beforeHookedMethod(param);
                                param.args[0] = null;
                            }
                        });
//                XposedBridge.hookAllMethods(TextView.class,
//                        "setText", new XC_MethodHook() {
//                            @Override
//                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                                super.beforeHookedMethod(param);
//                                CharSequence old = (CharSequence) param.args[0];
                //param.args.length长度不等
//                                param.args[0] = "***!";
//                            }
//                        });
//                XposedHelpers.findAndHookMethod(TextView.class,
//                        "setText",
//                        CharSequence.class,
//                        new XC_MethodHook() {
//                            @Override
//                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                                super.beforeHookedMethod(param);
//                                CharSequence old = (CharSequence) param.args[0];
//                                //param.args.length长度为1
//                                param.args[0] = "****";
//                            }
//                        });
            } catch (Exception e) {
                XposedBridge.log("fucku--Exception");
            }
        }
    }
}
