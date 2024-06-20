package radio.ab3j.adbtricks.wrappers;

import android.os.IBinder;
import android.os.IInterface;

import java.lang.reflect.Method;

public final class ShellServiceManager {

    private static final Method GET_SERVICE_METHOD;

    static {
        try {
            GET_SERVICE_METHOD = Class.forName("android.os.ServiceManager").getDeclaredMethod("getService", String.class);
        } catch (ReflectiveOperationException e) {
            throw new AssertionError(e);
        }
    }

    private static ShellWifiManager wifiManager;

    private ShellServiceManager() {
        /* not instantiable */
    }

    static IInterface getService(String service, String type) {
        try {
            IBinder binder = (IBinder) GET_SERVICE_METHOD.invoke(null, service);
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

}
