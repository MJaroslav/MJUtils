package io.github.mjaroslav.mjutils.object.game.world;

import lombok.*;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Special helper for creating complex block collision bounding boxes. Creating boxes by methods of this set will
 * be automatically adding them to internal list and after creating of all required AABBs, you can just delegate
 * all calculations of {@link net.minecraft.block.Block#addCollisionBoxesToList(World, int, int, int, AxisAlignedBB, List, Entity)
 * Block#addCollisionBoxesToList} by
 * calling internal same named function. You also can visualize all collision bounding boxes by debug highlighting option.
 * <br><br>Example of usage:
 * <pre>{@code
 * ...
 *
 * public class SomeBlock extends Block {
 *     // You can use BlockAABBSet#compile(Consumer) method for a one-time creation of AABBs list.
 *     private final BlockAABBSet bounds = BlockAABBSet.compile(set -> {
 *         // It's cauldron collision copy but created by BlockAABBSet
 *         set.cloneBoxInCircleY(set.addBox(0, 0, 0, 16, 16, 2));
 *         set.addBox(0, 0, 0, 16, 5, 16);
 *     });
 *
 *     public SomeBlock() {
 *         super(...);
 *         ...
 *     }
 *
 *     // Just call same name method from set.
 *     @Override
 *     public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB mask, List list,
 *                                         Entity collidedEntity) {
 *         bounds.addCollisionBoxesToList(x, y, z, mask, list);
 *     }
 * }
 * }</pre>
 *
 * @apiNote Inspired by the dispute with MaxLegend
 */
@Getter
@EqualsAndHashCode
public class BlockAABBSet {
    private final List<BlockAABB> boxes = new ArrayList<>();

    /**
     * Special method for one time creating of ABBSs similar to example in class-level documentation.
     *
     * @param consumer function with AABBs.
     * @return Set of AABBs ready for usage.
     */
    @Contract("_ -> new")
    public static @NotNull BlockAABBSet compile(@NotNull Consumer<BlockAABBSet> consumer) {
        val result = new BlockAABBSet();
        consumer.accept(result);
        return result;
    }

    /**
     * Checks for out of from working ranges in specifier location.
     *
     * @param box reformatted for testing.
     * @param x   x coordinate in world.
     * @param y   y coordinate in world.
     * @param z   z coordinate in world.
     * @return true if x or z not in range from 0 to 1 or y not in range from 0 to 2.
     */
    public static boolean isDeadZoned(@NotNull AxisAlignedBB box, int x, int y, int z) {
        return (box.minX - x) < 0 || (box.minX - x) > 6 || (box.minY - y) < 0 || (box.minY - y) > 2 || (box.minZ - z) < 0
            || (box.minZ - z) > 1 || (box.maxX - x) < 0 || (box.maxX - x) > 1 || (box.maxY - y) < 0 || (box.maxY - y) > 2
            || (box.maxZ - z) < 0 || (box.maxZ - z) > 1;
    }

    /**
     * Reformat internal AABBs to Block AABBs and add them to list of collisions. Just call this from same named
     * method in {@link net.minecraft.block.Block Block}.
     *
     * @see net.minecraft.block.Block#addCollisionBoxesToList(World, int, int, int, AxisAlignedBB, List, Entity)
     */
    @SuppressWarnings({"rawtypes", "unchecked"}) // I hate forge decompiler with generic losing
    public void addCollisionBoxesToList(int x, int y, int z, @NotNull AxisAlignedBB mask, @NotNull List list) {
        boxes.stream().map(box -> box.format(x, y, z)).filter(mask::intersectsWith).forEach(list::add);
    }

    /**
     * Create and add AABBs to list. You can swap min and max values for each axis in arguments.
     * Values outside the ranges are considered dead by the game and are not used during collision calculation.
     *
     * @param minX minimum value of x in range from 0 to 16
     * @param minY minimum value of y in range from 0 to 32
     * @param minZ minimum value of z in range from 0 to 16
     * @param maxX maximum value of x in range from 0 to 16
     * @param maxY maximum value of y in range from 0 to 32 (vanilla fences and gates use 24 btw)
     * @param maxZ maximum value of z in range from 0 to 16
     * @return normalized and added to list AABB.
     */
    @Contract("_, _, _, _, _, _ -> new")
    public @NotNull BlockAABB addBox(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        val result = new BlockAABB(minX, minY, minZ, maxX, maxY, maxZ);
        result.normalize();
        boxes.add(result);
        return result;
    }

