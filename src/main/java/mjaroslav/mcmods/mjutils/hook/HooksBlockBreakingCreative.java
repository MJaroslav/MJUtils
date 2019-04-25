package mjaroslav.mcmods.mjutils.hook;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mjaroslav.mcmods.mjutils.gloomyfolken.hooklib.asm.Hook;
import mjaroslav.mcmods.mjutils.gloomyfolken.hooklib.asm.ReturnCondition;
import mjaroslav.mcmods.mjutils.lib.util.UtilsInteractions;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S23PacketBlockChange;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;

@SuppressWarnings("unused")
public class HooksBlockBreakingCreative {
    public static final String DISABLE_ID = "hooks_block_breaking_creative";

    // TODO: Поискать альтернативу, которая предусмотрена mc/forge.
    @SideOnly(Side.CLIENT)
    @Hook(returnCondition = ReturnCondition.ON_TRUE)
    public static boolean onPlayerDestroyBlock(PlayerControllerMP instance, int x, int y, int z, int i) {
        return instance.isInCreativeMode() &&
                UtilsInteractions.blockBreakingIsDisabledInCreative(Minecraft.getMinecraft().thePlayer.getHeldItem());
    }

    // TODO: Выбрать между hook'ом и BreakEvent'ом.
    @Hook(returnCondition = ReturnCondition.ALWAYS)
    public static BlockEvent.BreakEvent onBlockBreakEvent(ForgeHooks instance, World world, WorldSettings.GameType gameType,
                                                          EntityPlayerMP entityPlayer, int x, int y, int z) {
        // TODO: Попробовать реализовать через hook локальной переменной.
        boolean preCancelEvent = false;
        if (gameType.isAdventure() && !entityPlayer.isCurrentToolAdventureModeExempt(x, y, z))
            preCancelEvent = true;
        // ORIGINAL
//        else if (gameType.isCreative() && entityPlayer.getHeldItem() != null &&
//                entityPlayer.getHeldItem().getItem() instanceof ItemSword)
//            preCancelEvent = true;
        // HOOK START
        if (gameType.isCreative() && entityPlayer.getHeldItem() != null &&
                UtilsInteractions.blockBreakingIsDisabledInCreative(entityPlayer.getHeldItem()))
            preCancelEvent = true;
        // HOOK END
        if (world.getTileEntity(x, y, z) == null) {
            S23PacketBlockChange packet = new S23PacketBlockChange(x, y, z, world);
            packet.field_148883_d = Blocks.air;
            packet.field_148884_e = 0;
            entityPlayer.playerNetServerHandler.sendPacket(packet);
        }
        Block block = world.getBlock(x, y, z);
        int blockMetadata = world.getBlockMetadata(x, y, z);
        BlockEvent.BreakEvent event = new BlockEvent.BreakEvent(x, y, z, world, block, blockMetadata, entityPlayer);
        event.setCanceled(preCancelEvent);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.isCanceled()) {
            entityPlayer.playerNetServerHandler.sendPacket(new S23PacketBlockChange(x, y, z, world));
            TileEntity tileentity = world.getTileEntity(x, y, z);
            if (tileentity != null) {
                Packet pkt = tileentity.getDescriptionPacket();
                if (pkt != null)
                    entityPlayer.playerNetServerHandler.sendPacket(pkt);
            }
        }
        return event;
    }
}
