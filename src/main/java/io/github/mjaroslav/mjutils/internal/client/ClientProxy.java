package io.github.mjaroslav.mjutils.internal.client;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import io.github.mjaroslav.mjutils.internal.client.listener.ClientWorldEventListener;
import io.github.mjaroslav.mjutils.internal.client.listener.GuiReplacerEventListener;
import io.github.mjaroslav.mjutils.internal.common.CommonProxy;
import net.minecraftforge.common.MinecraftForge;
import org.jetbrains.annotations.NotNull;

public class ClientProxy extends CommonProxy {
    public void listen(@NotNull FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(ClientWorldEventListener.INSTANCE);
        MinecraftForge.EVENT_BUS.register(GuiReplacerEventListener.INSTANCE);
    }
}
