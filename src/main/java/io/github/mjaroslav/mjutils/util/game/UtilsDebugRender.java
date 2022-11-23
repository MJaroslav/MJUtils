package io.github.mjaroslav.mjutils.util.game;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.github.mjaroslav.mjutils.util.UtilsFormat;
import io.github.mjaroslav.mjutils.util.UtilsFormat.ColorFormat;
import io.github.mjaroslav.mjutils.util.object.game.BlockAABBSet;
import io.github.mjaroslav.mjutils.util.object.game.Pos;
import lombok.experimental.UtilityClass;
import lombok.val;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.AxisAlignedBB;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static io.github.mjaroslav.mjutils.internal.lib.General.Debug.BlockCollisionHighlighting.*;
import static org.lwjgl.opengl.GL11.*;

@SideOnly(Side.CLIENT)
@UtilityClass
public class UtilsDebugRender {
    private int colorCounter = 0;

    public void renderBlocksCollisions(@NotNull Pos pos, int blockRadius) {
        val world = Minecraft.getMinecraft().theWorld;
        val lighting = glGetBoolean(GL_LIGHTING);
        val texture = glGetBoolean(GL_TEXTURE_2D);
        val depth = glGetBoolean(GL_DEPTH_TEST);
        val cullFace = glGetBoolean(GL_CULL_FACE);
        if (lighting) glDisable(GL_LIGHTING);
        if (texture) glDisable(GL_TEXTURE_2D);
        if (depth) glDisable(GL_DEPTH_TEST);
        if (cullFace) glDisable(GL_CULL_FACE);

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
                innerApplyCollisionColor(BlockAABBSet.isDeadZoned(box, i.getX().intValue(), i.getY().intValue(),
                    i.getZ().intValue()));
                addVertexFromAABBForLines(box);
            });
        });
        glEnd();
        glPopMatrix();

        if (lighting) glEnable(GL_LIGHTING);
        if (texture) glEnable(GL_TEXTURE_2D);
        if (depth) glEnable(GL_DEPTH_TEST);
        if (cullFace) glEnable(GL_CULL_FACE);
    }

    public void addVertexFromAABBForLines(@NotNull AxisAlignedBB box) {
        // Just indian code for all box edges
        glVertex3d(box.minX, box.minY, box.minZ);
        glVertex3d(box.maxX, box.minY, box.minZ);
        glVertex3d(box.minX, box.minY, box.minZ);
        glVertex3d(box.minX, box.minY, box.maxZ);
        glVertex3d(box.minX, box.minY, box.minZ);
        glVertex3d(box.minX, box.maxY, box.minZ);
        glVertex3d(box.maxX, box.maxY, box.maxZ);
        glVertex3d(box.maxX, box.maxY, box.minZ);
        glVertex3d(box.maxX, box.maxY, box.maxZ);
        glVertex3d(box.maxX, box.minY, box.maxZ);
        glVertex3d(box.maxX, box.maxY, box.maxZ);
        glVertex3d(box.minX, box.maxY, box.maxZ);
        glVertex3d(box.minX, box.maxY, box.minZ);
        glVertex3d(box.minX, box.maxY, box.maxZ);
        glVertex3d(box.minX, box.minY, box.maxZ);
        glVertex3d(box.minX, box.maxY, box.maxZ);
        glVertex3d(box.minX, box.minY, box.maxZ);
        glVertex3d(box.maxX, box.minY, box.maxZ);
        glVertex3d(box.minX, box.maxY, box.minZ);
        glVertex3d(box.maxX, box.maxY, box.minZ);
        glVertex3d(box.maxX, box.minY, box.minZ);
        glVertex3d(box.maxX, box.maxY, box.minZ);
        glVertex3d(box.maxX, box.minY, box.minZ);
        glVertex3d(box.maxX, box.minY, box.maxZ);
    }

    private void innerApplyCollisionColor(boolean inDeadZone) {
        val color = UtilsFormat.unpackColorIntToDoubleArray(inDeadZone ? deadColor : colorCycle[colorCounter++],
            ColorFormat.RGB, ColorFormat.RGB);
        colorCounter = colorCounter >= colorCycle.length ? 0 : colorCounter;
        glColor3d(color[0], color[1], color[2]);
    }
}
