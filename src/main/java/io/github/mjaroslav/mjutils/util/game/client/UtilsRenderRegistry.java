package io.github.mjaroslav.mjutils.util.game.client;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;


/**
 * A set of utilities for working with render.
 */
@SideOnly(Side.CLIENT)
public class UtilsRenderRegistry {
    /**
     * Shortcut for {@link ClientRegistry#bindTileEntitySpecialRenderer(Class,
     * TileEntitySpecialRenderer)
     * ClientRegistry.bindTileEntitySpecialRenderer(tileClass, renderer)}
     *
     * @param tileClass specified TileEntity class.
     * @param renderer  TileEntity render handler.
     */
    public static void rendererTileEntity(Class<? extends TileEntity> tileClass, TileEntitySpecialRenderer renderer) {
        ClientRegistry.bindTileEntitySpecialRenderer(tileClass, renderer);
    }

    /**
     * Shortcut for {@link MinecraftForgeClient#registerItemRenderer(Item,
     * IItemRenderer) MinecraftForgeClient.registerItemRenderer(item, renderer)}
     *
     * @param item     specified item.
     * @param renderer item render handler.
     */
    public static void rendererItem(Item item, IItemRenderer renderer) {
        MinecraftForgeClient.registerItemRenderer(item, renderer);
    }

    /**
     * Shortcut for {@link RenderingRegistry#registerBlockHandler(
     *ISimpleBlockRenderingHandler)
     * RenderingRegistry.registerBlockHandler(renderer)}
     *
     * @param renderer block render handler with block render id.
     */
    public static void rendererBlock(ISimpleBlockRenderingHandler renderer) {
        RenderingRegistry.registerBlockHandler(renderer);
    }

    /**
     * Shortcut for {@link RenderingRegistry#registerBlockHandler(int,
     * ISimpleBlockRenderingHandler)
     * RenderingRegistry.registerBlockHandler(id, renderer)}
     *
     * @param id       block render id
     * @param renderer block render handler.
     */
    public static void rendererBlock(int id, ISimpleBlockRenderingHandler renderer) {
        RenderingRegistry.registerBlockHandler(id, renderer);
    }
}
