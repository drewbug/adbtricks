package radio.ab3j.adbtricks.wrappers;

import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.apache.commons.lang3.reflect.MethodUtils;

import com.genymobile.scrcpy.FakeContext;

import android.content.Context;
import android.net.LinkAddress;
import android.os.IBinder;

import java.util.function.Supplier;

public final class ShellTetheringManager {

    private static final Class TETHERING_MANAGER_CLASS;

    static {
        try {
            TETHERING_MANAGER_CLASS = Class.forName("android.net.TetheringManager");
        } catch (ReflectiveOperationException e) {
            throw new AssertionError(e);
        }
    }

    private static final Class TETHERING_REQUEST_BUILDER_CLASS;

    static {
        try {
            TETHERING_REQUEST_BUILDER_CLASS = Class.forName("android.net.TetheringManager$TetheringRequest$Builder");
        } catch (ReflectiveOperationException e) {
            throw new AssertionError(e);
        }
    }

    private final Object mService;

    static ShellTetheringManager create() {
        try {
            IBinder b = ShellServiceManager.getService(Context.TETHERING_SERVICE);

            Object manager = ConstructorUtils.invokeConstructor(TETHERING_MANAGER_CLASS, FakeContext.get(), new Supplier<IBinder>() {
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

    public void startTethering(int type) {
        try {
            Object builder = ConstructorUtils.invokeConstructor(TETHERING_REQUEST_BUILDER_CLASS, type);

            Object request = MethodUtils.invokeMethod(builder, "build");

            MethodUtils.invokeMethod(this.mService, "startTethering", request, null, null);
        } catch (ReflectiveOperationException e) {
            throw new AssertionError(e);
        }
    }

    public void startTethering(int type, LinkAddress localIPv4Address, LinkAddress clientAddress) {
        try {
            Object builder = ConstructorUtils.invokeConstructor(TETHERING_REQUEST_BUILDER_CLASS, type);

            MethodUtils.invokeMethod(builder, "setStaticIpv4Addresses", localIPv4Address, clientAddress);

            Object request = MethodUtils.invokeMethod(builder, "build");

            MethodUtils.invokeMethod(this.mService, "startTethering", request, null, null);
        } catch (ReflectiveOperationException e) {
            throw new AssertionError(e);
        }
    }

}
