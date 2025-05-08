package com.hyfata.najoan.koreanpatch.process.handler;

import com.hyfata.najoan.koreanpatch.client.KoreanPatchClient;
import com.hyfata.najoan.koreanpatch.data.ConfigManager;
import com.hyfata.najoan.koreanpatch.data.LangTypeManager;
import com.hyfata.najoan.koreanpatch.data.config.category.CategoryInput;
import com.hyfata.najoan.koreanpatch.data.config.category.input.AutoLangTypeMode;
import com.hyfata.najoan.koreanpatch.data.provider.LanguageType;
import com.hyfata.najoan.koreanpatch.data.storage.InputStatus;
import com.hyfata.najoan.koreanpatch.data.storage.InputStatusStorage;
import com.hyfata.najoan.koreanpatch.gui.GUIStatus;
import com.hyfata.najoan.koreanpatch.process.ime.InputController;
import com.hyfata.najoan.koreanpatch.process.ime.InputManager;
import com.hyfata.najoan.koreanpatch.util.ReflectionFieldChecker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.font.TextFieldHelper;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.controls.ControlsScreen;
import net.minecraft.client.gui.screens.inventory.*;

import java.util.ArrayList;
import java.util.Arrays;

public class EventListener {
    private static ArrayList<Class<?>> patchedScreenClazz = new ArrayList<>();
    private static final Class<?>[] injectionBypassScreens = {
            JigsawBlockEditScreen.class,
            StructureBlockEditScreen.class
    };

    public static void onClientStarted() {
        KoreanPatchClient.clientStarted();

        String[] imeDisabledScreens = {
                "arm32x.minecraft.commandblockide.client.gui.screen.CommandIDEScreen",
                "xaero.map.gui.GuiMap"
        };
        Class<?>[] imeDisabledClasses = {
                ControlsScreen.class,
                ContainerScreen.class,
                InventoryScreen.class,
                FurnaceScreen.class,
                CraftingScreen.class,
                EnchantmentScreen.class,
                BeaconScreen.class,

                ShulkerBoxScreen.class,
                SmokerScreen.class,
                CartographyTableScreen.class,
                BlastFurnaceScreen.class,
                SmithingScreen.class,
                GrindstoneScreen.class,
                BrewingStandScreen.class,
                LoomScreen.class,
                StonecutterScreen.class,
                MerchantScreen.class
        };

        patchedScreenClazz = getExistingClasses(imeDisabledScreens);
        patchedScreenClazz.addAll(Arrays.asList(imeDisabledClasses));
    }

    public static void afterScreenChange() {
        Screen screen = Minecraft.getInstance().screen;
        if (screen == null || !KoreanPatchClient.loaded) return;

        GUIStatus.setBypassInjection(isInjectionBypassScreen(screen));

        boolean hasTextInput = isScreenPatched(screen) || hasTextField(screen);
        InputController controller = InputManager.getController();
        CategoryInput categoryInput = ConfigManager.getInstance().getConfig().getCategoryInput();

        if (controller != null && categoryInput.isAutoImeSwitch()) {
            controller.setFocus(!hasTextInput);
        }
        if (hasTextInput) {
            setLangType(screen);
        }
    }

    public static void onClientTick() {
        CategoryInput categoryInput = ConfigManager.getInstance().getConfig().getCategoryInput();
        Minecraft client = Minecraft.getInstance();

        if (client.screen == null && !GUIStatus.isShouldUseIME() && categoryInput.isDisableImeWhenPlaying()) {
            InputManager.getController().setFocus(false);
        } else if (GUIStatus.isShouldUseIME()) {
            InputManager.getController().setFocus(true);
        }
    }

    private static void setLangType(Screen screen) {
        CategoryInput categoryInput = ConfigManager.getInstance().getConfig().getCategoryInput();
        AutoLangTypeMode mode = categoryInput.getAutoLangTypeMode();

        if (mode != AutoLangTypeMode.AUTO) {
            switch (mode) {
                case KOREAN -> LangTypeManager.getInstance().setCurrentType(LanguageType.KO);
                case ENGLISH -> LangTypeManager.getInstance().setCurrentType(LanguageType.EN);
                case IME -> InputManager.getController().setFocus(true);
            }
        }

        if (categoryInput.isMemoryLangTypePerScreen()) {
            setStoredLangType(screen);
        }
    }

    private static void setStoredLangType(Screen screen) {
        InputStatus inputStatus = InputStatusStorage.getInstance().get(screen);

        if (inputStatus != null) {
            LangTypeManager.getInstance().setCurrentType(inputStatus.getLanguageType());
            InputManager.getController().setFocus(inputStatus.isImeFocus());
        } else {
            InputStatusStorage.getInstance().add(screen);
        }

        InputStatusStorage.getInstance().save();
    }

    private static ArrayList<Class<?>> getExistingClasses(String[] clazz) {
        ArrayList<Class<?>> result = new ArrayList<>();
        for (String className : clazz) {
            try {
                Class<?> cls = Class.forName(className);
                result.add(cls);
            } catch (ClassNotFoundException ignored) {
            }
        }
        return result;
    }

    private static boolean isInjectionBypassScreen(Screen screen) {
        return Arrays.stream(injectionBypassScreens).anyMatch(cls -> cls.isInstance(screen));
    }

    private static boolean isScreenPatched(Screen screen) {
        boolean screenPatched = false;
        for (Class<?> cls : patchedScreenClazz) {
            if (cls.isInstance(screen)) {
                screenPatched = true;
                break;
            }
        }
        return screenPatched;
    }

    private static boolean hasTextField(Screen screen) {
        boolean hasTextFieldWidget = ReflectionFieldChecker.hasFieldOfType(screen, EditBox.class);
        boolean hasSelectionManager = ReflectionFieldChecker.hasFieldOfType(screen, TextFieldHelper.class);
        return hasTextFieldWidget || hasSelectionManager;
    }
}
