package com.dafeiyu.nocheck;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.fabricmc.fabric.api.event.registry.RegistryAttributeHolder;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 把服务端所有注册表标成 OPTIONAL。
 *
 * Fabric API 在玩家加入的配置阶段会检查客户端注册表；只要有任何一个注册表是
 * REQUIRED 且客户端没有对应模组，就直接断开，提示 "This server requires
 * Fabric Loader and Fabric API installed on your client!"。
 *
 * 这个 mod 在服务端启动时把所有注册表都加上 OPTIONAL 标记，
 * RegistrySyncManager.areAllRegistriesOptional() 就会返回 true，检查直接放行。
 *
 * 副作用：客户端注册表与服务器不一致时不再报错，可能出现 ID 错位。
 * 只建议在单机开服给纯协议客户端（比如 mineflayer）留门时使用。
 */
public class NoCheckMod implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("nocheck");

    @Override
    public void onInitialize() {
        ServerLifecycleEvents.SERVER_STARTING.register(server -> {
            int marked = 0;
            for (Registry<?> registry : Registries.REGISTRIES) {
                try {
                    RegistryAttributeHolder.get(registry).addAttribute(RegistryAttribute.OPTIONAL);
                    marked++;
                } catch (Throwable ignored) {
                }
            }
            LOGGER.info("[nocheck] {} 个注册表已标记为可选，注册表检查已放行", marked);
        });
    }
}
