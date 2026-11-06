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
        
        // 1. Restore the working architecture framework hook
        XposedHelpers.findAndHookMethod(
            "java.lang.System", 
            lpparam.classLoader,
            "getProperty",
            String.class,
            new XC_MethodHook() {
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    if (param.args != null && param.args.length > 0 && param.args[0] instanceof String) {
                        String property = (String) param.args[0];
                        if ("os.arch".equals(property)) {
                            param.setResult("aarch64");
                        }
                    }
                }
            }
        );

        // 2. Restore the working Low-RAM bypass hook
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

        // 3. Add the extended draw-distance threshold system overrides correctly
        XposedHelpers.findAndHookMethod(
            "android.os.SystemProperties",
            lpparam.classLoader,
            "get",
            String.class,
            new XC_MethodHook() {
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    if (param.args != null && param.args.length > 0 && param.args[0] instanceof String) {
                        String key = (String) param.args[0];
                        if ("ro.config.low_ram".equals(key)) {
                            param.setResult("false");
                        }
                        if ("ro.sys.fw.bg_apps_limit".equals(key)) {
                            param.setResult("32");
                        }
                    }
                }
            }
        );
    }
}
