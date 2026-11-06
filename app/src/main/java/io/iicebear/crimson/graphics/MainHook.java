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
        
        // Hook the global system RAM metric query to trick the engine's capability check 
        // without spoofing the physical GPU hardware lines
        XposedHelpers.findAndHookMethod(
            "java.lang.System", 
            lpparam.classLoader,
            "getProperty",
            String.class,
            new XC_MethodHook() {
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    String property = (String) param.args[0];
                    if ("os.arch".equals(property)) {
                        param.setResult("aarch64");
                    }
                }
            }
        );

        // Force Android's ActivityManager memory reports to fake a high-tier free memory overhead 
        // to bypass the engine's low-RAM draw-distance clamping
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
    }
}
