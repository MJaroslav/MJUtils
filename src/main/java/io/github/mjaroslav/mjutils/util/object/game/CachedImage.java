package io.github.mjaroslav.mjutils.util.object.game;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.val;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

@Getter
@ToString
@EqualsAndHashCode
public class CachedImage {
    protected ResourceLocation location;
    protected Dimension dimensions;
    protected DynamicTexture texture;
    protected String modId;

    protected boolean loaded;

    public CachedImage(@NotNull ResourcePath path) {
        try {
            var image = ImageIO.read(path.stream());
            if (image != null) {
                dimensions = new Dimension(image.getWidth(), image.getHeight());
                this.texture = new DynamicTexture(image);
                val location = path.makeUnique();
                this.location = Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation(path.getPath(),
                    this.texture);
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
