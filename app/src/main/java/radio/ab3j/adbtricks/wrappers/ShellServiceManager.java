package radio.ab3j.adbtricks.wrappers;

import com.genymobile.scrcpy.Workarounds;

import android.os.IBinder;
import android.os.IInterface;

import java.lang.reflect.Method;

public final class ShellServiceManager {

    private static final Method GET_SERVICE_METHOD;

    static {
        try {
            GET_SERVICE_METHOD = Class.forName("android.os.ServiceManager").getDeclaredMethod("getService", String.class);
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }

    static {
        android.app.ActivityThread.initializeMainlineModules();
    }

    private static ShellWifiManager wifiManager;
    private static ShellTetheringManager tetheringManager;
    private static ShellAudioManager audioManager;
    private static ShellSubscriptionManager subscriptionManager;
    private static ShellTelephonyManager telephonyManager;

    private ShellServiceManager() {
        /* not instantiable */
    }

    static IBinder getService(String name) {
        try {
            return (IBinder) GET_SERVICE_METHOD.invoke(null, name);
        } catch (ReflectiveOperationException e) {
            throw new AssertionError(e);
        }
    }

    static IInterface getService(String service, String type) {
        try {
            IBinder binder = getService(service);
            Method asInterfaceMethod = Class.forName(type + "$Stub").getMethod("asInterface", IBinder.class);
            return (IInterface) asInterfaceMethod.invoke(null, binder);
        } catch (ReflectiveOperationException e) {
            throw new AssertionError(e);
        }
    }

    public static ShellWifiManager getWifiManager() {
        if (wifiManager == null) {
            wifiManager = ShellWifiManager.create();
        }
        return wifiManager;
    }

    public static ShellTetheringManager getTetheringManager() {
        if (tetheringManager == null) {
            tetheringManager = ShellTetheringManager.create();
        }
        return tetheringManager;
    }

    public static ShellAudioManager getAudioManager() {
        if (audioManager == null) {
            audioManager = ShellAudioManager.create();
        }
        return audioManager;
    }

    public static ShellSubscriptionManager getSubscriptionManager() {
        if (subscriptionManager == null) {
            subscriptionManager = ShellSubscriptionManager.create();
        }
        return subscriptionManager;
    }

    public static ShellTelephonyManager getTelephonyManager() {
        if (telephonyManager == null) {
            telephonyManager = ShellTelephonyManager.create();
        }
        return telephonyManager;
    }


}
