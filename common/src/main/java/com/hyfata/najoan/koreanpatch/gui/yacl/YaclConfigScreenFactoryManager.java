package com.hyfata.najoan.koreanpatch.gui.yacl;

import com.hyfata.najoan.koreanpatch.data.ConfigManager;
import com.hyfata.najoan.koreanpatch.data.config.ModConfig;
import com.hyfata.najoan.koreanpatch.gui.yacl.category.IndicatorConfigScreenFactory;
import com.hyfata.najoan.koreanpatch.gui.yacl.category.InputConfigScreenFactory;
import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class YaclConfigScreenFactoryManager {

    private static final ModConfig CONFIG = ConfigManager.getInstance().getConfig();

    public static Screen createScreen(Screen parent) {
        ConfigScreenFactory[] factories = {
                new IndicatorConfigScreenFactory(),
                new InputConfigScreenFactory()
        };

        List<ConfigCategory> categories = new ArrayList<>();
        for (ConfigScreenFactory factory : factories) {
            categories.add(factory.createCategory(CONFIG));
        }

        return YetAnotherConfigLib.createBuilder()
                .title(Component.translatable("koreanpatch.config"))
                .categories(categories)
                .save(() -> ConfigManager.getInstance().saveConfig(CONFIG))
                .build()
                .generateScreen(parent);
    }
}
