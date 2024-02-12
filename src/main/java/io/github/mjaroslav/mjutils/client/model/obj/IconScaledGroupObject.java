package io.github.mjaroslav.mjutils.client.model.obj;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import lombok.Getter;
import lombok.val;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Getter
public class IconScaledGroupObject {
    private final @NotNull IconScaledWavefrontObject owner;
    private final @NotNull String name;
    private final List<IconScaledFace> faces = new ArrayList<>();

    public IconScaledGroupObject(@NotNull IconScaledWavefrontObject owner) {
        this(owner, "Default");
    }

    public IconScaledGroupObject(@NotNull IconScaledWavefrontObject owner, @NotNull String name) {
        this.owner = owner;
        this.name = name;
    }

    @SideOnly(Side.CLIENT)
    public void render() {
        if (!faces.isEmpty()) {
            val tessellator = Tessellator.instance;
            tessellator.startDrawing(owner.getGlDrawingMode());
            render(tessellator);
            tessellator.draw();
        }
    }

    @SideOnly(Side.CLIENT)
    public void render(@NotNull Tessellator tessellator) {
        if (!faces.isEmpty()) for (val face : faces) face.addFaceForRender(tessellator);
    }

    @SideOnly(Side.CLIENT)
    public void render(@NotNull IIcon icon, @NotNull Tessellator tessellator) {
        if (!faces.isEmpty()) for (val face : faces) face.addFaceForRender(icon, tessellator);
    }
}
