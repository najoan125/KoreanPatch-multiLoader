package com.hyfata.najoan.koreanpatch.client;

import com.hyfata.najoan.koreanpatch.data.ConfigManager;
import com.hyfata.najoan.koreanpatch.process.handler.EventListener;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.client.ConfigGuiHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fmlclient.registry.ClientRegistry;

@Mod(value = Constants.MOD_ID)
public class KoreanPatchForge {

    public KoreanPatchForge() {
        IEventBus bus = Mod.EventBusSubscriber.Bus.MOD.bus().get();
        new KoreanPatchForge(bus);
    }

    private KoreanPatchForge(IEventBus bus) {
        KoreanPatchClient.init();
        registerKeys();

        registerEvents(bus);

        ModLoadingContext.get().registerExtensionPoint(ConfigGuiHandler.ConfigGuiFactory.class,
                () -> new ConfigGuiHandler.ConfigGuiFactory(
                        (client, parent) -> new Screen(new TextComponent("")) {
                            @Override
                            protected void init() {
                                ConfigManager.getInstance().openConfigFile();
                                client.setScreen(parent);
                            }
                        }
                )
        );
    }

    public static void registerEvents(IEventBus bus) {
        bus.addListener(KoreanPatchForge::onClientStarted);
        MinecraftForge.EVENT_BUS.addListener(KoreanPatchForge::onClientTick);
        MinecraftForge.EVENT_BUS.addListener(KoreanPatchForge::afterScreenChange);
    }


    public void registerKeys() {
        for (KeyMapping key : KeyBinds.getKeyMappings()) {
            ClientRegistry.registerKeyBinding(key);
        }
    }

    @SubscribeEvent
    public static void onClientStarted(FMLClientSetupEvent event) {
        EventListener.onClientStarted();
    }

    @SubscribeEvent
    public static void afterScreenChange(GuiScreenEvent.InitGuiEvent.Post event) {
        EventListener.afterScreenChange();
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        EventListener.onClientTick();
    }
}
