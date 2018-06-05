package com.example.lahm.xposedexample;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;

import java.lang.reflect.Array;
import java.lang.reflect.Field;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static de.robv.android.xposed.XposedHelpers.findClass;


/**
 * Project Name:XposedExample
 * Package Name:com.example.lahm.xposedexample
 * Created by lahm on 2017/12/25 下午12:54 .
 */

public class Main implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        if (loadPackageParam.packageName.contains("com.tencent.mm")) {
            try {
                hookBushuByFindAndHook(loadPackageParam);
            } catch (Exception e) {
                log("exception");
            }
        }
    }

    private void log(String msg) {
        XposedBridge.log("lahm--" + msg);
    }

    private void hookBushuByHookAll(final XC_LoadPackage.LoadPackageParam loadPackageParam) {
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
                        int sensorType = sensor.getType();
                        if (sensor == null) {
                            log("传感器为NULL");
                            return;
                        }
                        log("传感器不为NULL， 传感器类型 " + sensorType);
                        if (sensorType == 19 || sensorType == 18) {
                            //500倍差不多了
                            float step = ((float[]) param.args[1])[0] * 101;
                            log(loadPackageParam.packageName
                                    + " 当前步数 " + ((float[]) param.args[1])[0]
//                                            + " 倍数 " + PreferencesUtils.getRatio()
                                    + " 修改 " + step);
                            ((float[]) param.args[1])[0] = step;
                        }
                    }
                });
    }

    //这里遇到传入数组的问题
    // https://bbs.pediy.com/thread-202147.htm
    // https://blog.csdn.net/cskgnt/article/details/7816212
    //传入自定义的class，先用findClass
    //如果怕麻烦，考虑用hookAll，
    private void hookBushuByFindAndHook(final XC_LoadPackage.LoadPackageParam loadPackageParam) {
        findAndHookMethod("android.hardware.SystemSensorManager$SensorEventQueue",
                loadPackageParam.classLoader,
                "dispatchSensorEvent",
                int.class,
                float[].class,//亲测可行
//                new Class[]{float[].class},//不可行
//                Array.newInstance(float.class, 0).getClass(),//亲测可行
                int.class,
                long.class,
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
                            log("传感器为NULL");
                            return;
                        }
                        int sensorType = sensor.getType();
                        if (sensorType == 19 || sensorType == 18) {
                            //500倍差不多了
//                            float step = ((float[]) param.args[1])[0] * PreferencesUtils.getRatio();
                            float step = ((float[]) param.args[1])[0] * 231;
                            log(loadPackageParam.packageName
                                    + " 当前步数 " + ((float[]) param.args[1])[0]
//                                            + " 倍数 " + PreferencesUtils.getRatio()
                                    + " 修改 " + step);
                            ((float[]) param.args[1])[0] = step;
                        }
                    }
                });
    }

    private void hookMenu(final XC_LoadPackage.LoadPackageParam loadPackageParam) {
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
//                                        Toast.makeText(applicationContext, "click" + finalI, Toast.LENGTH_SHORT).show();
                                    return false;
                                }
                            });
                        }
                    }
                });
    }
}