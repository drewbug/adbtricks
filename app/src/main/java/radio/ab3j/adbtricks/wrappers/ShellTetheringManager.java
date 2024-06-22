package radio.ab3j.adbtricks.wrappers;

import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.apache.commons.lang3.reflect.MethodUtils;

import com.genymobile.scrcpy.FakeContext;

import android.net.LinkAddress;

public final class ShellTetheringManager {

    private static final Class REQUEST_BUILDER_CLASS;

    static {
        try {
            REQUEST_BUILDER_CLASS = Class.forName("android.net.TetheringManager$TetheringRequest$Builder");
        } catch (ReflectiveOperationException e) {
            throw new AssertionError(e);
        }
    }

    private final Object mService;

    static ShellTetheringManager create() {
        Object service = FakeContext.get().getSystemService("tethering");

        return new ShellTetheringManager(service);
    }

    private ShellTetheringManager(Object service) {
        this.mService = service;
    }

    public void startTethering(int type) {
        try {
            Object builder = ConstructorUtils.invokeConstructor(REQUEST_BUILDER_CLASS, type);

            Object request = MethodUtils.invokeMethod(builder, "build");

            MethodUtils.invokeMethod(this.mService, "startTethering", request, null, null);
        } catch (ReflectiveOperationException e) {
            throw new AssertionError(e);
        }
    }

    public void startTethering(int type, LinkAddress localIPv4Address, LinkAddress clientAddress) {
        try {
            Object builder = ConstructorUtils.invokeConstructor(REQUEST_BUILDER_CLASS, type);

            MethodUtils.invokeMethod(builder, "setStaticIpv4Addresses", localIPv4Address, clientAddress);

            Object request = MethodUtils.invokeMethod(builder, "build");

            MethodUtils.invokeMethod(this.mService, "startTethering", request, null, null);
        } catch (ReflectiveOperationException e) {
            throw new AssertionError(e);
        }
    }

}