    /**
     * Just adds already created box to list.
     *
     * @param box new box then not was added to list.
     */
    @Contract("_ -> param1")
    public @NotNull BlockAABB addBox(@NotNull BlockAABB box) {
        boxes.add(box);
        return box;
    }

    /**
     * Create and add new AABB to list with axis rotations in argument order.
     * Count arguments is a number of ninety-degree turns counterclockwise for positive and clockwise for negative values.
     *
     * @param box    original AABB for use in transformations.
     * @param xCount turns for x-axis.
     * @param yCount turns for y-axis.
     * @param zCount turns for z-axis.
     * @return new transformed AABB.
     * @see BlockAABB#rotate(int, int, int)
     */
    @Contract("_, _, _, _ -> new")
    public @NotNull BlockAABB cloneBoxRotated(@NotNull BlockAABB box, int xCount, int yCount, int zCount) {
        box = box.rotate(xCount, yCount, zCount);
        boxes.add(box);
        return box;
    }

    /**
     * Create and add new AABB to list with flips in argument order.
     *
     * @param box original AABB for use in transformations.
     * @param x   flip on x-axis.
     * @param y   flip on y-axis.
     * @param z   flip on z-axis.
     * @return new transformed AABB.
     * @see BlockAABB#flip(boolean, boolean, boolean)
     */
    @Contract("_, _, _, _ -> new")
    public BlockAABB cloneBoxMirrored(@NotNull BlockAABB box, boolean x, boolean y, boolean z) {
        box = box.flip(x, y, z);
        boxes.add(box);
        return box;
    }

    /**
     * Create and add new AABB to list with flip on x-axis.
     *
     * @param box original AABB for use in transformations.
     * @return new transformed AABB.
     */
    @Contract("_ -> new")
    public BlockAABB cloneBoxMirroredX(@NotNull BlockAABB box) {
        box = box.flipX();
        boxes.add(box);
        return box;
    }

    /**
     * Create and add new AABB to list with flip on y-axis.
     *
     * @param box original AABB for use in transformations.
     * @return new transformed AABB.
     */
    @Contract("_ -> new")
    public BlockAABB cloneBoxMirroredY(@NotNull BlockAABB box) {
        box = box.flipY();
        boxes.add(box);
        return box;
    }

    /**
     * Create and add new AABB to list with flip on z-axis.
     *
     * @param box original AABB for use in transformations.
     * @return new transformed AABB.
     */
    @Contract("_ -> new")
    public BlockAABB cloneBoxMirroredZ(@NotNull BlockAABB box) {
        box = box.flipZ();
        boxes.add(box);
        return box;
    }

    /**
     * Makes three cyclic rotated on x-axis clones of AABB and adds them to list.
     * Very useful for creating well/walls like a cauldron sides.
     *
     * @param box original AABB for use in transformations. This must be already in list.
     */
    public void cloneBoxInCircleX(@NotNull BlockAABB box) {
        for (var i = 0; i < 3; i++) {
            box = box.rotateX(1);
            boxes.add(box);
        }
    }

    /**
     * Makes three cyclic rotated on y-axis clones of AABB and adds them to list.
     * Very useful for creating well/walls like a cauldron sides.
     *
     * @param box original AABB for use in transformations. This must be already in list.
     */
    public void cloneBoxInCircleY(@NotNull BlockAABB box) {
        for (var i = 0; i < 3; i++) {
            box = box.rotateY(1);
            boxes.add(box);
        }
    }

    /**
     * Makes three cyclic rotated on z-axis clones of AABB and adds them to list.
     * Very useful for creating well/walls like a cauldron sides.
     *
     * @param box original AABB for use in transformations. This must be already in list.
     */
    public void cloneBoxInCircleZ(@NotNull BlockAABB box) {
        for (var i = 0; i < 3; i++) {
            box = box.rotateZ(1);
            boxes.add(box);
        }
    }

