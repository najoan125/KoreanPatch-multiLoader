package com.hyfata.najoan.koreanpatch.driver.arch.darwin;

import com.hyfata.najoan.koreanpatch.util.LibraryUtil;
import com.sun.jna.Callback;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;

public interface DarwinHandle extends Library {
    DarwinHandle INSTANCE = Native.load(LibraryUtil.copyLibrary("libdarwincocoainput.dylib"), DarwinHandle.class);

    void initialize(final LogInfoCallback log, final LogErrorCallback error, final LogDebugCallback debug);

    void setIfReceiveEvent(final String uuid, final int yn);

    void addInstance(
            final String uuid, final InsertText insertText,
            final SetMarkedText setMarkedText, final FirstRectForCharacterRange range
    );

    interface InsertText extends Callback {
        void invoke(final String str, final int position, final int length);
    }

    interface SetMarkedText extends Callback {
        void invoke(final String str, final int position1, final int length1, final int position2, final int length2);
    }

    interface FirstRectForCharacterRange extends Callback {
        void invoke(final Pointer pointer);
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
