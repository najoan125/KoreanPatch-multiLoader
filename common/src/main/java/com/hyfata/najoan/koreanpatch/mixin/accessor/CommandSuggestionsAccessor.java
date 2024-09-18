package com.hyfata.najoan.koreanpatch.mixin.accessor;

import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.Suggestions;
import net.minecraft.client.gui.components.CommandSuggestions;
import net.minecraft.util.FormattedCharSequence;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Mixin(CommandSuggestions.class)
public interface CommandSuggestionsAccessor {
    @Accessor("suggestionLineLimit")
    int getSuggestionLineLimit();

    @Accessor("pendingSuggestions")
    CompletableFuture<Suggestions> getPendingSuggestions();

    @Accessor("suggestions")
    CommandSuggestions.SuggestionsList getSuggestions();

    @Accessor("commandUsage")
    List<FormattedCharSequence> getCommandUsage();

    @Accessor("anchorToBottom")
    boolean isAnchorToBottom();

    @Invoker("sortSuggestions")
    List<Suggestion> getSortSuggestions(Suggestions suggestions);
}