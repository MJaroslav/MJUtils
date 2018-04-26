package mjaroslav.mcmods.mjutils.lib.module;

import cpw.mods.fml.client.registry.*;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;

/**
 * Base proxy for mods.
 *
 * @author MJaroslav
 */
public abstract class ProxyBase implements IInit {
    /**
     * Get proxy minecraft.
     *
     * @return Instance of {@link Minecraft}. Null on server side.
     */
    public abstract Minecraft getMinecraft();

    /**
     * Get proxy player from packet message.
     *
     * @param ctx
     *            - packet message.
     * @return EntityPlayer from packet message context or
     *         {@link Minecraft#thePlayer}.
     */
    public abstract EntityPlayer getEntityPlayer(MessageContext ctx);

    /**
     * Spawn custom particle on client side. Use {@link Minecraft#theWorld}.
     *
     * @param name
     *            - name of custom particle.
     * @param x
     *            - coordinate.
     * @param y
     *            - coordinate.
     * @param z
     *            - coordinate.
     * @param args
     *            - extra options.
     */
    public abstract void spawnParticle(String name, double x, double y, double z, Object... args);

    public static boolean isClient() {
        return FMLCommonHandler.instance().getEffectiveSide().isClient();
    }

    public static void rendererTileEntity(Class<? extends TileEntity> tileClass, TileEntitySpecialRenderer renderer) {
        ClientRegistry.bindTileEntitySpecialRenderer(tileClass, renderer);
    }

    public static void rendererItem(Item item, IItemRenderer renderer) {
        MinecraftForgeClient.registerItemRenderer(item, renderer);
    }

    public static void rendererBlock(ISimpleBlockRenderingHandler renderer) {
        RenderingRegistry.registerBlockHandler(renderer);
    }

    public static void rendererBlock(int id, ISimpleBlockRenderingHandler renderer) {
        RenderingRegistry.registerBlockHandler(id, renderer);
    }
}
