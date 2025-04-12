package com.hyfata.najoan.koreanpatch.gui.yacl.category;

import com.hyfata.najoan.koreanpatch.data.config.ModConfig;
import com.hyfata.najoan.koreanpatch.data.config.category.indicator.IndicatorAnimationConfig;
import com.hyfata.najoan.koreanpatch.data.config.category.indicator.IndicatorBackgroundColorConfig;
import com.hyfata.najoan.koreanpatch.data.config.category.indicator.IndicatorTextColorConfig;
import com.hyfata.najoan.koreanpatch.data.config.category.indicator.outline.OutlineConfig;
import com.hyfata.najoan.koreanpatch.data.provider.EasingFunctions;
import com.hyfata.najoan.koreanpatch.data.provider.OutlineType;
import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.OptionGroup;
import dev.isxander.yacl3.api.controller.ColorControllerBuilder;
import dev.isxander.yacl3.api.controller.EnumControllerBuilder;
import dev.isxander.yacl3.api.controller.IntegerSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import net.minecraft.network.chat.Component;

import java.awt.*;
import java.util.Arrays;
import java.util.stream.Collectors;

public class IndicatorConfigScreenFactory {

    public static ConfigCategory createCategory(ModConfig config) {
        ConfigCategory.Builder category = ConfigCategory.createBuilder()
                .name(Component.translatable("koreanpatch.config.indicator"))
                .tooltip(Component.translatable("koreanpatch.config.indicator.description"));

        OptionGroup general = generalGroup(config);
        OptionGroup outline = outlineGroup(config);
        OptionGroup background = backgroundGroup(config);
        OptionGroup text = textGroup(config);
        OptionGroup animation = animationGroup(config);

        category.group(general);
        category.group(outline);
        category.group(background);
        category.group(text);
        category.group(animation);

        return category.build();
    }

    private static OptionGroup generalGroup(ModConfig config) {
        OptionGroup.Builder group = OptionGroup.createBuilder()
                .name(Component.translatable("koreanpatch.config.indicator.general"))
                .description(OptionDescription.of(Component.translatable("koreanpatch.config.indicator.general.description")));

        Option<Boolean> showOption = Option.<Boolean>createBuilder()
                .name(Component.translatable("koreanpatch.config.indicator.general.show"))
                .description(OptionDescription.of(Component.translatable("koreanpatch.config.indicator.general.show.description")))
                .binding(
                        config.getCategoryIndicator().isShowIndicator(),
                        config.getCategoryIndicator()::isShowIndicator,
                        config.getCategoryIndicator()::setShowIndicator
                )
                .controller(TickBoxControllerBuilder::create)
                .build();

        group.option(showOption);

        return group.build();
    }

