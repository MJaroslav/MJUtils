package io.github.mjaroslav.mjutils.modular;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import io.github.mjaroslav.mjutils.lib.MJUtilsInfo;
import io.github.mjaroslav.sharedjava.tuple.Triplet;
import lombok.Getter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

@Getter
public abstract class NetworkModule {
    protected final @NotNull SimpleNetworkWrapper wrapper;

    public NetworkModule(@NotNull String channelName) {
        wrapper = NetworkRegistry.INSTANCE.newSimpleChannel(channelName);
    }

    public void listen(@NotNull FMLInitializationEvent event) {
        registerMessages();
    }

    public abstract void registerMessages();

    public void sendTo(@NotNull IMessage message, @NotNull EntityPlayer player) {
        if (player instanceof EntityPlayerMP)
            wrapper.sendTo(message, (EntityPlayerMP) player);
        else
            MJUtilsInfo.LOG_MODULES.error("Something is trying to send packet to non MP EntityPlayer",
                new IllegalArgumentException());
    }

    public void sendToAllAround(@NotNull IMessage message, @NotNull EntityPlayer player, double radius) {
        wrapper.sendToAllAround(message,
            new TargetPoint(player.worldObj.provider.dimensionId, player.posX, player.posY, player.posZ, radius));
    }

    public void sendToAllAround(@NotNull IMessage message,
                                @NotNull World world, @NotNull Triplet<? extends Number, ? extends Number,
        ? extends Number> pos, double radius) {
        wrapper.sendToAllAround(message, new TargetPoint(world.provider.dimensionId, pos.getX().doubleValue(),
            pos.getY().doubleValue(), pos.getZ().doubleValue(), radius));
    }

    public void sendToAllAround(@NotNull IMessage message, @NotNull World world, double x, double y, double z,
                                double radius) {
        wrapper.sendToAllAround(message, new TargetPoint(world.provider.dimensionId, x, y, z, radius));
    }

    public void sendToAllAround(@NotNull IMessage message, int dimId,
                                @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos,
                                double radius) {
        wrapper.sendToAllAround(message, new TargetPoint(dimId, pos.getX().doubleValue(), pos.getY().doubleValue(),
            pos.getZ().doubleValue(), radius));
    }

    public void sendToAllAround(@NotNull IMessage message, int dimId, double x, double y, double z, double radius) {
        wrapper.sendToAllAround(message, new TargetPoint(dimId, x, y, z, radius));
    }

    public void sendToAll(@NotNull IMessage message) {
        wrapper.sendToAll(message);
    }

    public void sendToServer(@NotNull IMessage message) {
        wrapper.sendToServer(message);
    }
}
