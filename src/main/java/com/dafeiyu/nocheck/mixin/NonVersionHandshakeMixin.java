package com.dafeiyu.nocheck.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * 绕过 NeedsOfNature 的版本握手检查。
 *
 * NoN 在玩家加入时会调用 NonVersionHandshake#mismatchMessage(remoteVersion, remoteProtocolVersion)，
 * 返回非 null 就把人踢掉。没装 NoN 的客户端（比如 mineflayer）根本没这个包，
 * 于是永远被判为版本不匹配。
 *
 * 这里让方法恒定返回 null（= 匹配），握手直接放行。
 *
 * 用 targets 字符串而不是类引用，这样编译时不需要 NoN 作为依赖。
 */
@Mixin(targets = "com.nonid.network.NonVersionHandshake")
public class NonVersionHandshakeMixin {

    @Inject(method = "mismatchMessage", at = @At("HEAD"), cancellable = true)
    private void nocheck$skipMismatch(String remoteVersion, int remoteProtocolVersion, CallbackInfoReturnable<String> cir) {
        cir.setReturnValue(null);
    }
}