    private static OptionGroup outlineGroup(ModConfig config) {
        OptionGroup.Builder group = OptionGroup.createBuilder()
                .name(Component.translatable("koreanpatch.config.indicator.outline"))
                .description(OptionDescription.of(Component.translatable("koreanpatch.config.indicator.outline.description")));

        OutlineConfig outline = config.getCategoryIndicator().getOutlineSettings();

        Option<Boolean> showOption = Option.<Boolean>createBuilder()
                .name(Component.translatable("koreanpatch.config.indicator.outline.show"))
                .description(OptionDescription.of(Component.translatable("koreanpatch.config.indicator.outline.show.description")))
                .binding(
                        outline.isShowOutline(),
                        outline::isShowOutline,
                        outline::setShowOutline
                )
                .controller(TickBoxControllerBuilder::create)
                .build();

        Option<OutlineType> outlineTypeOption = Option.<OutlineType>createBuilder()
                .name(Component.translatable("koreanpatch.config.indicator.outline.outline_type"))
                .description(OptionDescription.of(Component.translatable("koreanpatch.config.indicator.outline.outline_type.description")))
                .binding(
                        outline.getOutlineType(),
                        outline::getOutlineType,
                        outline::setOutlineType
                )
                .controller(option -> EnumControllerBuilder.create(option)
                .enumClass(OutlineType.class)
                .formatValue(value -> Component.literal(
                        Arrays.stream(value.name().split("_"))
                                .map(w -> w.charAt(0) + w.substring(1).toLowerCase())
                                .collect(Collectors.joining(" ")))
                ))
                .build();

        Option<Color> koColorOption = Option.<Color>createBuilder()
                .name(Component.translatable("koreanpatch.config.color_opacity.ko"))
                .description(OptionDescription.of(Component.translatable("koreanpatch.config.color_opacity.ko.description")))
                .binding(
                        outline.getColorOpacitySettings().getKoreanColor(),
                        outline.getColorOpacitySettings()::getKoreanColor,
                        outline.getColorOpacitySettings()::setKoreanColor
                )
                .controller(ColorControllerBuilder::create)
                .build();

        Option<Color> enColorOption = Option.<Color>createBuilder()
                .name(Component.translatable("koreanpatch.config.color_opacity.en"))
                .description(OptionDescription.of(Component.translatable("koreanpatch.config.color_opacity.en.description")))
                .binding(
                        outline.getColorOpacitySettings().getEnColor(),
                        outline.getColorOpacitySettings()::getEnColor,
                        outline.getColorOpacitySettings()::setEnColor
                )
                .controller(ColorControllerBuilder::create)
                .build();

        Option<Color> imeColorOption = Option.<Color>createBuilder()
                .name(Component.translatable("koreanpatch.config.color_opacity.ime"))
                .description(OptionDescription.of(Component.translatable("koreanpatch.config.color_opacity.ime.description")))
                .binding(
                        outline.getColorOpacitySettings().getImeColor(),
                        outline.getColorOpacitySettings()::getImeColor,
                        outline.getColorOpacitySettings()::setImeColor
                )
                .controller(ColorControllerBuilder::create)
                .build();

        Option<Integer> opacityOption = Option.<Integer>createBuilder()
                .name(Component.translatable("koreanpatch.config.color_opacity.opacity"))
                .description(OptionDescription.of(Component.translatable("koreanpatch.config.indicator.outline.opacity.description")))
                .binding(
                        outline.getColorOpacitySettings().getOpacity(),
                        outline.getColorOpacitySettings()::getOpacity,
                        outline.getColorOpacitySettings()::setOpacity
                )
                .controller(option -> IntegerSliderControllerBuilder.create(option)
                        .range(0, 100)
                        .step(5)
                        .formatValue(value -> Component.literal(String.format("%d%%", value)))
                )
                .build();

        group.option(showOption);
        group.option(outlineTypeOption);
        group.option(koColorOption);
        group.option(enColorOption);
        group.option(imeColorOption);
        group.option(opacityOption);

        return group.build();
    }

    private static OptionGroup backgroundGroup(ModConfig config) {
        OptionGroup.Builder group = OptionGroup.createBuilder()
                .name(Component.translatable("koreanpatch.config.indicator.background"))
                .description(OptionDescription.of(Component.translatable("koreanpatch.config.indicator.background.description")));

        IndicatorBackgroundColorConfig background = config.getCategoryIndicator().getBackgroundSettings();

        Option<Color> koColorOption = Option.<Color>createBuilder()
                .name(Component.translatable("koreanpatch.config.color_opacity.ko"))
                .description(OptionDescription.of(Component.translatable("koreanpatch.config.color_opacity.ko.description")))
                .binding(
                        background.getKoreanColor(),
                        background::getKoreanColor,
                        background::setKoreanColor
                )
                .controller(ColorControllerBuilder::create)
                .build();

        Option<Color> enColorOption = Option.<Color>createBuilder()
                .name(Component.translatable("koreanpatch.config.color_opacity.en"))
                .description(OptionDescription.of(Component.translatable("koreanpatch.config.color_opacity.en.description")))
                .binding(
                        background.getEnColor(),
                        background::getEnColor,
                        background::setEnColor
                )
                .controller(ColorControllerBuilder::create)
                .build();

        Option<Color> imeColorOption = Option.<Color>createBuilder()
                .name(Component.translatable("koreanpatch.config.color_opacity.ime"))
                .description(OptionDescription.of(Component.translatable("koreanpatch.config.color_opacity.ime.description")))
                .binding(
                        background.getImeColor(),
                        background::getImeColor,
                        background::setImeColor
                )
                .controller(ColorControllerBuilder::create)
                .build();

        Option<Integer> opacityOption = Option.<Integer>createBuilder()
                .name(Component.translatable("koreanpatch.config.color_opacity.opacity"))
                .description(OptionDescription.of(Component.translatable("koreanpatch.config.indicator.background.opacity.description")))
                .binding(
                        background.getOpacity(),
                        background::getOpacity,
                        background::setOpacity
                )
                .controller(option -> IntegerSliderControllerBuilder.create(option)
                        .range(0, 100)
                        .step(5)
                        .formatValue(value -> Component.literal(String.format("%d%%", value)))
                )
                .build();

        group.option(koColorOption);
        group.option(enColorOption);
        group.option(imeColorOption);
        group.option(opacityOption);

        return group.build();
    }

