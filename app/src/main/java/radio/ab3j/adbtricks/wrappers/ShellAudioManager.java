package radio.ab3j.adbtricks.wrappers;

import com.genymobile.scrcpy.FakeContext;

import android.content.Context;
import android.media.AudioManager;

import java.lang.reflect.Constructor;
import java.util.List;

public final class ShellAudioManager {

    private final AudioManager manager;

    static ShellAudioManager create() {
        try {
            Constructor ctor = Class.forName("android.media.AudioManager").getConstructor(Context.class);

            Object manager = ctor.newInstance(FakeContext.get());

            return new ShellAudioManager(manager);
        } catch (ReflectiveOperationException e) {
            throw new AssertionError(e);
        }
    }

    private ShellAudioManager(Object manager) {
        this.manager = (AudioManager) manager;
    }

    public int getRingerMode() {
        return manager.getRingerModeInternal();
    }

    public void setRingerMode(int ringerMode) {
        manager.setRingerModeInternal(ringerMode);
    }

}
