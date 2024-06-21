package radio.ab3j.adbtricks.wrappers;

import com.genymobile.scrcpy.FakeContext;

import android.content.Context;
import android.telephony.SubscriptionManager;
import android.telephony.SubscriptionInfo;

import java.lang.reflect.Constructor;
import java.util.List;

public final class ShellSubscriptionManager {

    private final SubscriptionManager manager;

    static ShellSubscriptionManager create() {
        try {
            Constructor ctor = Class.forName("android.telephony.SubscriptionManager").getConstructor(Context.class);

            Object manager = ctor.newInstance(FakeContext.get());

            return new ShellSubscriptionManager(manager);
        } catch (ReflectiveOperationException e) {
            throw new AssertionError(e);
        }
    }

    private ShellSubscriptionManager(Object manager) {
        this.manager = (SubscriptionManager) manager;
    }

    public List<SubscriptionInfo> getAvailableSubscriptionInfoList() {
        return manager.getAvailableSubscriptionInfoList();
    }

}
