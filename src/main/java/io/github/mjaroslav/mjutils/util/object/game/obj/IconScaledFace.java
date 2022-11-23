package io.github.mjaroslav.mjutils.util.object.game.obj;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import lombok.val;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.model.obj.TextureCoordinate;
import net.minecraftforge.client.model.obj.Vertex;
import org.jetbrains.annotations.NotNull;

public class IconScaledFace {
    public Vertex[] vertices;
    public Vertex[] vertexNormals;
    public Vertex faceNormal;
    public TextureCoordinate[] textureCoordinates;

    @SideOnly(Side.CLIENT)
    public void addFaceForRender(@NotNull Tessellator tessellator) {
        addFaceForRender(tessellator, 0.0005F);
    }

    @SideOnly(Side.CLIENT)
    public void addFaceForRender(@NotNull Tessellator tessellator, float textureOffset) {
        if (faceNormal == null) faceNormal = calculateFaceNormal();
        tessellator.setNormal(faceNormal.x, faceNormal.y, faceNormal.z);
        float averageU = 0F;
        float averageV = 0F;
        if ((textureCoordinates != null) && (textureCoordinates.length > 0)) {
            for (var textureCoordinate : textureCoordinates) {
                averageU += textureCoordinate.u;
                averageV += textureCoordinate.v;
            }
            averageU = averageU / textureCoordinates.length;
            averageV = averageV / textureCoordinates.length;
        }
        float offsetU, offsetV;
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
        if (faceNormal == null) faceNormal = calculateFaceNormal();
        tessellator.setNormal(faceNormal.x, faceNormal.y, faceNormal.z);
        float averageU = 0F;
        float averageV = 0F;
        if ((textureCoordinates != null) && (textureCoordinates.length > 0)) {
            for (var textureCoordinate : textureCoordinates) {
                averageU += icon.getInterpolatedU(16 * textureCoordinate.u);
                averageV += icon.getInterpolatedV(16 * textureCoordinate.v);
            }
            averageU = averageU / textureCoordinates.length;
            averageV = averageV / textureCoordinates.length;
        }
        float offsetU, offsetV;
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

    public Vertex calculateFaceNormal() {
        val v1 = Vec3.createVectorHelper(vertices[1].x - vertices[0].x, vertices[1].y - vertices[0].y,
            vertices[1].z - vertices[0].z);
        val v2 = Vec3.createVectorHelper(vertices[2].x - vertices[0].x, vertices[2].y - vertices[0].y,
            vertices[2].z - vertices[0].z);
        val normalVector = v1.crossProduct(v2).normalize();
        return new Vertex((float) normalVector.xCoord, (float) normalVector.yCoord, (float) normalVector.zCoord);
    }
}
