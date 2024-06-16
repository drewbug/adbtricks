package radio.ab3j.adbtricks;

import com.genymobile.scrcpy.FakeContext;

import android.os.IBinder;

import android.net.wifi.WifiConfiguration;

import android.hardware.camera2.CameraCharacteristics;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;

public class ShellMain {

    private static final Method GET_SERVICE_METHOD;

    static {
        try {
            GET_SERVICE_METHOD = Class.forName("android.os.ServiceManager").getDeclaredMethod("getService", String.class);
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }

    public static void main(String[] args) {
        try {
            if (args[0].equals("dump-wifi-keys")) {
                List<WifiConfiguration> networks = getPrivilegedConfiguredNetworks();

                for (WifiConfiguration config : networks) {
                    System.out.println(config.SSID + ": " + config.preSharedKey);
                }
            } else if (args[0].equals("list-hidden-cameras")) {
                for (String id : getCameraIdList()) {
                    CameraCharacteristics characteristics = getCameraCharacteristics(id);
                    int[] availableCapabilities = characteristics.get(CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES);
                    for (int cap : availableCapabilities) {
                        if (cap == CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES_SYSTEM_CAMERA) {
                            System.out.println(id);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static CameraCharacteristics getCameraCharacteristics(String cameraId) throws ReflectiveOperationException {
        Class<?> cameraManagerClass = Class.forName("android.hardware.camera2.CameraManager");
        Constructor<?> cameraManagerConstructor = cameraManagerClass.getConstructor(Class.forName("android.content.Context"));
        Object cameraManager = cameraManagerConstructor.newInstance(FakeContext.get());

        Method getCameraCharacteristics = cameraManagerClass.getDeclaredMethod("getCameraCharacteristics", String.class);
        return (CameraCharacteristics) getCameraCharacteristics.invoke(cameraManager, cameraId);
    }

    public static String[] getCameraIdList() throws ReflectiveOperationException {
        Class<?> cameraManagerClass = Class.forName("android.hardware.camera2.CameraManager");
        Constructor<?> cameraManagerConstructor = cameraManagerClass.getConstructor(Class.forName("android.content.Context"));
        Object cameraManager = cameraManagerConstructor.newInstance(FakeContext.get());

        Method getCameraIdList = cameraManagerClass.getDeclaredMethod("getCameraIdList");
        return (String[]) getCameraIdList.invoke(cameraManager);
    }

    public static List<WifiConfiguration> getPrivilegedConfiguredNetworks() throws ReflectiveOperationException {
        IBinder binder = (IBinder) GET_SERVICE_METHOD.invoke(null, "wifi");

        // Use reflection to get IWifiManager.Stub class and asInterface method
        Class<?> iWifiManagerStub = Class.forName("android.net.wifi.IWifiManager$Stub");
        Method asInterface = iWifiManagerStub.getDeclaredMethod("asInterface", IBinder.class);
        Object wifiManagerService = asInterface.invoke(null, binder);

        // Use reflection to get WifiManager class and its constructor
        Class<?> wifiManagerClass = Class.forName("android.net.wifi.WifiManager");
        Constructor<?> wifiManagerConstructor = wifiManagerClass.getConstructor(Class.forName("android.content.Context"), Class.forName("android.net.wifi.IWifiManager"), Class.forName("android.os.Looper"));

        // Create an instance of WifiManager
        Object wifiManager = wifiManagerConstructor.newInstance(FakeContext.get(), wifiManagerService, null);

        // Use reflection to get getPrivilegedConfiguredNetworks method
        Method getPrivilegedConfiguredNetworks = wifiManagerClass.getDeclaredMethod("getPrivilegedConfiguredNetworks");
        return (List<WifiConfiguration>) getPrivilegedConfiguredNetworks.invoke(wifiManager);
    }

}
