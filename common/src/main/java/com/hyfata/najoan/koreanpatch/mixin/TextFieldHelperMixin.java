package com.hyfata.najoan.koreanpatch.mixin;

import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

import com.hyfata.najoan.koreanpatch.client.KoreanPatchClient;
import com.hyfata.najoan.koreanpatch.handler.mixin.textfieldhelper.ITextFieldHelperAccessor;
import com.hyfata.najoan.koreanpatch.handler.mixin.textfieldhelper.TextFieldHelperHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.font.TextFieldHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = {TextFieldHelper.class})
public abstract class TextFieldHelperMixin implements ITextFieldHelperAccessor {
    @Shadow
    private int selectionPos;
    @Shadow
    @Final
    private Supplier<String> getMessageFn;
    @Shadow
    @Final
    private Predicate<String> stringValidator;
    @Shadow
    @Final
    private Consumer<String> setMessageFn;

    @Shadow
    protected abstract String getSelected(String string);

    @Shadow
    protected abstract void insertText(String string, String insertion);

    @Unique
    private final TextFieldHelperHandler koreanPatch$handler = new TextFieldHelperHandler(this);

    @Override
    public int koreanPatch$getCursor() {
        return this.selectionPos;
    }

    @Override
    public Supplier<String> koreanPatch$getStringGetter() {
        return this.getMessageFn;
    }

    @Override
    public Predicate<String> koreanPatch$getStringFilter() {
        return this.stringValidator;
    }

    @Override
    public Consumer<String> koreanPatch$getStringSetter() {
        return this.setMessageFn;
    }

    @Override
    public String koreanPatch$selectedText(String string) {
        return getSelected(string);
    }

    @Override
    public void koreanPatch$runInsert(String string, String insertion) {
        insertText(string, insertion);
    }

    @Inject(at = {@At(value = "HEAD")}, method = {"charTyped(C)Z"}, cancellable = true)
    public void insertChar(char chr, CallbackInfoReturnable<Boolean> cir) {
        koreanPatch$handler.insertChar(chr, cir);
    }

    @Inject(at = {@At(value = "HEAD")}, method = {"insertText(Ljava/lang/String;)V"}, cancellable = true)
    public void insertString(String string, CallbackInfo ci) {
        koreanPatch$handler.insertString(string, ci);
    }

    @Inject(at = {@At(value = "HEAD")}, method = {"removeCharsFromCursor(I)V"}, cancellable = true)
    public void delete(int offset, CallbackInfo ci) {
        Minecraft client = Minecraft.getInstance();
        if (client.screen != null && !KoreanPatchClient.bypassInjection) {
            if (koreanPatch$handler.onBackspaceKeyPressed()) {
                ci.cancel();
            }
        }
    }
}

