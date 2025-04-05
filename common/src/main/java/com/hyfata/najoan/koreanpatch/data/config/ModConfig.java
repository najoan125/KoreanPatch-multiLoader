package com.hyfata.najoan.koreanpatch.data.config;

import com.hyfata.najoan.koreanpatch.data.config.category.CategoryIndicator;

public class ModConfig {
    private CategoryIndicator categoryIndicator = new CategoryIndicator();

    public ModConfig() {}

    public ModConfig(CategoryIndicator categoryIndicator) {
        this.categoryIndicator = categoryIndicator;
    }

    public CategoryIndicator getCategoryIndicator() {
        return categoryIndicator;
    }
}
