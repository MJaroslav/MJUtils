package io.github.mjaroslav.mjutils.mod.client.listener;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import io.github.mjaroslav.mjutils.mod.lib.General.Debug.BlockCollisionHighlighting;
import io.github.mjaroslav.mjutils.object.game.world.Pos;
import io.github.mjaroslav.mjutils.util.game.client.UtilsDebugRender;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.val;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import org.jetbrains.annotations.NotNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientWorldEventListener {
    public static final ClientWorldEventListener INSTANCE = new ClientWorldEventListener();

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
}
