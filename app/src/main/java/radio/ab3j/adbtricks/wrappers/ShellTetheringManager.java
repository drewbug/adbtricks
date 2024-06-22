package radio.ab3j.adbtricks.wrappers;

import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.apache.commons.lang3.reflect.MethodUtils;

import com.genymobile.scrcpy.FakeContext;

import android.net.LinkAddress;
import android.os.IBinder;

import java.util.function.Supplier;

public final class ShellTetheringManager {

    private static final String SERVICE_NAME = android.content.Context.TETHERING_SERVICE;

    private static final String MANAGER_CLASS = "android.net.TetheringManager";

    private final Object mService;

    static ShellTetheringManager create() {
        try {
            IBinder b = ShellServiceManager.getService(SERVICE_NAME);

            Object manager = ConstructorUtils.invokeConstructor(Class.forName(MANAGER_CLASS), FakeContext.get(), new Supplier<IBinder>() {
                @Override
                public IBinder get() {
                    return b;
                }
            });

            return new ShellTetheringManager(manager);
        } catch (ReflectiveOperationException e) {
            throw new AssertionError(e);
        }
    }

    private ShellTetheringManager(Object service) {
        this.mService = service;
    }

    private static final String REQUEST_BUILDER_CLASS = "android.net.TetheringManager$TetheringRequest$Builder";

    public void startTethering(int type) {
        try {
            Object builder = ConstructorUtils.invokeConstructor(Class.forName(REQUEST_BUILDER_CLASS), type);

            Object request = MethodUtils.invokeMethod(builder, "build");

            MethodUtils.invokeMethod(this.mService, "startTethering", request, null, null);
        } catch (ReflectiveOperationException e) {
            throw new AssertionError(e);
        }
    }

    public void startTethering(int type, LinkAddress localIPv4Address, LinkAddress clientAddress) {
        try {
            Object builder = ConstructorUtils.invokeConstructor(Class.forName(REQUEST_BUILDER_CLASS), type);

            MethodUtils.invokeMethod(builder, "setStaticIpv4Addresses", localIPv4Address, clientAddress);

            Object request = MethodUtils.invokeMethod(builder, "build");

            MethodUtils.invokeMethod(this.mService, "startTethering", request, null, null);
        } catch (ReflectiveOperationException e) {
            throw new AssertionError(e);
        }
    }

}
