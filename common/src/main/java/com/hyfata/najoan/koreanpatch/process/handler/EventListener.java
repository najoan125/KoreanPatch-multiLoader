package com.hyfata.najoan.koreanpatch.process.handler;

import com.hyfata.najoan.koreanpatch.client.KoreanPatchClient;
import com.hyfata.najoan.koreanpatch.data.ConfigManager;
import com.hyfata.najoan.koreanpatch.data.LangTypeManager;
import com.hyfata.najoan.koreanpatch.data.config.category.CategoryInput;
import com.hyfata.najoan.koreanpatch.data.config.category.input.AutoLangTypeMode;
import com.hyfata.najoan.koreanpatch.data.provider.LanguageType;
import com.hyfata.najoan.koreanpatch.gui.GUIStatus;
import com.hyfata.najoan.koreanpatch.process.ime.InputManager;
import com.hyfata.najoan.koreanpatch.util.ReflectionFieldChecker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.font.TextFieldHelper;
import net.minecraft.client.gui.screens.inventory.*;
import net.minecraft.client.gui.screens.options.controls.KeyBindsScreen;

import java.util.ArrayList;
import java.util.Arrays;

public class EventListener {
    private static ArrayList<Class<?>> patchedScreenClazz = new ArrayList<>();

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
                MerchantScreen.class
        };

        patchedScreenClazz = getExistingClasses(imeDisabledScreens);
        patchedScreenClazz.addAll(Arrays.asList(imeDisabledClasses));
    }

    public static void afterScreenChange() {
        Minecraft client = Minecraft.getInstance();

        if (client.screen != null) {
//            Constants.LOG.info("Screen changed: " + client.screen.getClass()); // debug
            // injection bypass screens
            Class<?>[] bypassScreens = {JigsawBlockEditScreen.class, StructureBlockEditScreen.class};
            GUIStatus.setBypassInjection(Arrays.stream(bypassScreens)
                    .anyMatch(cls -> cls.isInstance(client.screen)));

            // IME set focus
            boolean screenPatched = false;
            for (Class<?> cls : patchedScreenClazz) {
                if (cls.isInstance(client.screen)) {
                    screenPatched = true;
                    break;
                }
            }

            if (InputManager.getController() != null) {
                if (!screenPatched) {
                    boolean hasTextFieldWidget = ReflectionFieldChecker.hasFieldOfType(client.screen, EditBox.class);
                    boolean hasSelectionManager = ReflectionFieldChecker.hasFieldOfType(client.screen, TextFieldHelper.class);
                    if (!hasTextFieldWidget && !hasSelectionManager) {
                        InputManager.getController().setFocus(true);
                    } else {
                        InputManager.getController().setFocus(false);
                        setLangType();
                    }
                } else {
                    InputManager.getController().setFocus(false);
                    setLangType();
                }
            }
        }
    }

    public static void onClientTick() {
        Minecraft client = Minecraft.getInstance();
        if (InputManager.getController() == null) return;

        if (client.screen == null && !GUIStatus.isShouldUseIME()) {
            InputManager.getController().setFocus(false);
        } else if (GUIStatus.isShouldUseIME()) {
            InputManager.getController().setFocus(true);
        }
    }

    private static void setLangType() {
        CategoryInput categoryInput = ConfigManager.getConfig().getCategoryInput();
        AutoLangTypeMode mode = categoryInput.getAutoLangTypeMode();

        if (mode != AutoLangTypeMode.AUTO) {
            switch (mode) {
                case KOREAN -> LangTypeManager.setCurrentType(LanguageType.KO);
                case ENGLISH -> LangTypeManager.setCurrentType(LanguageType.EN);
                case IME -> LanguageType.setIME(true);
            }
        }

        if (categoryInput.isMemoryLangTypePerScreen()) {
            // TODO: memory lang type per screen
            // make HashMap<String, LanguageType> and save it(realtime) when screen changed
            // save bin file when screen closed
        }
    }

    private static ArrayList<Class<?>> getExistingClasses(String[] clazz) {
        ArrayList<Class<?>> result = new ArrayList<>();
        for (String className : clazz) {
            try {
                Class<?> cls = Class.forName(className);
                result.add(cls);
            } catch (ClassNotFoundException ignored) {}
        }
        return result;
    }
}
