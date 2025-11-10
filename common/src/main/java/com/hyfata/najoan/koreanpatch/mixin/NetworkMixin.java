package com.hyfata.najoan.koreanpatch.mixin;

import com.hyfata.najoan.koreanpatch.client.Constants;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundCommandSuggestionPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(net.minecraft.network.Connection.class)
public class NetworkMixin {
    @Inject(method="send(Lnet/minecraft/network/protocol/Packet;)V", at=@At("HEAD"))
    private void onSend(Packet<?> packet, CallbackInfo ci) {
        if (packet instanceof ServerboundCommandSuggestionPacket suggestionPacket) {
            String commandString = suggestionPacket.getCommand();
            int transactionId = suggestionPacket.getId(); // 요청 ID

            Constants.LOG.info(
                    "[TAB_SEND] ID: {}, Command: '{}'",
                    transactionId,
                    commandString
            );
        }
    }
}
