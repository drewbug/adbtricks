package radio.ab3j.adbtricks.wrappers;

import com.genymobile.scrcpy.FakeContext;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.telephony.UiccCardInfo;
import android.telephony.UiccPortInfo;

import java.lang.reflect.Constructor;
import java.util.List;

public final class ShellTelephonyManager {

    private final TelephonyManager manager;

    static ShellTelephonyManager create() {
        try {
            Constructor ctor = Class.forName("android.telephony.TelephonyManager").getConstructor(Context.class);

            Object manager = ctor.newInstance(FakeContext.get());

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