    @Contract("_, _, _ -> new")
    public BlockAABBSet rotate(int x, int y, int z) {
        val result = new BlockAABBSet();
        getBoxes().stream().map(box -> box.rotate(x, y, z)).forEach(result::addBox);
        return result;
    }

    /**
     * Axis Aligned Bounding Box for creating {@link net.minecraft.block.Block Block} collisions.
     * Can consume values in range from 0 to 16 for x- and z-axis and from 0 to 32 for y-axis.
     * Values outside the ranges are considered dead by the game and are not used during collision calculation.
     */
    @SuppressWarnings("SuspiciousNameCombination") // WTF
    @AllArgsConstructor
    @Data
    public static class BlockAABB {
        /**
         * Box parameters. For usage in {@link net.minecraft.block.Block Block} should be converted by
         * {@link BlockAABB#format(int, int, int)} function with block coordinates.
         */
        private double minX, minY, minZ, maxX, maxY, maxZ;

        /**
         * Create new AABB with same parameters.
         *
         * @return new AABB.
         */
        @Contract(" -> new")
        public @NotNull BlockAABB copy() {
            return new BlockAABB(minX, minY, minZ, maxX, maxY, maxZ);
        }

        /**
         * Create new AABB with axis rotations in argument order.
         * Count arguments is a number of ninety-degree turns counterclockwise for positive and clockwise for negative values.
         *
         * @param xCount turns for x-axis.
         * @param yCount turns for y-axis.
         * @param zCount turns for z-axis.
         * @return new transformed AABB.
         */
        @Contract("_, _, _ -> new")
        public BlockAABB rotate(int xCount, int yCount, int zCount) {
            if (xCount == 0 && yCount == 0 && zCount == 0) return copy();
            val result = copy();
            result.centralize();
            if (xCount != 0) {
                val xReverse = xCount < 0;
                if (xCount % 2 == 0) {
                    result.minY *= -1;
                    result.minZ *= -1;
                    result.maxY *= -1;
                    result.maxZ *= -1;
                } else {
                    // (a;b) <-> (b;-a)
                    result.minY += result.minZ;
                    result.minZ = result.minY - result.minZ;
                    result.minY -= result.minZ;
                    if (xReverse) result.minY *= -1;
                    else result.minZ *= -1;
                    // (a;b) <-> (b;-a)
                    result.maxY += result.maxZ;
                    result.maxZ = result.maxY - result.maxZ;
                    result.maxY -= result.maxZ;
                    if (xReverse) result.maxY *= -1;
                    else result.maxZ *= -1;
                }
            }
            if (yCount != 0) {
                val yReverse = yCount < 0;
                if (yCount % 2 == 0) {
                    result.minX *= -1;
                    result.minZ *= -1;
                    result.maxX *= -1;
                    result.maxZ *= -1;
                } else {
                    // (a;b) <-> (b;-a)
                    result.minX += result.minZ;
                    result.minZ = result.minX - result.minZ;
                    result.minX -= result.minZ;
                    if (yReverse) result.minX *= -1;
                    else result.minZ *= -1;
                    // (a;b) <-> (b;-a)
                    result.maxX += result.maxZ;
                    result.maxZ = result.maxX - result.maxZ;
                    result.maxX -= result.maxZ;
                    if (yReverse) result.maxX *= -1;
                    else result.maxZ *= -1;
                }
            }
            if (zCount != 0) {
                val zReverse = zCount < 0;
                if (zCount % 2 == 0) {
                    result.minX *= -1;
                    result.minY *= -1;
                    result.maxX *= -1;
                    result.maxY *= -1;
                } else {
                    // (a;b) <-> (b;-a)
                    result.minX += result.minY;
                    result.minY = result.minX - result.minY;
                    result.minX -= result.minY;
                    if (zReverse) result.minX *= -1;
                    else result.minY *= -1;
                    // (a;b) <-> (b;-a)
                    result.maxX += result.maxY;
                    result.maxY = result.maxX - result.maxY;
                    result.maxX -= result.maxY;
                    if (zReverse) result.maxX *= -1;
                    else result.maxY *= -1;
                }
            }
            result.decentralize();
            result.normalize();
            return result;
        }

