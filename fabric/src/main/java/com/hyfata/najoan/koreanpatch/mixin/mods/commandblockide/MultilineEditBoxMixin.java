package com.hyfata.najoan.koreanpatch.mixin.mods.commandblockide;

import arm32x.minecraft.commandblockide.client.gui.MultilineTextFieldWidget;
import com.hyfata.najoan.koreanpatch.client.KoreanPatchClient;
import com.hyfata.najoan.koreanpatch.util.mixin.textfieldwidget.IEditBoxAccessor;
import com.hyfata.najoan.koreanpatch.util.mixin.textfieldwidget.EditBoxHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MultilineTextFieldWidget.class)
public abstract class MultilineEditBoxMixin extends EditBox implements IEditBoxAccessor {
    public MultilineEditBoxMixin(Font textRenderer, int width, int height, Component text) {
        super(textRenderer, width, height, text);
    }

    @Unique
    private final EditBoxHandler handler = new EditBoxHandler(this);

    @Inject(at = @At(value = "HEAD"), method = "keyPressed", cancellable = true)
    public void keyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        Minecraft client = Minecraft.getInstance();
        if (client.screen != null && !KoreanPatchClient.bypassInjection) {
            if (keyCode == GLFW.GLFW_KEY_BACKSPACE) {
                if (handler.onBackspaceKeyPressed()) {
                    cir.setReturnValue(Boolean.TRUE);
                }
            }
        }
    }
}
