package com.hyfata.najoan.koreanpatch.client;

import com.hyfata.najoan.koreanpatch.plugin.InputManager;
import com.hyfata.najoan.koreanpatch.util.ReflectionFieldChecker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.font.TextFieldHelper;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.JigsawBlockEditScreen;
import net.minecraft.client.gui.screens.inventory.StructureBlockEditScreen;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;

import java.util.ArrayList;
import java.util.Arrays;

@EventBusSubscriber(modid = Constants.MOD_ID)
public class EventListenerNeoForge {
    private static ArrayList<Class<?>> patchedScreenClazz = new ArrayList<>();

    protected static void onClientStarted(FMLClientSetupEvent event) {
        KoreanPatchClient.clientStarted();

        String[] patchedScreens = {
                "arm32x.minecraft.commandblockide.client.gui.screen.CommandIDEScreen"
        };
        patchedScreenClazz = getExistingClasses(patchedScreens);
    }

    @SubscribeEvent
    protected static void afterScreenChange(ScreenEvent.Init.Post event) {
        Minecraft client = Minecraft.getInstance();
        Screen screen = event.getScreen();

        if (client.screen != null) {
            // injection bypass screens
            Class<?>[] bypassScreens = {JigsawBlockEditScreen.class, StructureBlockEditScreen.class};
            KoreanPatchClient.bypassInjection = Arrays.stream(bypassScreens)
                    .anyMatch(cls -> cls.isInstance(client.screen));

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
                    InputManager.getController().setFocus(!hasTextFieldWidget && !hasSelectionManager);
                } else {
                    InputManager.getController().setFocus(false);
                }
            }
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

    @SubscribeEvent
    protected static void onClientTick(ClientTickEvent.Post event) {
        Minecraft client = Minecraft.getInstance();
        if (InputManager.getController() == null) return;

        if (client.screen == null) {
            InputManager.getController().setFocus(false);
        }
    }
}
