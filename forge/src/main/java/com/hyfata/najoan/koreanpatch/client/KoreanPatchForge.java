package com.hyfata.najoan.koreanpatch.client;

import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;

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
        bus.addListener(EventListenerForge::onClientStarted);
        MinecraftForge.EVENT_BUS.addListener(EventListenerForge::onClientTick);
        MinecraftForge.EVENT_BUS.addListener(EventListenerForge::afterScreenChange);
    }

    @SubscribeEvent
    public void registerKeys(RegisterKeyMappingsEvent event) {
        event.register(KeyBinds.getImeBinding());
        event.register(KeyBinds.getLangBinding());
	}
}
