package io.github.mjaroslav.mjutils.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayerMP;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractPacket<REQ extends IMessage> implements IMessage, IMessageHandler<REQ, REQ> {
    @Override
    public final @Nullable REQ onMessage(@NotNull REQ message, @NotNull MessageContext ctx) {
        if (ctx.side == Side.SERVER)
            handleServerSide(message, ctx.getServerHandler().playerEntity);
        else
            handleClientSide(message, Minecraft.getMinecraft().thePlayer);
        return null;
    }

    @SideOnly(Side.CLIENT)
    public void handleClientSide(@NotNull REQ message, @NotNull EntityClientPlayerMP player) {
    }

    public void handleServerSide(@NotNull REQ message, @NotNull EntityPlayerMP player) {
    }

    @Override
    public abstract void fromBytes(@NotNull ByteBuf buf);

    @Override
    public abstract void toBytes(@NotNull ByteBuf buf);
}
