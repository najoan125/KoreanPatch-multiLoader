package com.hyfata.najoan.koreanpatch.ime.controller;

public class InputManager {
    private static InputController controller;

    public static void applyController(InputController controller) {
        InputManager.controller = controller;
    }

    public static InputController getController() {
        return controller;
    }
}
