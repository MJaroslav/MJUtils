package io.github.mjaroslav.mjutils.internal.client.listener;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import io.github.mjaroslav.mjutils.client.DebugRenderer;
import io.github.mjaroslav.mjutils.event.player.PlayerDestroyBlockInCreativeEvent;
import io.github.mjaroslav.mjutils.item.ItemStackSet;
import io.github.mjaroslav.mjutils.item.Stacks;
import io.github.mjaroslav.mjutils.lib.General.Creative.BlockBreaking;
import io.github.mjaroslav.mjutils.lib.General.Debug.BlockCollisionHighlighting;
import io.github.mjaroslav.mjutils.util.Pos;
import io.github.mjaroslav.mjutils.util.Pos.Mutable;
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

    public final ItemStackSet extraDisabledForCreativeDestroying = new ItemStackSet(Stacks.META_WILDCARD
        | Stacks.ITEM_NULLABLE);

    private final Mutable temp = Pos.mutable();

    @SubscribeEvent
    public void onRenderWorldLastEvent(@NotNull RenderWorldLastEvent event) {
        if (!BlockCollisionHighlighting.enable.isEnabled()) return;
        val mov = Minecraft.getMinecraft().objectMouseOver;
        val world = Minecraft.getMinecraft().theWorld;
        val player = Minecraft.getMinecraft().thePlayer;
        if (mov == null || world == null || player == null || (BlockCollisionHighlighting.enable.isShift()
            && !player.isSneaking())) return;
        val flag = BlockCollisionHighlighting.enable.isCursor();
        DebugRenderer.renderBlocksCollisions(temp.set(flag ? mov.blockX : player.posX, flag ? mov.blockX : player.posX,
            flag ? mov.blockX : player.posX), BlockCollisionHighlighting.range - 1);
    }

    @SubscribeEvent
    public void onBlockDestroyedInCreativeEvent(@NotNull PlayerDestroyBlockInCreativeEvent event) {
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
