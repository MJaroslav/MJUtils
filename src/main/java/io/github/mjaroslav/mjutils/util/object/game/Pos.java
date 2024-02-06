package io.github.mjaroslav.mjutils.util.object.game;

import io.github.mjaroslav.sharedjava.tuple.triplet.DTriplet;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.Vec3;
import net.minecraft.world.ChunkPosition;
import net.minecraftforge.common.util.ForgeDirection;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

import static java.lang.Math.*;

public class Pos extends DTriplet implements Comparable<Object> {
    public static final Pos xyz = new Pos(-1, -1, -1);
    public static final Pos xy = new Pos(-1, -1, 0);
    public static final Pos xyZ = new Pos(-1, -1, 1);
    public static final Pos xz = new Pos(-1, 0, -1);
    public static final Pos x = new Pos(-1, 0, 0);
    public static final Pos xZ = new Pos(-1, 0, 1);
    public static final Pos xYz = new Pos(-1, 1, -1);
    public static final Pos xY = new Pos(-1, 1, 0);
    public static final Pos xYZ = new Pos(-1, 1, 1);
    public static final Pos yz = new Pos(0, -1, -1);
    public static final Pos y = new Pos(0, -1, 0);
    public static final Pos yZ = new Pos(0, -1, 1);
    public static final Pos z = new Pos(0, 0, -1);
    public static final Pos ZERO = new Pos(0, 0, 0);
    public static final Pos Z = new Pos(0, 0, 1);
    public static final Pos Yz = new Pos(0, 1, -1);
    public static final Pos Y = new Pos(0, 1, 0);
    public static final Pos YZ = new Pos(0, 1, 1);
    public static final Pos Xyz = new Pos(1, -1, -1);
    public static final Pos Xy = new Pos(1, -1, 0);
    public static final Pos XyZ = new Pos(1, -1, 1);
    public static final Pos Xz = new Pos(1, 0, -1);
    public static final Pos X = new Pos(1, 0, 0);
    public static final Pos XZ = new Pos(1, 0, 1);
    public static final Pos XYz = new Pos(1, 1, -1);
    public static final Pos XY = new Pos(1, 1, 0);
    public static final Pos XYZ = new Pos(1, 1, 1);

    public Pos(@NotNull ChunkPosition position) {
        super(position.chunkPosX, position.chunkPosY, position.chunkPosZ);
    }

    public Pos(@NotNull Vec3 vec) {
        super(vec.xCoord, vec.yCoord, vec.zCoord);
    }

    public Pos(@NotNull ForgeDirection direction) {
        super(direction.offsetX, direction.offsetY, direction.offsetZ);
    }

    public Pos(@NotNull ChunkCoordinates coordinates) {
        super(coordinates.posX, coordinates.posY, coordinates.posZ);
    }

    public Pos(@NotNull Pos pos) {
        super(pos.getX(), pos.getY(), pos.getZ());
    }

    public Pos(double x, double y, double z) {
        super(x, y, z);
    }

    public Pos() {
        super(0d, 0d, 0d); // I'm very careful
    }

    public void set(@NotNull Vec3 vec) {
        set(vec.xCoord, vec.yCoord, vec.zCoord);
    }

    public void set(@NotNull ChunkCoordinates coordinates) {
        set((double) coordinates.posX, (double) coordinates.posY, (double) coordinates.posZ);
    }

    public void set(@NotNull ChunkPosition position) {
        set((double) position.chunkPosX, (double) position.chunkPosY, (double) position.chunkPosZ);
    }

    public void set(@NotNull Pos pos) {
        set(pos.getX(), pos.getY(), pos.getZ());
    }

    @Contract(" -> new")
    public Pos plusX() {
        return new Pos(getX() + 1, getY(), getZ());
    }

    @Contract(" -> new")
    public Pos plusY() {
        return new Pos(getX(), getY() + 1, getZ());
    }

    @Contract(" -> new")
    public Pos plusZ() {
        return new Pos(getX(), getY(), getZ() + 1);
    }

    @Contract(" -> new")
    public Pos minusX() {
        return new Pos(getX() - 1, getY(), getZ());
    }

    @Contract(" -> new")
    public Pos minusY() {
        return new Pos(getX(), getY() - 1, getZ());
    }

    @Contract(" -> new")
    public Pos minusZ() {
        return new Pos(getX(), getY(), getZ() - 1);
    }

    @Contract("_ -> new")
    public Pos plusX(double distance) {
        return new Pos(getX() + distance, getY(), getZ());
    }

