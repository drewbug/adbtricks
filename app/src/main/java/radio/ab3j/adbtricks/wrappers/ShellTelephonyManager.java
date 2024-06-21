package radio.ab3j.adbtricks.wrappers;

import com.genymobile.scrcpy.FakeContext;

import org.objenesis.ObjenesisHelper;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.telephony.UiccCardInfo;
import android.telephony.UiccPortInfo;

import java.lang.reflect.Field;
import java.util.List;

public final class ShellTelephonyManager {

    private final TelephonyManager manager;

    static ShellTelephonyManager create() {
        try {
            Field contextField = TelephonyManager.class.getDeclaredField("mContext");

            contextField.setAccessible(true);

            Object manager = ObjenesisHelper.newInstance(TelephonyManager.class);

            contextField.set(manager, FakeContext.get());

            return new ShellTelephonyManager(manager);
        } catch (ReflectiveOperationException e) {
            throw new AssertionError(e);
        }
    }

    private ShellTelephonyManager(Object manager) {
        this.manager = (TelephonyManager) manager;
    }

    public List<UiccCardInfo> getUiccCardsInfo() {
        return manager.getUiccCardsInfo();
    }

}
