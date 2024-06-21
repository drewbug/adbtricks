package radio.ab3j.adbtricks.wrappers;

import com.genymobile.scrcpy.FakeContext;

import org.objenesis.ObjenesisHelper;

import android.content.Context;
import android.telephony.SubscriptionManager;
import android.telephony.SubscriptionInfo;

import java.lang.reflect.Field;
import java.util.List;

public final class ShellSubscriptionManager {

    private final SubscriptionManager manager;

    static ShellSubscriptionManager create() {
        try {
            Field contextField = SubscriptionManager.class.getDeclaredField("mContext");

            contextField.setAccessible(true);

            Object manager = ObjenesisHelper.newInstance(SubscriptionManager.class);

            contextField.set(manager, FakeContext.get());

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
