package io.github.mjaroslav.mjutils.mod.client;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import io.github.mjaroslav.mjutils.mod.client.listener.ClientWorldEventListener;
import io.github.mjaroslav.mjutils.mod.common.CommonProxy;
import net.minecraftforge.common.MinecraftForge;
import org.jetbrains.annotations.NotNull;

public class ClientProxy extends CommonProxy {
    public void listen(@NotNull FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(ClientWorldEventListener.INSTANCE);
    }
}
