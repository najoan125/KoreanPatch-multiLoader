package com.hyfata.najoan.koreanpatch.mixin.indicator;

import com.hyfata.najoan.koreanpatch.mixin.helper.BookScreenVar;
import com.hyfata.najoan.koreanpatch.mixin.accessor.MultilineEditBoxAccessor;
import com.hyfata.najoan.koreanpatch.indicator.AnimationHandler;
import com.hyfata.najoan.koreanpatch.indicator.IndicatorHandler;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.MultiLineEditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.BookEditScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.WritableBookContent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = {BookEditScreen.class})
public abstract class BookEditScreenMixin extends Screen {

    @Shadow private MultiLineEditBox page;

    protected BookEditScreenMixin(Component title) {
        super(title);
    }

    @Inject(at = @At("TAIL"), method = "<init>")
    private void init(Player owner, ItemStack book, InteractionHand hand, WritableBookContent content, CallbackInfo ci) {
        BookScreenVar.animationHandler = new AnimationHandler();
    }

    @Inject(at = {@At(value = "RETURN")}, method = {"render"})
    private void addCustomLabel(GuiGraphics context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        MultilineEditBoxAccessor accessor = (MultilineEditBoxAccessor) this.page;
        float x = (this.width - 192) / 2f; // int i = (this.width - 192) / 2; in render() method

        int innerPadding = 4; // ref: MultilineEditBox.seekCursorScreen()
        int lineAtCursor = accessor.getTextField().getLineAtCursor() + 1;
        float y = (float) (this.page.getY() + innerPadding - this.page.scrollAmount() + lineAtCursor * 9f - 4.5f);

        BookScreenVar.animationHandler.init(0, y - 4);
        BookScreenVar.animationHandler.calculateAnimation(0, y);

        IndicatorHandler.showCenteredIndicator(context, x + 10, BookScreenVar.animationHandler.getResultY());
    }
}

