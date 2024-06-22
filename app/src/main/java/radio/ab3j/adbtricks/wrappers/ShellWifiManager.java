package radio.ab3j.adbtricks.wrappers;

import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.apache.commons.lang3.reflect.MethodUtils;

import com.genymobile.scrcpy.FakeContext;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.os.IBinder;

import java.util.List;

public final class ShellWifiManager {

    private static final Class WIFI_SERVICE_CLASS;

    static {
        try {
            WIFI_SERVICE_CLASS = Class.forName("android.net.wifi.IWifiManager$Stub");
        } catch (ReflectiveOperationException e) {
            throw new AssertionError(e);
        }
    }

    private static final Class WIFI_MANAGER_CLASS;

    static {
        try {
            WIFI_MANAGER_CLASS = Class.forName("android.net.wifi.WifiManager");
        } catch (ReflectiveOperationException e) {
            throw new AssertionError(e);
        }
    }

    private final Object mService;

    static ShellWifiManager create() {
        try {
            IBinder b = ShellServiceManager.getService(Context.WIFI_SERVICE);

            Object service = MethodUtils.invokeStaticMethod(WIFI_SERVICE_CLASS, "asInterface", b);

            Object manager = ConstructorUtils.invokeConstructor(WIFI_MANAGER_CLASS, FakeContext.get(), service, null);

            return new ShellWifiManager(manager);
        } catch (ReflectiveOperationException e) {
            throw new AssertionError(e);
        }
    }

    private ShellWifiManager(Object service) {
        this.mService = service;
    }

    public List<WifiConfiguration> getPrivilegedConfiguredNetworks() {
        try {
            return (List<WifiConfiguration>) MethodUtils.invokeMethod(this.mService, "getPrivilegedConfiguredNetworks");
        } catch (ReflectiveOperationException e) {
            throw new AssertionError(e);
        }
    }

}
