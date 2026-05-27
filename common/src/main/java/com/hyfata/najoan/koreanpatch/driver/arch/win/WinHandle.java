package com.hyfata.najoan.koreanpatch.driver.arch.win;

import org.lwjgl.system.Platform;
import org.lwjgl.system.Platform.Architecture;

import com.hyfata.najoan.koreanpatch.util.LibraryUtil;
import com.sun.jna.Callback;
import com.sun.jna.Library;
import com.sun.jna.Native;

public interface WinHandle extends Library {
    WinHandle INSTANCE = Native.load(LibraryUtil.copyLibrary(selectLibrary()), WinHandle.class);

    static String selectLibrary() {
        Architecture arch = Platform.getArchitecture();
        switch (arch) {
            case X64:
                return "libwincocoainput-x64.dll";
            case ARM64:
                return "libwincocoainput-arm64.dll";
            default:
                return null;
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
