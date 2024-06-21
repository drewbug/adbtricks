package radio.ab3j.adbtricks.wrappers;

import com.genymobile.scrcpy.FakeContext;

import org.objenesis.ObjenesisHelper;

import android.content.Context;
import android.media.AudioManager;

import java.lang.reflect.Field;
import java.util.List;

public final class ShellAudioManager {

    private final AudioManager manager;

    static ShellAudioManager create() {
        try {
            Field contextField = AudioManager.class.getDeclaredField("mOriginalContext");

            contextField.setAccessible(true);

            Object manager = ObjenesisHelper.newInstance(AudioManager.class);

            contextField.set(manager, FakeContext.get());

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