    @Contract("_ -> new")
    public Pos plusY(double distance) {
        return new Pos(getX(), getY() + distance, getZ());
    }

    @Contract("_ -> new")
    public Pos plusZ(double distance) {
        return new Pos(getX(), getY(), getZ() + distance);
    }

    @Contract("_ -> new")
    public Pos minusX(double distance) {
        return new Pos(getX() - distance, getY(), getZ());
    }

    @Contract("_ -> new")
    public Pos minusY(double distance) {
        return new Pos(getX(), getY() - distance, getZ());
    }

    @Contract("_ -> new")
    public Pos minusZ(double distance) {
        return new Pos(getX(), getY(), getZ() - distance);
    }

    @Contract("_ -> new")
    public Pos mulX(double distance) {
        return new Pos(getX() * distance, getY(), getZ());
    }

    @Contract("_ -> new")
    public Pos mulY(double distance) {
        return new Pos(getX(), getY() * distance, getZ());
    }

    @Contract("_ -> new")
    public Pos mulZ(double distance) {
        return new Pos(getX(), getY(), getZ() * distance);
    }

    @Contract("_ -> new")
    public Pos divX(double distance) {
        return new Pos(getX() / distance, getY(), getZ());
    }

    @Contract("_ -> new")
    public Pos divY(double distance) {
        return new Pos(getX(), getY() / distance, getZ());
    }

    @Contract("_ -> new")
    public Pos divZ(double distance) {
        return new Pos(getX(), getY(), getZ() / distance);
    }

    public Pos add(@NotNull ChunkPosition position) {
        if (position.chunkPosX == 0 && position.chunkPosY == 0 && position.chunkPosZ == 0) return this;
        return new Pos(getX() + position.chunkPosX, getY() + position.chunkPosY, getZ() + position.chunkPosZ);
    }

    public Pos add(@NotNull Vec3 vec) {
        if (vec.xCoord == 0 && vec.yCoord == 0 && vec.zCoord == 0) return this;
        return new Pos(getX() + vec.xCoord, getY() + vec.yCoord, getZ() + vec.zCoord);
    }

    public Pos add(@NotNull ChunkCoordinates coordinates) {
        if (coordinates.posX == 0 && coordinates.posY == 0 && coordinates.posZ == 0) return this;
        return new Pos(getX() + coordinates.posX, getY() + coordinates.posY, getZ() + coordinates.posZ);
    }

    public Pos add(@NotNull ForgeDirection direction) {
        return add(direction, 1);
    }

    public Pos add(@NotNull ForgeDirection direction, double term) {
        if (term == 0) return this;
        return new Pos(getX() + direction.offsetX * term, getY() + direction.offsetY * term, getZ() + direction.offsetZ * term);
    }

    public Pos add(@NotNull Pos pos) {
        if (pos.getX() == 0 && pos.getY() == 0 && pos.getZ() == 0) return this;
        return new Pos(getX() + pos.getX(), getY() + pos.getY(), getZ() + pos.getZ());
    }

    public Pos add(double term) {
        if (term == 0) return this;
        return new Pos(getX() + term, getY() + term, getZ() + term);
    }

    public Pos add(double x, double y, double z) {
        if (x == 0 && y == 0 && z == 0) return this;
        return new Pos(getX() + x, getY() + y, getZ() + z);
    }

    public Pos sub(@NotNull ChunkPosition position) {
        if (position.chunkPosX == 0 && position.chunkPosY == 0 && position.chunkPosZ == 0) return this;
        return new Pos(getX() - position.chunkPosX, getY() - position.chunkPosY, getZ() - position.chunkPosZ);
    }

    public Pos sub(@NotNull Vec3 vec) {
        if (vec.xCoord == 0 && vec.yCoord == 0 && vec.zCoord == 0) return this;
        return new Pos(getX() - vec.zCoord, getY() - vec.yCoord, getZ() - vec.zCoord);
    }

    public Pos sub(@NotNull ChunkCoordinates coordinates) {
        if (coordinates.posX == 0 && coordinates.posY == 0 && coordinates.posZ == 0) return this;
        return new Pos(getX() - coordinates.posX, getY() - coordinates.posY, getZ() - coordinates.posZ);
    }

    public Pos sub(@NotNull ForgeDirection direction) {
        return sub(direction, 1);
    }

