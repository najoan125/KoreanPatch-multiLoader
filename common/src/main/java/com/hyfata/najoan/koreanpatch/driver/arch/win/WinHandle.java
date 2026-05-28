package com.hyfata.najoan.koreanpatch.driver.arch.win;

import com.hyfata.najoan.koreanpatch.util.LibraryUtil;
import com.sun.jna.Callback;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;

public interface WinHandle extends Library {
    WinHandle INSTANCE = Native.load(LibraryUtil.copyLibrary(selectLibrary()), WinHandle.class);

    static String selectLibrary() {
        if (Platform.isARM()) {
            return "libwincocoainput-arm64.dll";
        } else {
            return "libwincocoainput-x64.dll";
        }
    }

    void set_focus(int flag);
    void initialize(
            long window,
            LogInfoCallback log,
            LogErrorCallback error,
            LogDebugCallback debug
    );

    interface LogInfoCallback extends Callback {
        void invoke(final String log);
    }

    interface LogErrorCallback extends Callback {
        void invoke(final String log);
    }

    interface LogDebugCallback extends Callback {
        void invoke(final String log);
    }
}