        /**
         * Create new AABB with axis rotations on x-axis.
         * Count arguments is a number of ninety-degree turns counterclockwise for positive and clockwise for negative values.
         *
         * @param count turns.
         * @return new transformed AABB.
         */
        @Contract("_ -> new")
        public BlockAABB rotateX(int count) {
            if (count == 0) return copy();
            else if (count % 2 == 0) return flipX();
            else return rotateX(count < 0);
        }

        /**
         * Create new AABB with axis rotations on y-axis.
         * Count arguments is a number of ninety-degree turns counterclockwise for positive and clockwise for negative values.
         *
         * @param count turns.
         * @return new transformed AABB.
         */
        @Contract("_ -> new")
        public BlockAABB rotateY(int count) {
            if (count == 0) return copy();
            else if (count % 2 == 0) return flipY();
            else return rotateY(count < 0);
        }

        /**
         * Create new AABB with axis rotations on z-axis.
         * Count arguments is a number of ninety-degree turns counterclockwise for positive and clockwise for negative values.
         *
         * @param count turns.
         * @return new transformed AABB.
         */
        @Contract("_ -> new")
        public BlockAABB rotateZ(int count) {
            if (count == 0) return copy();
            else if (count % 2 == 0) return flipZ();
            else return rotateZ(count < 0);
        }

        /**
         * Create new AABB with flips in argument order.
         *
         * @param x flip on x-axis.
         * @param y flip on y-axis.
         * @param z flip on z-axis.
         * @return new transformed AABB.
         */
        @Contract("_, _, _ -> new")
        public @NotNull BlockAABB flip(boolean x, boolean y, boolean z) {
            val result = copy();
            result.centralize();
            if (x) {
                result.minY *= -1;
                result.minZ *= -1;
                result.maxY *= -1;
                result.maxZ *= -1;
            }
            if (y) {
                result.minX *= -1;
                result.minZ *= -1;
                result.maxX *= -1;
                result.maxZ *= -1;
            }
            if (z) {
                result.minX *= -1;
                result.minY *= -1;
                result.maxX *= -1;
                result.maxY *= -1;
            }
            result.decentralize();
            result.normalize();
            return result;
        }

        /**
         * Create new AABB with flip on x-axis.
         *
         * @return new transformed AABB.
         */
        @Contract(" -> new")
        public @NotNull BlockAABB flipX() {
            val result = copy();
            result.centralize();
            result.minY *= -1;
            result.minZ *= -1;
            result.maxY *= -1;
            result.maxZ *= -1;
            result.decentralize();
            result.normalize();
            return result;
        }

        /**
         * Create new AABB with flip on y-axis.
         *
         * @return new transformed AABB.
         */
        @Contract(" -> new")
        public @NotNull BlockAABB flipY() {
            val result = copy();
            result.centralize();
            result.minX *= -1;
            result.minZ *= -1;
            result.maxX *= -1;
            result.maxZ *= -1;
            result.decentralize();
            result.normalize();
            return result;
        }

        /**
         * Create new AABB with flip on z-axis.
         *
         * @return new transformed AABB.
         */
        @Contract(" -> new")
        public @NotNull BlockAABB flipZ() {
            val result = copy();
            result.centralize();
            result.minX *= -1;
            result.minY *= -1;
            result.maxX *= -1;
            result.maxY *= -1;
            result.decentralize();
            result.normalize();
            return result;
        }

        @Contract("_ -> new")
        public @NotNull BlockAABB rotateX(boolean reverse) {
            val result = copy();
            result.centralize();
            // (a;b) <-> (b;-a)
            result.minY += result.minZ;
            result.minZ = result.minY - result.minZ;
            result.minY -= result.minZ;
            if (reverse) result.minY *= -1;
            else result.minZ *= -1;
            // (a;b) <-> (b;-a)
            result.maxY += result.maxZ;
            result.maxZ = result.maxY - result.maxZ;
            result.maxY -= result.maxZ;
            if (reverse) result.maxY *= -1;
            else result.maxZ *= -1;
            result.decentralize();
            result.normalize();
            return result;
        }

