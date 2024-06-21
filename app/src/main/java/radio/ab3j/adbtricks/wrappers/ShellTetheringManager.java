package radio.ab3j.adbtricks.wrappers;

import com.genymobile.scrcpy.FakeContext;

import android.net.LinkAddress;
import android.net.TetheringManager;
import android.os.IBinder;

import java.lang.reflect.Constructor;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Supplier;


public final class ShellTetheringManager {

    private final TetheringManager manager;

    static ShellTetheringManager create() {
        try {
            IBinder service = android.os.ServiceManager.getService("tethering");

            Constructor ctor = Class.forName("android.net.TetheringManager").getConstructors()[0];

            Object manager = ctor.newInstance(FakeContext.get(), new Supplier<IBinder>() {
                @Override
                public IBinder get() {
                    return service;
                }
            });

            return new ShellTetheringManager(manager);
        } catch (ReflectiveOperationException e) {
            throw new AssertionError(e);
        }
    }

    private ShellTetheringManager(Object manager) {
        this.manager = (TetheringManager) manager;
    }

    public void startTethering(int type) {
        TetheringManager.TetheringRequest.Builder builder = new TetheringManager.TetheringRequest.Builder(type);

        manager.startTethering(builder.build(), null, null);
    }

    public void startTethering(int type, LinkAddress localIPv4Address, LinkAddress clientAddress) {
        TetheringManager.TetheringRequest.Builder builder = new TetheringManager.TetheringRequest.Builder(type);

        builder.setStaticIpv4Addresses(localIPv4Address, clientAddress);

        manager.startTethering(builder.build(), null, null);
    }

}
