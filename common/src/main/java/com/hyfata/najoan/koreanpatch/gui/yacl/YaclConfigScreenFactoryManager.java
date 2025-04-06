package com.hyfata.najoan.koreanpatch.gui.yacl;

import com.hyfata.najoan.koreanpatch.data.ConfigManager;
import com.hyfata.najoan.koreanpatch.data.config.ModConfig;
import com.hyfata.najoan.koreanpatch.gui.yacl.category.IndicatorConfigScreenFactory;
import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class YaclConfigScreenFactoryManager {

    private static final ModConfig CONFIG = ConfigManager.getConfig();

    public static Screen createScreen(Screen parent) {
        List<ConfigCategory> categories = new ArrayList<>();

        ConfigCategory indicator = IndicatorConfigScreenFactory.createCategory(CONFIG);

        categories.add(indicator);

        return YetAnotherConfigLib.createBuilder()
                .title(Component.translatable("koreanpatch.config"))
                .categories(categories)
                .save(() -> ConfigManager.saveConfig(CONFIG))
                .build()
                .generateScreen(parent);
    }
}
