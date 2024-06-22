package radio.ab3j.adbtricks.wrappers;

import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.apache.commons.lang3.reflect.MethodUtils;

import com.genymobile.scrcpy.FakeContext;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.telephony.UiccCardInfo;
import android.telephony.UiccPortInfo;

import java.lang.reflect.Field;
import java.util.List;

public final class ShellTelephonyManager {

    private static final Class TELEPHONY_MANAGER_CLASS;

    static {
        try {
            TELEPHONY_MANAGER_CLASS = Class.forName("android.telephony.TelephonyManager");
        } catch (ReflectiveOperationException e) {
            throw new AssertionError(e);
        }
    }

    private final Object mService;

    static ShellTelephonyManager create() {
        try {
            Object manager = ConstructorUtils.invokeConstructor(TELEPHONY_MANAGER_CLASS, FakeContext.get());

            return new ShellTelephonyManager(manager);
        } catch (ReflectiveOperationException e) {
            throw new AssertionError(e);
        }
    }

    private ShellTelephonyManager(Object service) {
        this.mService = service;
    }

    public List<UiccCardInfo> getUiccCardsInfo() {
        try {
            return (List<UiccCardInfo>) MethodUtils.invokeMethod(this.mService, "getUiccCardsInfo");
        } catch (ReflectiveOperationException e) {
            throw new AssertionError(e);
        }
    }

}
