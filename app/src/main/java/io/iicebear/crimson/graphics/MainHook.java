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
        
        // 1. Force the system configuration to report a 64-bit architecture
        XposedHelpers.findAndHookMethod(
            "java.lang.System", 
            lpparam.classLoader,
            "getProperty",
            String.class,
            new XC_MethodHook() {
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    String property = (String) param.args;
                    if ("os.arch".equals(property)) {
                        param.setResult("aarch64");
                    }
                }
            }
        );

        // 2. Bypass low memory limits by forcing the RAM evaluation to return false
        XposedHelpers.findAndHookMethod(
            "android.app.ActivityManager",
            lpparam.classLoader,
            "isLowRamDevice",
            new XC_MethodHook() {
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    param.setResult(false);
                }
            }
        );

        // 3. Inject system properties to unlock high-performance asset streaming thresholds
        XposedHelpers.findAndHookMethod(
            "android.os.SystemProperties",
            lpparam.classLoader,
            "get",
            String.class,
            new XC_MethodHook() {
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    String key = (String) param.args;
                    if ("ro.config.low_ram".equals(key)) {
                        param.setResult("false");
                    }
                    if ("ro.sys.fw.bg_apps_limit".equals(key)) {
                        param.setResult("32");
                    }
                }
            }
        );
    }
}
