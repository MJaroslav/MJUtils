package io.github.mjaroslav.mjutils.asm.mixin.accessors;

import net.minecraft.block.BlockChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BlockChest.class)
public interface AccessorBlockChest {
    @Invoker("func_149954_e")
    void rotateAndConnect(@NotNull World world, int x, int y, int z);

    @Invoker("func_149952_n")
    boolean isSameBlockAround(@NotNull World world, int x, int y, int z);

    @Invoker("func_149951_m")
    @Nullable IInventory getInventory(@NotNull World world, int x, int y, int z);

    @Invoker("func_149953_o")
    static boolean isOcelotSittingOnChest(@NotNull World world, int x, int y, int z) {
        throw new AssertionError();
    }
}