    private static OptionGroup textGroup(ModConfig config) {
        OptionGroup.Builder group = OptionGroup.createBuilder()
                .name(Component.translatable("koreanpatch.config.indicator.text"))
                .description(OptionDescription.of(Component.translatable("koreanpatch.config.indicator.text.description")));

        IndicatorTextColorConfig text = config.getCategoryIndicator().getTextSettings();

        Option<Color> koColorOption = Option.<Color>createBuilder()
                .name(Component.translatable("koreanpatch.config.color_opacity.ko"))
                .description(OptionDescription.of(Component.translatable("koreanpatch.config.color_opacity.ko.description")))
                .binding(
                        text.getKoreanColor(),
                        text::getKoreanColor,
                        text::setKoreanColor
                )
                .controller(ColorControllerBuilder::create)
                .build();

        Option<Color> enColorOption = Option.<Color>createBuilder()
                .name(Component.translatable("koreanpatch.config.color_opacity.en"))
                .description(OptionDescription.of(Component.translatable("koreanpatch.config.color_opacity.en.description")))
                .binding(
                        text.getEnColor(),
                        text::getEnColor,
                        text::setEnColor
                )
                .controller(ColorControllerBuilder::create)
                .build();

        Option<Color> imeColorOption = Option.<Color>createBuilder()
                .name(Component.translatable("koreanpatch.config.color_opacity.ime"))
                .description(OptionDescription.of(Component.translatable("koreanpatch.config.color_opacity.ime.description")))
                .binding(
                        text.getImeColor(),
                        text::getImeColor,
                        text::setImeColor
                )
                .controller(ColorControllerBuilder::create)
                .build();

        Option<Integer> opacityOption = Option.<Integer>createBuilder()
                .name(Component.translatable("koreanpatch.config.color_opacity.opacity"))
                .description(OptionDescription.of(Component.translatable("koreanpatch.config.indicator.text.opacity.description")))
                .binding(
                        text.getOpacity(),
                        text::getOpacity,
                        text::setOpacity
                )
                .controller(option -> IntegerSliderControllerBuilder.create(option)
                        .range(0, 100)
                        .step(5)
                        .formatValue(value -> Component.literal(String.format("%d%%", value)))
                )
                .build();

        group.option(koColorOption);
        group.option(enColorOption);
        group.option(imeColorOption);
        group.option(opacityOption);

        return group.build();
    }

    private static OptionGroup animationGroup(ModConfig config) {
        OptionGroup.Builder group = OptionGroup.createBuilder()
                .name(Component.translatable("koreanpatch.config.indicator.animation"))
                .description(OptionDescription.of(Component.translatable("koreanpatch.config.indicator.animation.description")));

        IndicatorAnimationConfig animation = config.getCategoryIndicator().getAnimationSettings();

        Option<Boolean> showOption = Option.<Boolean>createBuilder()
                .name(Component.translatable("koreanpatch.config.indicator.animation.show"))
                .description(OptionDescription.of(Component.translatable("koreanpatch.config.indicator.animation.show.description")))
                .binding(
                        animation.isShowAnimation(),
                        animation::isShowAnimation,
                        animation::setShowAnimation
                )
                .controller(TickBoxControllerBuilder::create)
                .build();

        Option<EasingFunctions> easingFunctionsOption = Option.<EasingFunctions>createBuilder()
                .name(Component.translatable("koreanpatch.config.indicator.animation.easing_function"))
                .description(OptionDescription.of(Component.translatable("koreanpatch.config.indicator.animation.easing_function.description")))
                .binding(
                        animation.getEasingFunction(),
                        animation::getEasingFunction,
                        animation::setEasingFunction
                )
                .controller(option -> EnumControllerBuilder.create(option)
                .enumClass(EasingFunctions.class)
                .formatValue(value -> Component.literal(
                        Arrays.stream(value.name().split("_"))
                                .map(w -> w.charAt(0) + w.substring(1).toLowerCase())
                                .collect(Collectors.joining(" ")))
                ))
                .build();

        Option<Integer> speedOption = Option.<Integer>createBuilder()
                .name(Component.translatable("koreanpatch.config.indicator.animation.speed"))
                .description(OptionDescription.of(Component.translatable("koreanpatch.config.indicator.animation.speed.description")))
                .binding(
                        animation.getSpeed(),
                        animation::getSpeed,
                        animation::setSpeed
                )
                .controller(option -> IntegerSliderControllerBuilder.create(option)
                        .range(0, 100)
                        .step(1)
                )
                .build();

        group.option(showOption);
        group.option(easingFunctionsOption);
        group.option(speedOption);

        return group.build();
    }
}
