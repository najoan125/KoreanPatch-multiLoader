package com.hyfata.najoan.koreanpatch.config;

import com.hyfata.najoan.koreanpatch.config.category.CategoryIndicator;
import com.hyfata.najoan.koreanpatch.config.category.CategoryInput;
import com.hyfata.najoan.koreanpatch.config.category.CategoryKeyBindings;

public class ModConfig {
    private final CategoryIndicator categoryIndicator = new CategoryIndicator();
    private final CategoryInput categoryInput = new CategoryInput();
    private final CategoryKeyBindings categoryKeyBindings = new CategoryKeyBindings();

    public CategoryIndicator getCategoryIndicator() {
        return categoryIndicator;
    }

    public CategoryInput getCategoryInput() {
        return categoryInput;
    }

    public CategoryKeyBindings getCategoryKeyBindings() {
        return categoryKeyBindings;
    }
}
