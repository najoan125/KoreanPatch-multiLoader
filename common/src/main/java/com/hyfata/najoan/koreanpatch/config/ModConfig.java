package com.hyfata.najoan.koreanpatch.config;

import com.hyfata.najoan.koreanpatch.config.category.CategoryIndicator;
import com.hyfata.najoan.koreanpatch.config.category.CategoryInput;

public class ModConfig {
    private final CategoryIndicator categoryIndicator = new CategoryIndicator();
    private final CategoryInput categoryInput = new CategoryInput();

    public CategoryIndicator getCategoryIndicator() {
        return categoryIndicator;
    }

    public CategoryInput getCategoryInput() {
        return categoryInput;
    }
}
