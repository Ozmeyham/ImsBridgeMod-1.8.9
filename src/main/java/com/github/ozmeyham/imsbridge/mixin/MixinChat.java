package com.github.ozmeyham.imsbridge.mixin;

import com.github.ozmeyham.imsbridge.IMSBridge;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiScreen.class)
public abstract class MixinChat {

    @Shadow public abstract void sendChatMessage(String msg);

    @Inject(method = "sendChatMessage(Ljava/lang/String;Z)V", at = @At("HEAD"), cancellable = true)
    public void cbcToggle(String text, boolean addToChat, CallbackInfo ci) {
        if (IMSBridge.combinedBridgeChatEnabled && !text.startsWith("/")) {
            sendChatMessage("/cbc " + text);
            ci.cancel();
        }
    }
}
