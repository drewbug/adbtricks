package radio.ab3j.adbtricks.wrappers;

import com.genymobile.scrcpy.FakeContext;

import org.objenesis.ObjenesisHelper;

import android.net.wifi.WifiManager;
import android.net.wifi.WifiConfiguration;
import android.os.IInterface;

import java.lang.reflect.Field;
import java.util.List;

public final class ShellWifiManager {

    private final WifiManager manager;

    static ShellWifiManager create() {
        try {
            Field contextField = WifiManager.class.getDeclaredField("mContext");
            Field serviceField = WifiManager.class.getDeclaredField("mService");

            contextField.setAccessible(true);
            serviceField.setAccessible(true);

            Object manager = ObjenesisHelper.newInstance(WifiManager.class);

            Object context = FakeContext.get();
            Object service = ShellServiceManager.getService("wifi", "android.net.wifi.IWifiManager");

            contextField.set(manager, context);
            serviceField.set(manager, service);

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
