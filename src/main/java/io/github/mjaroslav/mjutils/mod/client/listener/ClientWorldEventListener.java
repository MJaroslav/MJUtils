package io.github.mjaroslav.mjutils.mod.client.listener;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import io.github.mjaroslav.mjutils.mod.lib.General.Debug.BlockCollisionHighlighting;
import io.github.mjaroslav.mjutils.object.game.world.BlockAABBSet;
import io.github.mjaroslav.mjutils.util.UtilsFormat;
import io.github.mjaroslav.mjutils.util.UtilsFormat.ColorFormat;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.val;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientWorldEventListener {
    public static final ClientWorldEventListener INSTANCE = new ClientWorldEventListener();

    @SubscribeEvent
    public void onRenderWorldLastEvent(@NotNull RenderWorldLastEvent event) {
        if (BlockCollisionHighlighting.enable == 0) return;
        val mov = Minecraft.getMinecraft().objectMouseOver;
        val world = Minecraft.getMinecraft().theWorld;
        val player = Minecraft.getMinecraft().thePlayer;
        if (mov == null || world == null || player == null || (BlockCollisionHighlighting.enable == 2
            && !player.isSneaking())) return;
        final int x = mov.blockX, y = mov.blockY, z = mov.blockZ;
        val block = world.getBlock(x, y, z);
        if (block == null) return;
        val boxes = new ArrayList<AxisAlignedBB>();
        block.addCollisionBoxesToList(world, x, y, z, AxisAlignedBB.getBoundingBox(x - 1, y - 1, z - 1, x + 2, y + 3,
            z + 2), boxes, player);
        if (boxes.isEmpty()) return;
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
        var colorCounter = 0;
        double[] color;
        for (var box : boxes) {
            if (BlockAABBSet.isDeadZoned(box, x, y, z))
                color = UtilsFormat.unpackColorIntToDoubleArray(BlockCollisionHighlighting.deadColor, ColorFormat.RGB,
                    ColorFormat.RGB);
            else {
                color = UtilsFormat.unpackColorIntToDoubleArray(BlockCollisionHighlighting.colorCycle[colorCounter],
                    ColorFormat.RGB, ColorFormat.RGB);
                colorCounter++;
                if (colorCounter > BlockCollisionHighlighting.colorCycle.length) colorCounter = 0;
            }
            glColor3d(color[0], color[1], color[2]);
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
        glEnd();
        glPopMatrix();
        if (lighting) glEnable(GL_LIGHTING);
        if (texture) glEnable(GL_TEXTURE_2D);
        if (depth) glEnable(GL_DEPTH_TEST);
        if (cullFace) glEnable(GL_CULL_FACE);
    }
}