    public Pos sub(@NotNull ForgeDirection direction, double subtrahend) {
        if (subtrahend == 0) return this;
        return new Pos(getX() - direction.offsetX * subtrahend, getY() - direction.offsetY * subtrahend, getZ() - direction.offsetZ * subtrahend);
    }

    public Pos sub(@NotNull Pos pos) {
        if (pos.getX() == 0 && pos.getY() == 0 && pos.getZ() == 0) return this;
        return new Pos(getX() - pos.getX(), getY() - pos.getY(), getZ() - pos.getZ());
    }

    public Pos sub(double x, double y, double z) {
        if (x == 0 && y == 0 && z == 0) return this;
        return new Pos(getX() - x, getY() - y, getZ() - z);
    }

    public Pos sub(double subtrahend) {
        if (subtrahend == 0) return this;
        return new Pos(getX() - subtrahend, getY() - subtrahend, getZ() - subtrahend);
    }

    public Pos mul(@NotNull ChunkPosition position) {
        if (position.chunkPosX == 1 && position.chunkPosY == 1 && position.chunkPosZ == 1) return this;
        return new Pos(getX() * position.chunkPosX, getY() * position.chunkPosY, getZ() * position.chunkPosZ);
    }

    public Pos mul(@NotNull Vec3 vec) {
        if (vec.xCoord == 1 && vec.yCoord == 1 && vec.zCoord == 1) return this;
        return new Pos(getX() * vec.xCoord, getY() * vec.yCoord, getZ() * vec.zCoord);
    }

    public Pos mul(@NotNull ChunkCoordinates coordinates) {
        if (coordinates.posX == 1 && coordinates.posY == 1 && coordinates.posZ == 1) return this;
        return new Pos(getX() * coordinates.posX, getY() * coordinates.posY, getZ() * coordinates.posZ);
    }

    public Pos mul(@NotNull ForgeDirection direction, double factor) {
        if (factor == 1) return this;
        var x = direction.offsetX * factor;
        if (x == 0) x = 1;
        var y = direction.offsetY * factor;
        if (y == 0) y = 1;
        var z = direction.offsetZ * factor;
        if (z == 0) z = 1;
        return new Pos(getX() * x, getY() * y, getZ() * z);
    }

    public Pos mul(@NotNull Pos pos) {
        if (pos.getX() == 1 && pos.getY() == 1 && pos.getZ() == 1) return this;
        return new Pos(getX() * pos.getX(), getY() * pos.getY(), getZ() * pos.getZ());
    }

    public Pos mul(double factor) {
        if (factor == 1) return this;
        return new Pos(getX() * factor, getY() * factor, getZ() * factor);
    }

    public Pos mul(double x, double y, double z) {
        if (x == 1 && y == 1 && z == 1) return this;
        return new Pos(getX() * x, getY() * y, getZ() * z);
    }

    public Pos div(@NotNull ChunkPosition position) {
        if (position.chunkPosX == 1 && position.chunkPosY == 1 && position.chunkPosZ == 1) return this;
        return new Pos(getX() / position.chunkPosX, getY() / position.chunkPosY, getZ() / position.chunkPosZ);
    }

    public Pos div(@NotNull Vec3 vec) {
        if (vec.xCoord == 1 && vec.yCoord == 1 && vec.zCoord == 1) return this;
        return new Pos(getX() - vec.xCoord, getY() - vec.yCoord, getZ() - vec.zCoord);
    }

    public Pos div(@NotNull ChunkCoordinates coordinates) {
        if (coordinates.posX == 1 && coordinates.posY == 1 && coordinates.posZ == 1) return this;
        return new Pos(getX() / coordinates.posX, getY() / coordinates.posY, getZ() / coordinates.posZ);
    }

    public Pos div(@NotNull ForgeDirection direction, double divider) {
        if (divider == 1) return this;
        var x = direction.offsetX * divider;
        if (x == 0) x = 1;
        var y = direction.offsetY * divider;
        if (y == 0) y = 1;
        var z = direction.offsetZ * divider;
        if (z == 0) z = 1;
        return new Pos(getX() * x, getY() * y, getZ() * z);
    }

    public Pos div(@NotNull Pos pos) {
        if (pos.getX() == 1 && pos.getY() == 1 && pos.getZ() == 1) return this;
        return new Pos(getX() / pos.getX(), getY() / pos.getY(), getZ() / pos.getZ());
    }

    public Pos div(double divider) {
        if (divider == 1) return this;
        return new Pos(getX() / divider, getY() / divider, getZ() / divider);
    }

