package io.github.mjaroslav.mjutils.client;

import lombok.experimental.UtilityClass;
import lombok.val;
import net.minecraft.client.Minecraft;
import net.minecraft.util.AxisAlignedBB;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

import java.util.HashSet;
import java.util.Set;

import static org.lwjgl.opengl.GL11.*;

@UtilityClass
public class GLHelper {
    public @Nullable Set<Integer> glDisable(int... names) {
        if (names == null || names.length == 0) return null;
        val result = new HashSet<Integer>();
        var temp = false;
        for (val name : names) {
            temp = GL11.glGetBoolean(name);
            if (temp) {
                result.add(name);
                GL11.glDisable(name);
            }
        }
        return result;
    }

    public void glDisableRestore(@Nullable Set<Integer> names) {
        if (names != null) names.forEach(GL11::glEnable);
    }

    public @Nullable Set<Integer> glEnable(int... names) {
        if (names == null || names.length == 0) return null;
        val result = new HashSet<Integer>();
        var temp = false;
        for (val name : names) {
            temp = GL11.glGetBoolean(name);
            if (!temp) {
                result.add(name);
                GL11.glEnable(name);
            }
        }
        return result;
    }

    public void glEnableRestore(@Nullable Set<Integer> names) {
        if (names != null) names.forEach(GL11::glDisable);
    }

    public void addVertexesFromAABB(@NotNull AxisAlignedBB box) {
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

    public void subtractCameraTranslation(float partial) {
        val player = Minecraft.getMinecraft().thePlayer;
        glTranslated(-(player.lastTickPosX + (player.posX - player.lastTickPosX) * partial),
            -(player.lastTickPosY + (player.posY - player.lastTickPosY) * partial),
            -(player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partial));
    }
}
