package io.github.mjaroslav.mjutils.client.model.obj;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.github.mjaroslav.sharedjava.function.LazySupplier;
import lombok.Getter;
import lombok.val;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.model.obj.TextureCoordinate;
import net.minecraftforge.client.model.obj.Vertex;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
public class IconScaledFace {
    private final @NotNull IconScaledGroupObject parent;
    private final @NotNull Vertex @NotNull [] vertices;
    private final @NotNull Vertex @Nullable [] vertexNormals; // Not implemented by Tessellator
    private final @NotNull TextureCoordinate @Nullable [] textureCoordinates;
    private final @NotNull LazySupplier<Vertex> faceNormal;

    public IconScaledFace(@NotNull IconScaledGroupObject parent, @NotNull Vertex @NotNull [] vertices,
                          @NotNull Vertex @Nullable [] vertexNormals,
                          @NotNull TextureCoordinate @Nullable [] textureCoordinates) {
        this.parent = parent;
        this.vertices = vertices;
        this.vertexNormals = vertexNormals;
        this.textureCoordinates = textureCoordinates;
        faceNormal = new LazySupplier<>(() -> {
            val v1 = Vec3.createVectorHelper(vertices[1].x - vertices[0].x, vertices[1].y - vertices[0].y,
                vertices[1].z - vertices[0].z);
            val v2 = Vec3.createVectorHelper(vertices[2].x - vertices[0].x, vertices[2].y - vertices[0].y,
                vertices[2].z - vertices[0].z);
            val normalVector = v1.crossProduct(v2).normalize();
            return new Vertex((float) normalVector.xCoord, (float) normalVector.yCoord, (float) normalVector.zCoord);
        });
    }

    @SideOnly(Side.CLIENT)
    public void addFaceForRender(@NotNull Tessellator tessellator) {
        addFaceForRender(tessellator, 0.0005F);
    }

    @SideOnly(Side.CLIENT)
    public void addFaceForRender(@NotNull Tessellator tessellator, float textureOffset) {
        val faceNormal = this.faceNormal.orElseThrow();
        tessellator.setNormal(faceNormal.x, faceNormal.y, faceNormal.z);
        var averageU = 0F;
        var averageV = 0F;
        if ((textureCoordinates != null) && (textureCoordinates.length > 0)) {
            for (val textureCoordinate : textureCoordinates) {
                averageU += textureCoordinate.u;
                averageV += textureCoordinate.v;
            }
            averageU = averageU / textureCoordinates.length;
            averageV = averageV / textureCoordinates.length;
        }
        var offsetU = 0F;
        var offsetV = 0F;
        for (var i = 0; i < vertices.length; ++i)
            if ((textureCoordinates != null) && (textureCoordinates.length > 0)) {
                offsetU = textureOffset;
                offsetV = textureOffset;
                if (textureCoordinates[i].u > averageU) offsetU = -offsetU;
                if (textureCoordinates[i].v > averageV) offsetV = -offsetV;
                tessellator.addVertexWithUV(vertices[i].x, vertices[i].y, vertices[i].z, textureCoordinates[i].u
                    + offsetU, textureCoordinates[i].v + offsetV);
            } else tessellator.addVertex(vertices[i].x, vertices[i].y, vertices[i].z);
    }

    @SideOnly(Side.CLIENT)
    public void addFaceForRender(@NotNull IIcon icon, @NotNull Tessellator tessellator) {
        addFaceForRender(icon, tessellator, 0.0005F);
    }

    @SideOnly(Side.CLIENT)
    public void addFaceForRender(@NotNull IIcon icon, @NotNull Tessellator tessellator, float textureOffset) {
        val faceNormal = this.faceNormal.orElseThrow();
        tessellator.setNormal(faceNormal.x, faceNormal.y, faceNormal.z);
        var averageU = 0F;
        var averageV = 0F;
        if ((textureCoordinates != null) && (textureCoordinates.length > 0)) {
            for (var textureCoordinate : textureCoordinates) {
                averageU += icon.getInterpolatedU(16 * textureCoordinate.u);
                averageV += icon.getInterpolatedV(16 * textureCoordinate.v);
            }
            averageU = averageU / textureCoordinates.length;
            averageV = averageV / textureCoordinates.length;
        }
        var offsetU = 0F;
        var offsetV = 0F;
        for (var i = 0; i < vertices.length; ++i)
            if ((textureCoordinates != null) && (textureCoordinates.length > 0)) {
                offsetU = textureOffset;
                offsetV = textureOffset;
                if (icon.getInterpolatedU(16 * textureCoordinates[i].u) > averageU) offsetU = -offsetU;
                if (icon.getInterpolatedV(16 * textureCoordinates[i].v) > averageV) offsetV = -offsetV;
                tessellator.addVertexWithUV(vertices[i].x, vertices[i].y, vertices[i].z,
                    icon.getInterpolatedU(16 * textureCoordinates[i].u) + offsetU,
                    icon.getInterpolatedV(16 * textureCoordinates[i].v) + offsetV);
            } else tessellator.addVertex(vertices[i].x, vertices[i].y, vertices[i].z);
    }
}