    public Pos div(double x, double y, double z) {
        if (x == 1 && y == 1 && z == 1) return this;
        return new Pos(getX() / x, getY() / y, getZ() / z);
    }

    public double diameter() {
        return sqrt(pow(getX(), 2) + pow(getY(), 2) + pow(getZ(), 2));
    }

    public double distance(@NotNull ChunkPosition position) {
        return sqrt(pow(getX() - position.chunkPosX, 2) + pow(getY() - position.chunkPosY, 2) + pow(getZ() - position.chunkPosZ, 2));
    }

    public double distance(@NotNull Vec3 vec) {
        return sqrt(pow(getX() - vec.xCoord, 2) + pow(getY() - vec.yCoord, 2) + pow(getZ() - vec.zCoord, 2));
    }

    public double distance(@NotNull ChunkCoordinates coordinates) {
        return sqrt(pow(getX() - coordinates.posX, 2) + pow(getY() - coordinates.posY, 2) + pow(getZ() - coordinates.posZ, 2));
    }

    public double distance(@NotNull Pos pos) {
        return sqrt(pow(getX() - pos.getX(), 2) + pow(getY() - pos.getY(), 2) + pow(getZ() - pos.getZ(), 2));
    }

    public double distance(double x, double y, double z) {
        return sqrt(pow(getX() - x, 2) + pow(getY() - y, 2) + pow(getZ() - z, 2));
    }

    public double diameterQrt() {
        return pow(getX(), 2) + pow(getY(), 2) + pow(getZ(), 2);
    }

    public double distanceQrt(@NotNull ChunkPosition position) {
        return pow(getX() - position.chunkPosX, 2) + pow(getY() - position.chunkPosY, 2) + pow(getZ() - position.chunkPosZ, 2);
    }

    public double distanceQrt(@NotNull Vec3 vec) {
        return pow(getX() - vec.xCoord, 2) + pow(getY() - vec.yCoord, 2) + pow(getZ() - vec.zCoord, 2);
    }

    public double distanceQrt(@NotNull ChunkCoordinates coordinates) {
        return pow(getX() - coordinates.posZ, 2) + pow(getY() - coordinates.posY, 2) + pow(getZ() - coordinates.posZ, 2);
    }

    public double distanceQrt(@NotNull Pos pos) {
        return pow(getX() - pos.getX(), 2) + pow(getY() - pos.getY(), 2) + pow(getZ() - pos.getZ(), 2);
    }

    public double distanceQrt(double x, double y, double z) {
        return pow(getX() - x, 2) + pow(getY() - y, 2) + pow(getZ() - z, 2);
    }

    public float floatX() {
        return getX().floatValue();
    }

    public float floatY() {
        return getY().floatValue();
    }

    public float floatZ() {
        return getZ().floatValue();
    }

    public int intX() {
        return getX().intValue();
    }

    public int intY() {
        return getY().intValue();
    }

    public int intZ() {
        return getZ().intValue();
    }

    @Contract(" -> new")
    public ChunkPosition toChunkPosition() {
        return new ChunkPosition(intX(), intY(), intZ());
    }

    @Contract(" -> new")
    public Vec3 toVec3() {
        return Vec3.createVectorHelper(getX(), getY(), getZ());
    }

    @Contract(" -> new")
    public ChunkCoordinates toChunkCoordinates() {
        return new ChunkCoordinates(intX(), intY(), intZ());
    }

    public static void forEachInBox(@NotNull Pos a, @NotNull Pos b, @NotNull Consumer<Pos> action) {
        Pos temp = new Pos();
        for (var x = Math.min(a.intX(), b.intX()); x < Math.max(a.intX(), b.intX()) + 1; x++)
            for (var y = Math.min(a.intY(), b.intY()); y < Math.max(a.intY(), b.intY()) + 1; y++)
                for (var z = Math.min(a.intZ(), b.intZ()); z < Math.max(a.intZ(), b.intZ()) + 1; z++) {
                    temp.set((double) x, (double) y, (double) z);
                    action.accept(temp);
                }
    }

    @Contract("_ -> new")
    public AxisAlignedBB toAABB(@NotNull ChunkPosition position) {
        return AxisAlignedBB.getBoundingBox(min(getX(), position.chunkPosX), min(getY(), position.chunkPosY), min(getZ(),
            position.chunkPosZ), max(getX(), position.chunkPosX), max(getY(), position.chunkPosY), max(getZ(), position.chunkPosZ));
    }

