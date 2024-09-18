package com.hyfata.najoan.koreanpatch.util;

import net.minecraft.client.gui.screens.Screen;

import java.lang.reflect.Field;

public class ReflectionFieldChecker {

    /**
     * Checks whether a specific field type exists on a given screen and in all parent and inner classes.
     *
     * @param screen Screen instance
     * @param fieldType Type of field you are looking for
     * @return true if at least one field is found, false otherwise
     */
    public static boolean hasFieldOfType(Screen screen, Class<?> fieldType) {
        Class<?> clazz = screen.getClass();
        while (clazz != null) {
            if (checkFieldsInClass(clazz, fieldType)) {
                return true;
            }
            // check inner classes
            Class<?>[] declaredClasses = clazz.getDeclaredClasses();
            for (Class<?> innerClass : declaredClasses) {
                if (checkFieldsInClass(innerClass, fieldType)) {
                    return true;
                }
            }
            clazz = clazz.getSuperclass();
        }
        return false;
    }

    /**
     * Checks for a specific field type in a given class.
     *
     * @param clazz class
     * @param fieldType Type of field you are looking for
     * @return true if at least one field is found, false otherwise
     */
    private static boolean checkFieldsInClass(Class<?> clazz, Class<?> fieldType) {
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            if (fieldType.isAssignableFrom(field.getType())) {
                return true;
            }
        }
        return false;
    }
}
