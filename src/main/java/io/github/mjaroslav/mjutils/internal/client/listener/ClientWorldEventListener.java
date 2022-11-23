package io.github.mjaroslav.mjutils.internal.client.listener;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import io.github.mjaroslav.mjutils.event.BlockDestroyedInCreativeEvent;
import io.github.mjaroslav.mjutils.internal.lib.General.Creative.BlockBreaking;
import io.github.mjaroslav.mjutils.internal.lib.General.Debug.BlockCollisionHighlighting;
import io.github.mjaroslav.mjutils.util.game.UtilsDebugRender;
import io.github.mjaroslav.mjutils.util.game.UtilsItemStack;
import io.github.mjaroslav.mjutils.util.object.game.ItemStackSet;
import io.github.mjaroslav.mjutils.util.object.game.Pos;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.val;
import net.minecraft.client.Minecraft;
import net.minecraft.item.*;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import org.jetbrains.annotations.NotNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientWorldEventListener {
    public static final ClientWorldEventListener INSTANCE = new ClientWorldEventListener();

    public final ItemStackSet extraDisabledForCreativeDestroying = new ItemStackSet(UtilsItemStack.META
        | UtilsItemStack.ITEM);

    @SubscribeEvent
    public void onRenderWorldLastEvent(@NotNull RenderWorldLastEvent event) {
        if (!BlockCollisionHighlighting.enable.isEnabled()) return;
        val mov = Minecraft.getMinecraft().objectMouseOver;
        val world = Minecraft.getMinecraft().theWorld;
        val player = Minecraft.getMinecraft().thePlayer;
        if (mov == null || world == null || player == null || (BlockCollisionHighlighting.enable.isShift()
            && !player.isSneaking())) return;
        val pos = BlockCollisionHighlighting.enable.isCursor() ? new Pos(mov.blockX, mov.blockY, mov.blockZ) :
            new Pos(player.posX, player.posY, player.posZ);
        UtilsDebugRender.renderBlocksCollisions(pos, BlockCollisionHighlighting.range - 1);
    }

    @SubscribeEvent
    public void onBlockDestroyedInCreativeEvent(@NotNull BlockDestroyedInCreativeEvent event) {
        boolean cancel = false;
        val item = event.heldItem.getItem();
        if (BlockBreaking.swords && item instanceof ItemSword) cancel = true;
        if (BlockBreaking.tools && item instanceof ItemTool) cancel = true;
        if (BlockBreaking.axes && item instanceof ItemAxe) cancel = true;
        if (BlockBreaking.pickaxes && item instanceof ItemPickaxe) cancel = true;
        if (BlockBreaking.shovels && item instanceof ItemSpade) cancel = true;
        if (BlockBreaking.extraItems && extraDisabledForCreativeDestroying.contains(event.heldItem)) cancel = true;
        event.setCanceled(cancel);
    }
}
