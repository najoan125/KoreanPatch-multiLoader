package com.hyfata.najoan.koreanpatch.gui.yacl.category;

import com.hyfata.najoan.koreanpatch.data.config.ModConfig;
import com.hyfata.najoan.koreanpatch.gui.yacl.ConfigScreenFactory;
import dev.isxander.yacl3.api.ConfigCategory;

public class InputConfigScreenFactory extends ConfigScreenFactory {

    @Override
    protected ConfigCategory createCategory(ModConfig config) {
        ConfigCategory.Builder category = createCategoryBuilder("input");
        return category.build();
    }
}
