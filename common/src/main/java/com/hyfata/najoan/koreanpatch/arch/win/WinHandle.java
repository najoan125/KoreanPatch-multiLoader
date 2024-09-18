package com.hyfata.najoan.koreanpatch.arch.win;

import com.hyfata.najoan.koreanpatch.util.LibraryUtil;
import com.sun.jna.*;

public interface WinHandle extends Library {
    WinHandle INSTANCE = Native.load(LibraryUtil.copyLibrary("libwincocoainput.dll"), WinHandle.class);

    void set_focus(int flag);
    void initialize(
            long window,
            PreeditCallback paramDrawCallback,
            DoneCallback paramDoneCallback,
            RectCallback paramRectCallback,
            LogInfoCallback log,
            LogErrorCallback error,
            LogDebugCallback debug
    );

    interface PreeditCallback extends Callback{
        void invoke(WString str, int cursor, int length);
    }

    interface DoneCallback extends Callback {
        void invoke(WString str);
    }

    interface RectCallback extends Callback{
        int invoke(Pointer p);
    }

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
