package de.robv.android.xposed;

public class XposedHelpers {
    public static void setStaticObjectField(Class<?> clazz, String fieldName, Object value) {}
    public static void findAndHookMethod(String className, ClassLoader classLoader, String methodName, Object... parameterTypesAndCallback) {}
    public static Class<?> findClass(String className, ClassLoader classLoader) { return null; }
}
