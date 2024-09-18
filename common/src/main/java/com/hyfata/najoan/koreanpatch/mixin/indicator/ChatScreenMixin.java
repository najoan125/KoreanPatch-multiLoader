package com.hyfata.najoan.koreanpatch.mixin.indicator;

import com.hyfata.najoan.koreanpatch.mixin.accessor.CommandSuggestionsAccessor;
import com.hyfata.najoan.koreanpatch.util.animation.AnimationUtil;
import com.hyfata.najoan.koreanpatch.util.Indicator;
import com.hyfata.najoan.koreanpatch.util.EditBoxUtil;
import com.mojang.brigadier.suggestion.Suggestions;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.CommandSuggestions;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = {ChatScreen.class})
public abstract class ChatScreenMixin extends Screen {
    protected ChatScreenMixin(Component title) {
        super(title);
    }

    @Shadow
    protected EditBox input;

    @Shadow
    CommandSuggestions commandSuggestions;

    @Unique
    private final AnimationUtil animationUtil = new AnimationUtil();


    @Inject(at = {@At(value = "TAIL")}, method = {"render"})
    private void addCustomLabel(GuiGraphics context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        CommandSuggestionsAccessor accessor = (CommandSuggestionsAccessor) commandSuggestions;
        int suggestorHeight = 0;
        int messagesY = 0;
        // chatSuggestor
        if (accessor.getSuggestions() != null && accessor.getPendingSuggestions() != null && accessor.getPendingSuggestions().isDone()) {
            Suggestions suggestions = accessor.getPendingSuggestions().join();
            if (!suggestions.isEmpty())
                suggestorHeight = Math.min(accessor.getSortSuggestions(suggestions).size(), accessor.getSuggestionLineLimit()) * 12 + 1;
        }
        //messages
        else if (!accessor.getCommandUsage().isEmpty()) {
            int i = accessor.getCommandUsage().size() - 1;
            messagesY = accessor.isAnchorToBottom() ? this.height - 14 - 13 - 12 * i : 72 + 12 * i;
            messagesY -= 12;
        }

        float indicatorX = EditBoxUtil.getCursorX(input);
        float indicatorY = messagesY == 0 ? this.height - 27 - suggestorHeight : messagesY;

        animationUtil.init(0, 0);
        animationUtil.calculateAnimation(indicatorX, 0);

        context.pose().translate(0.0F, 0.0F, 200.0F);
        Indicator.showIndicator(context, animationUtil.getResultX(), indicatorY);
    }
}
