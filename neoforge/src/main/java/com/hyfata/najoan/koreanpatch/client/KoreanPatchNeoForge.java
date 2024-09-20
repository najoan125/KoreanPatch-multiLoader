package com.hyfata.najoan.koreanpatch.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.common.NeoForge;

@Mod(value = Constants.MOD_ID, dist = Dist.CLIENT)
public class KoreanPatchNeoForge {

    public KoreanPatchNeoForge(IEventBus bus, ModContainer container) {
        KoreanPatchClient.init();
        bus.addListener(this::registerKeys);

        registerEvents(bus);
    }

    public void registerEvents(IEventBus bus) {
        bus.addListener(KoreanPatchNeoForge::onClientStarted);
        NeoForge.EVENT_BUS.addListener(KoreanPatchNeoForge::onClientTick);
        NeoForge.EVENT_BUS.addListener(KoreanPatchNeoForge::afterScreenChange);
    }

    @SubscribeEvent
    public void registerKeys(RegisterKeyMappingsEvent event) {
        event.register(KeyBinds.getImeBinding());
        event.register(KeyBinds.getLangBinding());
	}

    @SubscribeEvent
    public static void onClientStarted(FMLClientSetupEvent event) {
        EventListener.onClientStarted();
    }

    @SubscribeEvent
    public static void afterScreenChange(ScreenEvent.Init.Post event) {
        EventListener.afterScreenChange(event.getScreen());
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        EventListener.onClientTick();
    }
}
