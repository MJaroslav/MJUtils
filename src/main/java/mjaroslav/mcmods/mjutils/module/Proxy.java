package mjaroslav.mcmods.mjutils.module;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;

public abstract class Proxy implements Initializator {
    /**
     * Get proxy minecraft.
     *
     * @return Instance of {@link Minecraft}. Null on server side.
     */
    @SideOnly(Side.CLIENT)
    public abstract Minecraft getMinecraft();

    /**
     * Get proxy player from packet message.
     *
     * @param ctx - packet message.
     * @return EntityPlayer from packet message context or
     * {@link Minecraft#thePlayer}.
     */
    public abstract EntityPlayer getEntityPlayer(MessageContext ctx);

    /**
     * Spawn custom particle on client side. Use {@link Minecraft#theWorld}.
     *
     * @param name - name of custom particle.
     * @param x    - coordinate.
     * @param y    - coordinate.
     * @param z    - coordinate.
     * @param args - extra options.
     */
    public void spawnParticle(String name, double x, double y, double z, Object... args) {
    }

    public static boolean isClient() {
        return FMLCommonHandler.instance().getSide().isClient();
    }

    @SideOnly(Side.CLIENT)
    public static void rendererTileEntity(Class<? extends TileEntity> tileClass, TileEntitySpecialRenderer renderer) {
        ClientRegistry.bindTileEntitySpecialRenderer(tileClass, renderer);
    }

    @SideOnly(Side.CLIENT)
    public static void rendererItem(Item item, IItemRenderer renderer) {
        MinecraftForgeClient.registerItemRenderer(item, renderer);
    }

    @SideOnly(Side.CLIENT)
    public static void rendererBlock(ISimpleBlockRenderingHandler renderer) {
        RenderingRegistry.registerBlockHandler(renderer);
    }

    @SideOnly(Side.CLIENT)
    public static void rendererBlock(int id, ISimpleBlockRenderingHandler renderer) {
        RenderingRegistry.registerBlockHandler(id, renderer);
    }
}
