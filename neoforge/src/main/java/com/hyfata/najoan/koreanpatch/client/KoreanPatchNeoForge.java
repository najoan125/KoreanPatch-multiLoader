package com.hyfata.najoan.koreanpatch.client;

import com.hyfata.najoan.koreanpatch.gui.yacl.YaclConfigScreenFactoryManager;
import com.hyfata.najoan.koreanpatch.platform.Services;
import com.hyfata.najoan.koreanpatch.process.handler.EventListener;
import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;

@Mod(value = Constants.MOD_ID, dist = Dist.CLIENT)
public class KoreanPatchNeoForge {

    public KoreanPatchNeoForge(IEventBus bus, ModContainer container) {
        KoreanPatchClient.init();
        bus.addListener(this::registerKeys);

        registerEvents(bus);

        if (Services.PLATFORM.isModLoaded("yet_another_config_lib_v3")) {
            ModLoadingContext.get().registerExtensionPoint(
                    IConfigScreenFactory.class,
                    () -> (client, parent) -> YaclConfigScreenFactoryManager.createScreen(parent));
        }
    }

    public void registerEvents(IEventBus bus) {
        bus.addListener(KoreanPatchNeoForge::onClientStarted);
        NeoForge.EVENT_BUS.addListener(KoreanPatchNeoForge::onClientTick);
        NeoForge.EVENT_BUS.addListener(KoreanPatchNeoForge::afterScreenChange);
    }

    @SubscribeEvent
    public void registerKeys(RegisterKeyMappingsEvent event) {
        for (KeyMapping key : KeyBinds.getKeyMappings()) {
            event.register(key);
        }
	}

    @SubscribeEvent
    public static void onClientStarted(FMLClientSetupEvent event) {
        EventListener.onClientStarted();
    }

    @SubscribeEvent
    public static void afterScreenChange(ScreenEvent.Init.Post event) {
        EventListener.afterScreenChange();
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        EventListener.onClientTick();
    }
}
