package com.hyfata.najoan.koreanpatch.client;

import com.hyfata.najoan.koreanpatch.config.ConfigManager;
import com.hyfata.najoan.koreanpatch.config.screen.ModernSettingsScreen;
import com.hyfata.najoan.koreanpatch.process.LangTypeManager;
import com.hyfata.najoan.koreanpatch.config.category.CategoryInput;
import com.hyfata.najoan.koreanpatch.config.category.input.AutoLangTypeMode;
import com.hyfata.najoan.koreanpatch.process.LanguageType;
import com.hyfata.najoan.koreanpatch.storage.InputStatus;
import com.hyfata.najoan.koreanpatch.storage.InputStatusStorage;
import com.hyfata.najoan.koreanpatch.driver.InputController;
import com.hyfata.najoan.koreanpatch.driver.InputManager;
import com.hyfata.najoan.koreanpatch.util.ReflectionFieldChecker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.MultiLineEditBox;
import net.minecraft.client.gui.font.TextFieldHelper;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.*;
import net.minecraft.client.gui.screens.options.controls.KeyBindsScreen;

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
                KeyBindsScreen.class,
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
                MerchantScreen.class,

                ModernSettingsScreen.class // Embedded settings screen in this mod
        };

        patchedScreenClazz = getExistingClasses(imeDisabledScreens);
        patchedScreenClazz.addAll(Arrays.asList(imeDisabledClasses));
    }

    public static void afterScreenChange() {
        Screen screen = Minecraft.getInstance().screen;
        if (screen == null || !KoreanPatchClient.loaded) return;

        GUIStatus.getInstance().setBypassInjection(isInjectionBypassScreen(screen));

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

        if (client.screen == null && !GUIStatus.getInstance().isShouldUseIME() && categoryInput.isDisableImeWhenPlaying()) {
            InputManager.getController().setFocus(false);
        } else if (GUIStatus.getInstance().isShouldUseIME()) {
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
        boolean hasMultilineEditBox = ReflectionFieldChecker.hasFieldOfType(screen, MultiLineEditBox.class);
        boolean hasSelectionManager = ReflectionFieldChecker.hasFieldOfType(screen, TextFieldHelper.class);
        return hasTextFieldWidget || hasMultilineEditBox || hasSelectionManager;
    }
}
