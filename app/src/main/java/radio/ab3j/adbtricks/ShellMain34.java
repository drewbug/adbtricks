package radio.ab3j.adbtricks;

import radio.ab3j.adbtricks.wrappers.ShellServiceManager;

import com.genymobile.scrcpy.FakeContext;

import android.os.IBinder;

import android.net.LinkAddress;
import android.net.wifi.WifiConfiguration;

import android.hardware.camera2.CameraCharacteristics;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;
import java.util.function.Supplier;

import android.telephony.SubscriptionInfo;
import android.telephony.UiccCardInfo;
import android.telephony.UiccPortInfo;

import android.content.Context;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.apache.sshd.server.ServerBuilder;
import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.auth.pubkey.AcceptAllPublickeyAuthenticator;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.server.shell.ProcessShellCommandFactory;
import org.apache.sshd.server.shell.InteractiveProcessShellFactory;

public class ShellMain34 {

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
            if (args.length == 0) {
                System.out.println("===========================");
                System.out.println("welcome to adbtricks v1.0.0");
                System.out.println("===========================\n");

                System.out.println("Usage:\n");

                System.out.println(" adbtricks dump-wifi-keys");
                System.out.println(" (outputs all wifi passwords from device memory)\n");

                System.out.println(" adbtricks list-hidden-cameras");
                System.out.println(" (read https://source.android.com/docs/core/camera/system-cameras for info)\n");

                System.out.println(" adbtricks start-wifi-tethering [device-ip] [client-ip]");
                System.out.println(" (turns on wifi tethering with optional IPv4 configuration)\n");

                System.out.println(" adbtricks start-usb-tethering [device-ip] [client-ip]");
                System.out.println(" (turns on usb tethering with optional IPv4 configuration)\n");

                System.out.println(" adbtricks start-bluetooth-tethering [device-ip] [client-ip]");
                System.out.println(" (turns on bluetooth tethering with optional IPv4 configuration)\n");

                System.out.println(" adbtricks set-ringer-normal");
                System.out.println(" (toggles \"normal\" ringer mode on)\n");

                System.out.println(" adbtricks set-ringer-vibrate");
                System.out.println(" (toggles \"vibrate\" ringer mode on)\n");

                System.out.println(" adbtricks set-ringer-silent");
                System.out.println(" (toggles \"silent\" ringer mode on)\n");

                System.out.println(" adbtricks list-sim-cards");
                System.out.println(" (list physical and e-sim profiles)\n");

                System.out.println(" adbtricks start-ssh-server");
                System.out.println(" (start an ssh server daemon for remote access)\n");

                System.out.println(" adbtricks dump-debugging-info");
                System.out.println(" (outputs adb daemon information)");
            } else if (args[0].equals("dump-wifi-keys")) {
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
                if (args.length == 1) {
                    ShellServiceManager.getTetheringManager().startTethering(0);
                } else if (args.length == 3) {
                    ShellServiceManager.getTetheringManager().startTethering(0, new LinkAddress(args[1]), new LinkAddress(args[2]));
                }
            } else if (args[0].equals("start-usb-tethering")) {
                if (args.length == 1) {
                    ShellServiceManager.getTetheringManager().startTethering(1);
                } else if (args.length == 3) {
                    ShellServiceManager.getTetheringManager().startTethering(1, new LinkAddress(args[1]), new LinkAddress(args[2]));
                }
            } else if (args[0].equals("start-bluetooth-tethering")) {
                if (args.length == 1) {
                    ShellServiceManager.getTetheringManager().startTethering(2);
                } else if (args.length == 3) {
                    ShellServiceManager.getTetheringManager().startTethering(2, new LinkAddress(args[1]), new LinkAddress(args[2]));
                }
            } else if (args[0].equals("stop-wifi-tethering")) {
                // TODO
            } else if (args[0].equals("stop-usb-tethering")) {
                // TODO
            } else if (args[0].equals("stop-bluetooth-tethering")) {
                // TODO
            } else if (args[0].equals("dump-wifi-debugging")) {
                System.out.println(Integer.toString(get_adb_port()));
            } else if (args[0].equals("get-ringer-mode")) {
                switch (ShellServiceManager.getAudioManager().getRingerMode()) {
                    case 0:
                        System.out.println("silent");
                        break;
                    case 1:
                        System.out.println("vibrate");
                        break;
                    case 2:
                        System.out.println("normal");
                        break;
                }
            } else if (args[0].equals("set-ringer-silent")) {
                ShellServiceManager.getAudioManager().setRingerMode(0);
            } else if (args[0].equals("set-ringer-vibrate")) {
                ShellServiceManager.getAudioManager().setRingerMode(1);
            } else if (args[0].equals("set-ringer-normal")) {
                ShellServiceManager.getAudioManager().setRingerMode(2);
            } else if (args[0].equals("list-sim-cards")) {
                for (UiccCardInfo card : ShellServiceManager.getTelephonyManager().getUiccCardsInfo()) {
                    System.out.println(card.toString());

                    for (UiccPortInfo port : card.getPorts()) {
                        System.out.println(port.toString());
                    }

                    System.out.println();
                }
                for (SubscriptionInfo sub : ShellServiceManager.getSubscriptionManager().getAvailableSubscriptionInfoList()) {
                    System.out.println(sub.toString());
                }
            } else if (args[0].equals("start-ssh-server")) {
                System.setProperty("user.home", "/");
                ServerBuilder builder = ServerBuilder.builder();

                SshServer sshd = builder.build();

                sshd.setPort(2222);

                sshd.setKeyPairProvider(new SimpleGeneratorHostKeyProvider());

                sshd.setPublickeyAuthenticator(AcceptAllPublickeyAuthenticator.INSTANCE);
                sshd.setPasswordAuthenticator((username, password, session) -> username.equals("shell"));

                sshd.setShellFactory(InteractiveProcessShellFactory.INSTANCE);
                sshd.setCommandFactory(ProcessShellCommandFactory.INSTANCE);

                sshd.start();
                Thread.sleep(Long.MAX_VALUE);
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
