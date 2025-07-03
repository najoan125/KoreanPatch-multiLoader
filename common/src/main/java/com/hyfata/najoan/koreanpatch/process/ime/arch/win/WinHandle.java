package com.hyfata.najoan.koreanpatch.process.ime.arch.win;

import com.hyfata.najoan.koreanpatch.util.LibraryUtil;
import com.sun.jna.*;

public interface WinHandle extends Library {
    WinHandle INSTANCE = Native.load(LibraryUtil.copyLibrary("libwincocoainput.dll"), WinHandle.class);

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
