package radio.ab3j.adbtricks.wrappers;

import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.apache.commons.lang3.reflect.MethodUtils;

import com.genymobile.scrcpy.FakeContext;

import android.net.wifi.WifiConfiguration;
import android.os.IBinder;

import java.util.List;

public final class ShellWifiManager {

    private static final String SERVICE_NAME = android.content.Context.WIFI_SERVICE;

    private static final String SERVICE_CLASS = "android.net.wifi.IWifiManager$Stub";

    private static final String MANAGER_CLASS = "android.net.wifi.WifiManager";

    private final Object mService;

    static ShellWifiManager create() {
        try {
            IBinder b = ShellServiceManager.getService(SERVICE_NAME);

            Object service = MethodUtils.invokeStaticMethod(Class.forName(SERVICE_CLASS), "asInterface", b);

            Object manager = ConstructorUtils.invokeConstructor(Class.forName(MANAGER_CLASS), FakeContext.get(), service, null);

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
