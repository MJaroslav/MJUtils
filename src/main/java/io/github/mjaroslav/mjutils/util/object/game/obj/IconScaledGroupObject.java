package io.github.mjaroslav.mjutils.util.object.game.obj;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import lombok.val;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class IconScaledGroupObject {
    public String name;
    public ArrayList<IconScaledFace> faces = new ArrayList<>();
    public int glDrawingMode;

    public IconScaledGroupObject() {
        this("");
    }

    public IconScaledGroupObject(@NotNull String name) {
        this(name, -1);
    }

    public IconScaledGroupObject(@NotNull String name, int glDrawingMode) {
        this.name = name;
        this.glDrawingMode = glDrawingMode;
    }

    @SideOnly(Side.CLIENT)
    public void render() {
        if (faces.size() > 0) {
            val tessellator = Tessellator.instance;
            tessellator.startDrawing(glDrawingMode);
            render(tessellator);
            tessellator.draw();
        }
    }

    @SideOnly(Side.CLIENT)
    public void render(@NotNull Tessellator tessellator) {
        if (faces.size() > 0) for (var face : faces) face.addFaceForRender(tessellator);
    }

    @SideOnly(Side.CLIENT)
    public void render(@NotNull IIcon icon, @NotNull Tessellator tessellator) {
        if (faces.size() > 0) for (var face : faces) face.addFaceForRender(icon, tessellator);
    }
}