        @Contract("_ -> new")
        public @NotNull BlockAABB rotateY(boolean reverse) {
            val result = copy();
            result.centralize();
            // (a;b) <-> (b;-a)
            result.minX += result.minZ;
            result.minZ = result.minX - result.minZ;
            result.minX -= result.minZ;
            if (reverse) result.minX *= -1;
            else result.minZ *= -1;
            // (a;b) <-> (b;-a)
            result.maxX += result.maxZ;
            result.maxZ = result.maxX - result.maxZ;
            result.maxX -= result.maxZ;
            if (reverse) result.maxX *= -1;
            else result.maxZ *= -1;
            result.decentralize();
            result.normalize();
            return result;
        }

        @Contract("_ -> new")
        public @NotNull BlockAABB rotateZ(boolean reverse) {
            val result = copy();
            result.centralize();
            // (a;b) <-> (b;-a)
            result.minX += result.minY;
            result.minY = result.minX - result.minY;
            result.minX -= result.minY;
            if (reverse) result.minX *= -1;
            else result.minY *= -1;
            // (a;b) <-> (b;-a)
            result.maxX += result.maxY;
            result.maxY = result.maxX - result.maxY;
            result.maxX -= result.maxY;
            if (reverse) result.maxX *= -1;
            else result.maxY *= -1;
            result.decentralize();
            result.normalize();
            return result;
        }

        /**
         * Move zeros of axis to center of block (move all to -8).
         * Full block bounds after this will be from -8;-8;-8 to 8;8;8.
         * Required for transformations, {@link BlockAABB#decentralize()} and {@link BlockAABB#normalize()}
         * must be called after any calculations.
         */
        public void centralize() {
            minX -= 8;
            minY -= 8;
            minZ -= 8;
            maxX -= 8;
            maxY -= 8;
            maxZ -= 8;
        }

        /**
         * Move zeros of axis to corner of block (move all to +8).
         * Full block bounds after this will be from 0;0;0 to 16;16;16 again.
         * Required for transformations, {@link BlockAABB#centralize()} must be called before and {@link BlockAABB#normalize()}
         * after any calculations.
         */
        public void decentralize() {
            minX += 8;
            minY += 8;
            minZ += 8;
            maxX += 8;
            maxY += 8;
            maxZ += 8;
        }

        /**
         * Just swaps minimums and maximums of each axis if they not on own places.
         */
        public void normalize() {
            if (maxX < minX) {
                // Swap values
                maxX += minX;
                minX = maxX - minX;
                maxX -= minX;
            }
            if (maxZ < minZ) {
                // Swap values
                maxZ += minZ;
                minZ = maxZ - minZ;
                maxZ -= minZ;
            }
            if (maxY < minY) {
                // Swap values
                maxY += minY;
                minY = maxY - minY;
                maxY -= minY;
            }
        }

        /**
         * Checks for out of from working ranges.
         *
         * @return true if x or z not in range from 0 to 16 or y not in range from 0 to 32.
         */
        public boolean isDeadZoned() {
            return minX < 0 || minX > 16 || minY < 0 || minY > 32 || minZ < 0 || minZ > 16
                || maxX < 0 || maxX > 16 || maxY < 0 || maxY > 32 || maxZ < 0 || maxZ > 16;
        }

        /**
         * Format BlockAABB to original {@link AxisAlignedBB} including position in world.
         *
         * @param x current x world position.
         * @param y current x world position.
         * @param z current x world position.
         * @return new {@link AxisAlignedBB} created from this BlockAABB.
         */
        @Contract("_, _, _ -> new")
        public @NotNull AxisAlignedBB format(int x, int y, int z) {
            return AxisAlignedBB.getBoundingBox((double) x + (minX / 16d), (double) y + (minY / 16d),
                (double) z + (minZ / 16d), (double) x + (maxX / 16d), (double) y + (maxY / 16d), (double) z + (maxZ / 16d));
        }
    }
}
