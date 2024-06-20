package radio.ab3j.adbtricks.wrappers;

import com.genymobile.scrcpy.FakeContext;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.os.IInterface;
import android.os.Looper;

import java.lang.reflect.Constructor;
import java.util.List;

public final class ShellWifiManager {

    private final Object manager;

    static ShellWifiManager create() {
        try {
            IInterface service = ShellServiceManager.getService("wifi", "android.net.wifi.IWifiManager");

            Constructor ctor = android.net.wifi.WifiManager.class.getConstructors()[0];

            Object manager = ctor.newInstance(FakeContext.get(), service, null);

            return new ShellWifiManager(manager);
        } catch (ReflectiveOperationException e) {
            throw new AssertionError(e);
        }
    }

    private ShellWifiManager(Object manager) {
        this.manager = manager;
    }

    public List<WifiConfiguration> getPrivilegedConfiguredNetworks() {
        try {
            return (List<WifiConfiguration>) manager.getClass().getMethod("getPrivilegedConfiguredNetworks").invoke(manager);
        } catch (ReflectiveOperationException e) {
            throw new AssertionError(e);
        }
    }

}
