package com.hyfata.najoan.koreanpatch.config.yacl;

import com.hyfata.najoan.koreanpatch.client.Constants;
import com.hyfata.najoan.koreanpatch.config.ModConfig;
import dev.isxander.yacl3.api.ConfigCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public abstract class ConfigScreenFactory {

    /**
     * A translation key pointing to <b>category</b><br>
     * This field's value is like 'koreanpatch.config.category'
     */
    private String categoryTranslationKey;

    protected abstract ConfigCategory createCategory(ModConfig config);

    /**
     * Creates a {@link ConfigCategory.Builder} and initializes {@link #categoryTranslationKey}.
     *
     * @param category The category name from translation key.
     * @return A builder for creating a {@link ConfigCategory} object, <br>
     * including the category name and tooltip.
     */
    protected ConfigCategory.Builder createCategoryBuilder(String category) {
        this.categoryTranslationKey = Constants.MOD_ID + ".config." + category;
        return ConfigCategory.createBuilder()
                .name(createTranslatableComponent())
                .tooltip(createTranslatableComponent("description"));
    }

    /**
     * Creates a {@link MutableComponent} based on the translation key.
     *
     * @param keys An array of key strings used to construct the translation key.<br>
     *             If the array is empty, return {@link #categoryTranslationKey}.<br>
     *             If the array is not empty, {@link #categoryTranslationKey} is combined with the elements of the array,
     *             separated by ".", to create the translation key.
     * @return The created {@link MutableComponent} object.
     */
    protected MutableComponent createTranslatableComponent(String... keys) {
        return Component.translatable(createTranslationKey(keys));
    }

    /**
     * Constructs a translation key by combining the category translation key and<br>
     * additional key strings, separated by a period. <br>
     * If no additional key strings are provided, the method returns the category translation key.
     *
     * @param keys An array of strings representing additional parts of the translation key.
     *             If no keys are provided, only the category translation key is returned.
     * @return A string representing the constructed translation key.
     */
    protected String createTranslationKey(String... keys) {
        return keys.length == 0 ? categoryTranslationKey :
                categoryTranslationKey + "." + String.join(".", keys);
    }

    protected MutableComponent formatedColorOpacityDesc(String group, String type) {
        String name = createTranslatableComponent(group, "name").getString();
        String translated;
        switch (type) {
            case "ko":
                translated = Component.translatable("koreanpatch.config.color_opacity.ko.description").getString();
                break;
            case "en":
                translated = Component.translatable("koreanpatch.config.color_opacity.en.description").getString();
                break;
            case "ime":
                translated = Component.translatable("koreanpatch.config.color_opacity.ime.description").getString();
                break;
            case "opacity":
                translated = Component.translatable("koreanpatch.config.color_opacity.description").getString();
                break;
            default:
                return null;
        }
        return Component.literal(String.format(translated, name));
    }
}
