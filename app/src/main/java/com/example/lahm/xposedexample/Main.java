package com.example.lahm.xposedexample;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.util.SparseArray;

import java.lang.reflect.Field;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedHelpers.findClass;


/**
 * Project Name:XposedExample
 * Package Name:com.example.lahm.xposedexample
 * Created by lahm on 2017/12/25 下午12:54 .
 * <p>
 * Copyright (c) 2016—2017 https://www.lizhiweike.com all rights reserved.
 */

public class Main implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        if (loadPackageParam.packageName.contains("com.tencent")) {
            try {
                XposedBridge.hookAllMethods(
                        findClass("android.hardware.SystemSensorManager$SensorEventQueue",
                                loadPackageParam.classLoader),
                        "dispatchSensorEvent",
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                super.beforeHookedMethod(param);
                                int intValue = ((Integer) param.args[0]).intValue();
                                Field declaredField = param.thisObject.getClass().getDeclaredField("mSensorsEvents");
                                declaredField.setAccessible(true);
                                Sensor sensor = ((SensorEvent)
                                        ((SparseArray) declaredField.get(param.thisObject)).get(intValue)).sensor;
                                if (sensor == null) {
                                    XposedBridge.log("fucku---传感器为NULL");
                                    return;
                                }
                                int sensorType = sensor.getType();
                                if (sensorType == 19 || sensorType == 18) {
                                    //500倍差不多了
                                    float step = ((float[]) param.args[1])[0] * 500;
                                    XposedBridge.log("fucku---" + loadPackageParam.packageName
                                            + " 传感器类型 " + sensorType
                                            + " 当前步数 " + ((float[]) param.args[1])[0]
                                            + " 修改 " + step);
                                    ((float[]) param.args[1])[0] = step;
                                }
                            }
                        });
            } catch (Exception e) {
                XposedBridge.log("fucku--caonimashabi");
            }
        }


    }
}