package com.hyfata.najoan.koreanpatch.mixin.mods.commandblockide.indicator;

import arm32x.minecraft.commandblockide.client.gui.MultilineTextFieldWidget;
import arm32x.minecraft.commandblockide.client.gui.editor.CommandEditor;
import com.hyfata.najoan.koreanpatch.util.Indicator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CommandEditor.class)
public abstract class CommandEditorMixin {
    @Shadow(remap = false)
    @Final
    protected MultilineTextFieldWidget commandField;

    @Shadow(remap = false)
    private int y;

    @Unique
    private static final int margin = 5;

    @Unique
    private int orgX = 0;

    @Unique
    private int width = 0, fieldWidth = 0;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void init(Screen screen, Font textRenderer, int x, int y, int width, int height, int leftPadding, int rightPadding, int index, CallbackInfo ci) {
        this.orgX = x + leftPadding + 20;
        this.width = width - screen.width;
        this.fieldWidth = - leftPadding - rightPadding - 20;
    }

    @Inject(at = @At(value = "HEAD"), method = "render")
    public void renderHead(GuiGraphics context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (this.orgX != 0)
            commandField.setX((int) (this.orgX + Indicator.getIndicatorWidth() + margin));
        if (this.width != 0 && Minecraft.getInstance().screen != null) {
            int width = this.width + Minecraft.getInstance().screen.width;
            int totalWidth = width + fieldWidth;
            commandField.setWidth((int) (totalWidth - Indicator.getIndicatorWidth() - margin));
        }
    }

    @Inject(at = @At(value = "INVOKE", target = "Larm32x/minecraft/commandblockide/client/gui/Container;render(Lnet/minecraft/client/gui/GuiGraphics;IIF)V", shift = At.Shift.BEFORE), method = "render")
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (commandField.isFocused() && this.orgX != 0) {
            Indicator.showIndicator(context, (float) this.orgX, (float) (y - Indicator.getIndicatorHeight() / 2 + 7.5));
        }
    }
}
