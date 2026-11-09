package com.dafeiyu.nocheck.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * 诊断版：记录 mismatchMessage 是否真的被调用，并试图拦截。
 */
@Mixin(targets = "com.nonid.network.NonVersionHandshake")
public class NonVersionHandshakeMixin {

    @Inject(method = "mismatchMessage", at = @At("HEAD"), cancellable = true, require = 0)
    private static void nocheck$skipMismatch(String remoteVersion, int remoteProtocolVersion, CallbackInfoReturnable<String> cir) {
        System.out.println("[nocheck][HANDSHAKE] mismatchMessage 被调用 remote=" + remoteVersion + " proto=" + remoteProtocolVersion + " → 返回 null");
        cir.setReturnValue(null);
    }
}
