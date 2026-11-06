package kxiicebear.crimson.graphics;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class MainHook implements IXposedHookLoadPackage {

    @Override
    public void handleLoadPackage(final LoadPackageParam lpparam) throws Throwable {
        
        if (!lpparam.packageName.equals("com.garena.game.codm")) {
            return;
        }
        
        XposedHelpers.findAndHookMethod(
            "com.garena.game.codm.ConfigUtils", 
            lpparam.classLoader,
            "getDevicePerformanceTier", 
            new XC_MethodHook() {
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    param.setResult(3); 
                }
            }
        );

        XposedHelpers.findAndHookMethod(
            "com.garena.game.codm.ConfigUtils",
            lpparam.classLoader,
            "isLowEndDevice",
            new XC_MethodHook() {
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    param.setResult(false);
                }
            }
        );
    }
}
