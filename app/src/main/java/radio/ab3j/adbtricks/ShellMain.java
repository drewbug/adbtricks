package radio.ab3j.adbtricks;

import radio.ab3j.adbtricks.wrappers.ShellServiceManager;

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
                List<WifiConfiguration> networks = ShellServiceManager.getWifiManager().getPrivilegedConfiguredNetworks();

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
            } else if (args[0].equals("start-usb-tethering")) {
                start_usb_tethering();
            } else if (args[0].equals("start-bluetooth-tethering")) {
                start_bluetooth_tethering();
            } else if (args[0].equals("stop-wifi-tethering")) {
                // TODO
            } else if (args[0].equals("stop-usb-tethering")) {
                // TODO
            } else if (args[0].equals("stop-bluetooth-tethering")) {
                // TODO
            } else if (args[0].equals("dump-wifi-debugging")) {
                System.out.println(Integer.toString(get_adb_port()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int get_adb_port() throws ReflectiveOperationException {
        IBinder binder = (IBinder) GET_SERVICE_METHOD.invoke(null, "adb");

        Class<?> iAdbManagerStub = Class.forName("android.debug.IAdbManager$Stub");
        Method asInterface = iAdbManagerStub.getDeclaredMethod("asInterface", IBinder.class);
        Object iAbdManagerService = asInterface.invoke(null, binder);

        Class<?> iAdbManager = Class.forName("android.debug.IAdbManager");
        Method getAdbWirelessPort = iAdbManager.getDeclaredMethod("getAdbWirelessPort");
        return (int) getAdbWirelessPort.invoke(iAbdManagerService);
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

    public static void start_usb_tethering() throws ReflectiveOperationException {
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
        startTetheringMethod.invoke(tetheringManager, 1 /* Tethering type */, executor, callbackProxy);
    }

    public static void start_bluetooth_tethering() throws ReflectiveOperationException {
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
        startTetheringMethod.invoke(tetheringManager, 2 /* Tethering type */, executor, callbackProxy);
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

}
