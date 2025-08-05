package com.hyfata.najoan.koreanpatch.config.yacl.category;

import com.hyfata.najoan.koreanpatch.config.ModConfig;
import com.hyfata.najoan.koreanpatch.config.category.CategoryInput;
import com.hyfata.najoan.koreanpatch.config.category.input.AutoLangTypeMode;
import com.hyfata.najoan.koreanpatch.config.yacl.ConfigScreenFactory;
import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.OptionGroup;
import dev.isxander.yacl3.api.controller.EnumControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import net.minecraft.network.chat.Component;

public class InputConfigScreenFactory extends ConfigScreenFactory {

    @Override
    protected ConfigCategory createCategory(ModConfig config) {
        ConfigCategory.Builder category = createCategoryBuilder("input");

        OptionGroup general = generalGroup(config);
        OptionGroup ime = imeGroup(config);

        category.group(general);
        category.group(ime);

        return category.build();
    }

    private OptionGroup generalGroup(ModConfig config) {
        OptionGroup.Builder group = OptionGroup.createBuilder()
                .name(createTranslatableComponent("general"))
                .description(OptionDescription.of(createTranslatableComponent("general.description")));

        CategoryInput categoryInput = config.getCategoryInput();

        Option<AutoLangTypeMode> autoLangTypeModeOption = Option.<AutoLangTypeMode>createBuilder()
                .name(createTranslatableComponent("general.auto_lang_type_mode"))
                .description(OptionDescription.of(createTranslatableComponent("general.auto_lang_type_mode.description")))
                .binding(
                        categoryInput.getAutoLangTypeMode(),
                        categoryInput::getAutoLangTypeMode,
                        categoryInput::setAutoLangTypeMode
                )
                .controller(option -> EnumControllerBuilder.create(option)
                .enumClass(AutoLangTypeMode.class)
                .formatValue(value -> Component.translatable(
                        value.getTranslatable()
                ))
                ).build();

        Option<Boolean> memoryLangTypeOption = Option.<Boolean>createBuilder()
                .name(createTranslatableComponent("general.memory_lang_type"))
                .description(OptionDescription.of(createTranslatableComponent("general.memory_lang_type.description")))
                .binding(
                        categoryInput.isMemoryLangTypePerScreen(),
                        categoryInput::isMemoryLangTypePerScreen,
                        categoryInput::setMemoryLangTypePerScreen
                )
                .controller(TickBoxControllerBuilder::create)
                .build();

        group.option(autoLangTypeModeOption);
        group.option(memoryLangTypeOption);

        return group.build();
    }

    private OptionGroup imeGroup(ModConfig config) {
        OptionGroup.Builder group = OptionGroup.createBuilder()
                .name(createTranslatableComponent("ime"))
                .description(OptionDescription.of(createTranslatableComponent("ime.description")));

        CategoryInput categoryInput = config.getCategoryInput();

        Option<Boolean> disableImePlaying = Option.<Boolean>createBuilder()
                .name(createTranslatableComponent("ime.disable_ime_playing"))
                .description(OptionDescription.of(createTranslatableComponent("ime.disable_ime_playing.description")))
                .binding(
                        categoryInput.isDisableImeWhenPlaying(),
                        categoryInput::isDisableImeWhenPlaying,
                        categoryInput::setDisableImeWhenPlaying
                )
                .controller(TickBoxControllerBuilder::create)
                .build();

        Option<Boolean> autoImeSwitch = Option.<Boolean>createBuilder()
                .name(createTranslatableComponent("ime.auto_ime_switch"))
                .description(OptionDescription.of(createTranslatableComponent("ime.auto_ime_switch.description")))
                .binding(
                        categoryInput.isAutoImeSwitch(),
                        categoryInput::isAutoImeSwitch,
                        categoryInput::setAutoImeSwitch
                )
                .controller(TickBoxControllerBuilder::create)
                .build();

        Option<Boolean> alwaysImeEnabled = Option.<Boolean>createBuilder()
                .name(createTranslatableComponent("ime.always_ime_enabled"))
                .description(OptionDescription.of(createTranslatableComponent("ime.always_ime_enabled.description")))
                .binding(
                        categoryInput.isAlwaysImeEnabled(),
                        categoryInput::isAlwaysImeEnabled,
                        categoryInput::setAlwaysImeEnabled
                )
                .controller(TickBoxControllerBuilder::create)
                .build();

        group.option(disableImePlaying);
        group.option(autoImeSwitch);
        group.option(alwaysImeEnabled);

        return group.build();
    }
}
