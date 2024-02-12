package io.github.mjaroslav.mjutils.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.github.mjaroslav.mjutils.util.BlockAABBSet;
import io.github.mjaroslav.mjutils.util.game.UtilsPosBlock;
import io.github.mjaroslav.mjutils.util.game.UtilsPosWorld;
import io.github.mjaroslav.mjutils.util.object.game.Pos;
import io.github.mjaroslav.sharedjava.format.ColorFormat;
import io.github.mjaroslav.sharedjava.format.Colors;
import lombok.experimental.UtilityClass;
import lombok.val;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.AxisAlignedBB;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static io.github.mjaroslav.mjutils.lib.General.Debug.BlockCollisionHighlighting.*;
import static org.lwjgl.opengl.GL11.*;

@SideOnly(Side.CLIENT)
@UtilityClass
public class DebugRenderer {
    private int colorCounter = 0;

    public void renderBlocksCollisions(@NotNull Pos pos, int blockRadius) {
        val world = Minecraft.getMinecraft().theWorld;
        val cache = GLHelper.glDisable(GL_LIGHTING, GL_TEXTURE_2D, GL_DEPTH_TEST, GL_CULL_FACE);

        glPushMatrix();
        glTranslated(-RenderManager.renderPosX, -RenderManager.renderPosY, -RenderManager.renderPosZ);
        glBegin(GL_LINES);
        val min = pos.sub(blockRadius);
        val max = pos.add(blockRadius + 1);
        val mask = min.toAABB(max);
        val list = new ArrayList<AxisAlignedBB>();
        Pos.forEachInBox(min, max, i -> {
            list.clear();
            colorCounter = 0;
            UtilsPosBlock.addCollisionBoxesToList(UtilsPosWorld.getBlock(world, i), world, i, mask, list, null);
            list.forEach(box -> {
                cycleDebugLinesColor(BlockAABBSet.isDeadZoned(box, i.getX().intValue(), i.getY().intValue(),
                    i.getZ().intValue()));
                GLHelper.addVertexesFromAABB(box);
            });
        });
        glEnd();
        glPopMatrix();

        GLHelper.glDisableRestore(cache);
    }

    public void cycleDebugLinesColor(boolean inDeadZone) {
        val color = Colors.unpackColorIntToDoubleArray(inDeadZone ? deadColor : colorCycle[colorCounter++],
            ColorFormat.RGB, ColorFormat.RGB);
        colorCounter = colorCounter >= colorCycle.length ? 0 : colorCounter;
        glColor3d(color[0], color[1], color[2]);
    }
}
