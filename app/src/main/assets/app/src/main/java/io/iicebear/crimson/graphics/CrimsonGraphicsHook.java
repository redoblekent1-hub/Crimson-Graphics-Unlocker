package io.iicebear.crimson.graphics;

import android.os.Build;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class CrimsonGraphicsHook implements IXposedHookLoadPackage {

    // Target game array from your original module configuration
    private static final Set<String> TARGET_GAMES = new HashSet<>(Arrays.asList(
        "com.HoYoverse.Nap", "com.miHoYo.GenshinImpact", "com.kurogame.wutheringwaves.global",
        "com.tencent.ig", "com.activision.callofduty.warzone", "com.garena.game.codm",
        "com.gameloft.android.ANMP.GloftA9HM", "com.riotgames.league.wildrift"
    ));

    @Override
    public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {
        // Only inject into listed gaming application packages
        if (!TARGET_GAMES.contains(lpparam.packageName)) {
            return;
        }

        XposedBridge.log("Crimson Graphics: Intercepting " + lpparam.packageName);

        // 1. Spoof system parameters to simulate a premium gaming device profile
        XposedHelpers.setStaticObjectField(Build.class, "MANUFACTURER", "ASUS");
        XposedHelpers.setStaticObjectField(Build.class, "BRAND", "asus");
        XposedHelpers.setStaticObjectField(Build.class, "MODEL", "ASUS_AI2401"); // ROG Phone 8 Pro
        XposedHelpers.setStaticObjectField(Build.class, "PRODUCT", "WW_AI2401");
        XposedHelpers.setStaticObjectField(Build.class, "DEVICE", "AI2401");
        XposedHelpers.setStaticObjectField(Build.class, "BOARD", "kalama");
        XposedHelpers.setStaticObjectField(Build.class, "HARDWARE", "qcom");

        // 2. Intercept native SystemProperties queries
        XposedHelpers.findAndHookMethod("android.os.SystemProperties", lpparam.classLoader, "get", String.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                String key = (String) param.args;
                if (key.equals("ro.product.model") || key.equals("ro.product.device")) {
                    param.setResult("ASUS_AI2401");
                } else if (key.equals("ro.product.manufacturer")) {
                    param.setResult("ASUS");
                } else if (key.equals("ro.hardware.egl")) {
                    param.setResult("adreno");
                }
            }
        });

        // 3. Spoof OpenGL graphics engine renderer responses
        try {
            Class<?> glClass = XposedHelpers.findClass("android.opengl.GLES20", lpparam.classLoader);
            XposedHelpers.findAndHookMethod(glClass, "glGetString", int.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    int name = (int) param.args;
                    if (name == 0x1F01) { // GL_RENDERER constant identification value
                        param.setResult("Adreno (TM) 750"); 
                    }
                }
            });
        } catch (Exception e) {
            XposedBridge.log("Crimson Graphics GL Hook Exception: " + e.getMessage());
        }
    }
          }

