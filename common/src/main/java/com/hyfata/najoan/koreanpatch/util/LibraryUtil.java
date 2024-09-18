package com.hyfata.najoan.koreanpatch.util;

import com.hyfata.najoan.koreanpatch.client.Constants;
import com.hyfata.najoan.koreanpatch.client.KoreanPatchClient;
import com.sun.jna.Native;
import com.sun.jna.Platform;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;

public class LibraryUtil {
    public static String copyLibrary(final String name) {
        try {
            final URL url = KoreanPatchClient.class.getClassLoader().getResource("native/" + name);
            if (url == null) {
                throw new IOException("Native library (" + name + ") not found.");
            }

            final File lib = File.createTempFile("koreanpatch", Platform.isWindows() ? ".dll" : null, tempDir());
            try (
                    final InputStream is = url.openStream();
                    final FileOutputStream fos = new FileOutputStream(lib)
            ) {
                Constants.LOG.debug("Extracting library to {}", lib.getAbsolutePath());
                fos.write(is.readAllBytes());
                lib.deleteOnExit();
            }

            Constants.LOG.info(name + " has copied library to native directory.");

            return lib.getAbsolutePath();
        } catch (final Exception exception) {
            Constants.LOG.error("An error occurred while loading the library.");
            throw new RuntimeException(exception);
        }
    }

    private static File tempDir() throws IOException {
        try {
            final Method method = Native.class.getDeclaredMethod("getTempDir");
            method.setAccessible(true);
            return (File) method.invoke(null);
        } catch (final Exception exception) {
            return File.createTempFile("native", "temp");
        }
    }
}
