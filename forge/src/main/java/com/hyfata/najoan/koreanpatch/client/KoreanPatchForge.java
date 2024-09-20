package com.hyfata.najoan.koreanpatch.client;

import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod(value = Constants.MOD_ID)
public class KoreanPatchForge {

    public KoreanPatchForge() {
        IEventBus bus = Mod.EventBusSubscriber.Bus.MOD.bus().get();
        ModContainer container = ModLoadingContext.get().getActiveContainer();
        init(bus, container);
    }

    private KoreanPatchForge(IEventBus bus, ModContainer container) {
        KoreanPatchClient.init();
        bus.addListener(this::registerKeys);

        registerEvents(bus);
    }

    private void init(IEventBus bus, ModContainer container) {
        new KoreanPatchForge(bus, container);
    }

    public static void registerEvents(IEventBus bus) {
        bus.addListener(KoreanPatchForge::onClientStarted);
        MinecraftForge.EVENT_BUS.addListener(KoreanPatchForge::onClientTick);
        MinecraftForge.EVENT_BUS.addListener(KoreanPatchForge::afterScreenChange);
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
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            EventListener.onClientTick();
        }
    }
}
