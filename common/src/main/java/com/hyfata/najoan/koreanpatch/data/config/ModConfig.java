package com.hyfata.najoan.koreanpatch.data.config;

import com.hyfata.najoan.koreanpatch.data.config.category.CategoryIndicator;
import com.hyfata.najoan.koreanpatch.data.config.category.CategoryInput;
import com.hyfata.najoan.koreanpatch.data.config.category.CategoryOther;

public class ModConfig {
    private final CategoryIndicator categoryIndicator = new CategoryIndicator();
    private final CategoryInput categoryInput = new CategoryInput();
    private final CategoryOther categoryOther = new CategoryOther();

    public CategoryIndicator getCategoryIndicator() {
        return categoryIndicator;
    }

    public CategoryInput getCategoryInput() {
        return categoryInput;
    }

    public CategoryOther getCategoryOther() {
        return categoryOther;
    }
}