    @Contract("_ -> new")
    public AxisAlignedBB toAABB(@NotNull Vec3 vec) {
        return AxisAlignedBB.getBoundingBox(min(getX(), getX() + vec.xCoord), min(getY(), getY() + vec.yCoord),
            min(getZ(), getZ() + vec.zCoord), max(getX(), getX() + vec.xCoord), max(getY(),
                getY() + vec.yCoord), max(getZ(), getZ() + vec.zCoord));
    }

    @Contract("_ -> new")
    public AxisAlignedBB toAABB(@NotNull ChunkCoordinates coordinates) {
        return AxisAlignedBB.getBoundingBox(min(getX(), getX() + coordinates.posX), min(getY(), getY() + coordinates.posY),
            min(getZ(), getZ() + coordinates.posZ), max(getX(), getX() + coordinates.posX), max(getY(),
                getY() + coordinates.posY), max(getZ(), getZ() + coordinates.posZ));
    }

    @Contract("_ -> new")
    public AxisAlignedBB toAABB(@NotNull ForgeDirection direction) {
        return AxisAlignedBB.getBoundingBox(min(getX(), getX() + direction.offsetX), min(getY(), getY() + direction.offsetY),
            min(getZ(), getZ() + direction.offsetZ), max(getX(), getX() + direction.offsetX), max(getY(),
                getY() + direction.offsetY), max(getZ(), getZ() + direction.offsetZ));
    }

    @Contract("_ -> new")
    public AxisAlignedBB toAABB(@NotNull Pos pos) {
        return AxisAlignedBB.getBoundingBox(min(getX(), pos.getX()), min(getY(), pos.getY()), min(getZ(), pos.getZ()),
            max(getX(), pos.getX()), max(getY(), pos.getY()), max(getZ(), pos.getZ()));
    }

    @Contract("_, _, _ -> new")
    public AxisAlignedBB toAABB(double x, double y, double z) {
        return AxisAlignedBB.getBoundingBox(min(getX(), x), min(getY(), y), min(getZ(), z), max(getX(), x),
            max(getY(), y), max(getZ(), z));
    }

    @Contract(" -> new")
    public double[] toArray() {
        return new double[]{getX(), getY(), getZ()};
    }

    @Contract(" -> new")
    public int[] toIntArray() {
        return new int[]{intX(), intY(), intZ()};
    }

    @Override
    public int compareTo(@NotNull Object o) {
        double x, y, z;
        if (o instanceof Pos pos) {
            x = pos.getX();
            y = pos.getY();
            z = pos.getZ();
        } else if (o instanceof ChunkCoordinates coordinates) {
            x = coordinates.posX;
            y = coordinates.posY;
            z = coordinates.posZ;
        } else if (o instanceof ChunkPosition position) {
            x = position.chunkPosX;
            y = position.chunkPosY;
            z = position.chunkPosZ;
        } else if (o instanceof Vec3 vec) {
            x = vec.xCoord;
            y = vec.yCoord;
            z = vec.zCoord;
        } else if (o instanceof DTriplet triplet) {
            x = triplet.getX();
            y = triplet.getY();
            z = triplet.getZ();
        } else return 0;
        return (int) (getY() == y ? (getZ() == z ? getX() - x : getZ() - z) : getY() - y);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        else if (o == this) return true;
        else if (o instanceof Pos pos)
            return getX().equals(pos.getX()) && getY().equals(pos.getY()) && getZ().equals(pos.getZ());
        else if (o instanceof ChunkCoordinates coordinates)
            return intX() == coordinates.posX && intY() == coordinates.posY && intZ() == coordinates.posZ;
        else if (o instanceof ChunkPosition position)
            return intX() == position.chunkPosX && intY() == position.chunkPosY && intZ() == position.chunkPosZ;
        else if (o instanceof Vec3 vec)
            return getX() == vec.xCoord && getY() == vec.yCoord && getZ() == vec.zCoord;
        else if (o instanceof DTriplet triplet)
            return getX().equals(triplet.getX()) && getY().equals(triplet.getY()) && getZ().equals(triplet.getZ());
        else return super.equals(o);
    }

    public boolean isInsideOfBox(@NotNull AxisAlignedBB aabb) {
        return getX() > aabb.minX && getX() < aabb.maxX && getY() > aabb.minY && getY() < aabb.maxY
            && getZ() > aabb.minZ && getZ() < aabb.maxZ;
    }

    @Override
    public String toString() {
        return "Pos(x = " + getX() + ", y = " + getY() + ", z = " + getZ() + ")";
    }
}
