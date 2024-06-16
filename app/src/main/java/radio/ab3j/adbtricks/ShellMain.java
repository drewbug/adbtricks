package radio.ab3j.adbtricks;

import com.genymobile.scrcpy.FakeContext;

import android.os.IBinder;

import android.net.wifi.WifiConfiguration;

import android.hardware.camera2.CameraCharacteristics;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;
import java.util.function.Supplier;

import android.content.Context;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

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
            } else if (args[0].equals("start-wifi-tethering")) {
                start_wifi_tethering();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void start_wifi_tethering() throws ReflectiveOperationException {
        IBinder binder = (IBinder) GET_SERVICE_METHOD.invoke(null, "tethering");
        Supplier<IBinder> connectorSupplier = () -> binder;

        Class<?> tetheringManagerClass = Class.forName("android.net.TetheringManager");
        Constructor<?> tetheringManagerConstructor = tetheringManagerClass.getConstructor(Class.forName("android.content.Context"), Supplier.class);
        Object tetheringManager = tetheringManagerConstructor.newInstance(FakeContext.get(), connectorSupplier);

        Executor executor = Executors.newSingleThreadExecutor();

        // Create a dynamic proxy for StartTetheringCallback
        Class<?> callbackClass = Class.forName("android.net.TetheringManager$StartTetheringCallback");
        Object callbackProxy = Proxy.newProxyInstance(
            callbackClass.getClassLoader(),
            new Class<?>[]{callbackClass},
            new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    String methodName = method.getName();
                    if ("onTetheringStarted".equals(methodName)) {
                        System.out.println("Tethering started successfully.");
                    } else if ("onTetheringFailed".equals(methodName)) {
                        System.out.println("Tethering failed to start.");
                    }
                    return null;
                }
            }
        );

        // Get the startTethering method
        Method startTetheringMethod = tetheringManagerClass.getMethod(
            "startTethering", int.class, Executor.class, callbackClass
        );

        // Invoke the startTethering method
        startTetheringMethod.invoke(tetheringManager, 0 /* Tethering type */, executor, callbackProxy);
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
