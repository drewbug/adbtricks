package radio.ab3j.adbtricks.wrappers;

import com.genymobile.scrcpy.FakeContext;

import android.net.wifi.WifiManager;
import android.net.wifi.WifiConfiguration;
import android.os.IInterface;

import java.lang.reflect.Constructor;
import java.util.List;

public final class ShellWifiManager {

    private final WifiManager manager;

    static ShellWifiManager create() {
        try {
            IInterface service = ShellServiceManager.getService("wifi", "android.net.wifi.IWifiManager");

            Constructor ctor = Class.forName("android.net.wifi.WifiManager").getConstructors()[0];

            Object manager = ctor.newInstance(FakeContext.get(), service, null);

            return new ShellWifiManager(manager);
        } catch (ReflectiveOperationException e) {
            throw new AssertionError(e);
        }
    }

    private ShellWifiManager(Object manager) {
        this.manager = (WifiManager) manager;
    }

    public List<WifiConfiguration> getPrivilegedConfiguredNetworks() {
        return manager.getPrivilegedConfiguredNetworks();
    }

}
