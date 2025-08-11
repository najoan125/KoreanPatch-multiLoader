package com.hyfata.najoan.koreanpatch.driver.arch.darwin;

import com.hyfata.najoan.koreanpatch.util.LibraryUtil;
import com.sun.jna.Callback;
import com.sun.jna.Library;
import com.sun.jna.Native;

public interface DarwinHandle extends Library {
    DarwinHandle INSTANCE = Native.load(LibraryUtil.copyLibrary("libdarwincocoainput.dylib"), DarwinHandle.class);

    void initialize(final LogInfoCallback log, final LogErrorCallback error, final LogDebugCallback debug);

    void setFocused(final int val);

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
