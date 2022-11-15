package io.github.mjaroslav.mjutils.object.game.client;

import io.github.mjaroslav.mjutils.util.game.UtilsMods;
import io.github.mjaroslav.mjutils.util.io.ResourcePath;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

@Getter
@ToString
@EqualsAndHashCode
public class CachedImage {
    protected ResourceLocation location;
    protected Dimension dimensions;
    protected DynamicTexture texture;
    protected String modId;

    protected boolean loaded;

    public CachedImage(ResourcePath path) {
        this(path.getPath(), path.getPath(), path.getNamespace());
    }

    public CachedImage(String location, String texture, String modSourceId) {
        try {
            BufferedImage image;
            InputStream imageStream = UtilsMods.getResourceFromMod(UtilsMods.getContainer(modSourceId), texture, false);
            image = ImageIO.read(imageStream);
            if (image != null) {
                dimensions = new Dimension(image.getWidth(), image.getHeight());
                this.texture = new DynamicTexture(image);
                this.location = Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation(location, this.texture);
                loaded = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
            loaded = false;
        }
    }

    public void delete() {
        loaded = false;
        texture.deleteGlTexture();
    }
}