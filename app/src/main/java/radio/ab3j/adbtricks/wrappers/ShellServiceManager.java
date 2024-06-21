package radio.ab3j.adbtricks.wrappers;

import android.os.IBinder;
import android.os.IInterface;

import java.lang.reflect.Method;

public final class ShellServiceManager {

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

    static IInterface getService(String service, String type) {
        try {
            IBinder binder = android.os.ServiceManager.getService(service);
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
