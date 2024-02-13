package io.github.mjaroslav.mjutils.util;

import com.google.common.collect.AbstractIterator;
import io.github.mjaroslav.sharedjava.tuple.Triplet;
import io.github.mjaroslav.sharedjava.tuple.triplet.DTriplet;
import lombok.val;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.ChunkPosition;
import net.minecraftforge.common.util.ForgeDirection;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.lang.Math.*;

@Unmodifiable
public class Pos extends DTriplet implements Comparable<Object> {
    //region Constants
    //------------------------------------
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
    public static final Pos ORIGIN = new Pos(0, 0, 0);
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
    //------------------------------------
    //endregion

    protected boolean immutable;

    protected Pos(double x, double y, double z) {
        super(x, y, z);
        immutable = true; // Super sets values by set method
    }

    protected Pos() {
        this(0d, 0d, 0d); // I'm very careful
    }

    //region Mutable creation
    //------------------------------------
    @Contract(" -> new")
    public static @NotNull Mutable mutable() {
        return new Mutable();
    }

    @Contract("_ -> new")
    public static @NotNull Pos mutableOf(@NotNull Pos pos) {
        return mutableOf(pos.getX(), pos.getY(), pos.getZ());
    }

    @Contract("_ -> new")
    public static @NotNull Pos mutableOf(@NotNull ChunkPosition position) {
        return mutableOf(position.chunkPosX, position.chunkPosY, position.chunkPosZ);
    }
    //------------------------------------
    //endregion

    @Contract("_ -> new")
    public static @NotNull Pos mutableOf(@NotNull ChunkCoordinates coordinates) {
        return mutableOf(coordinates.posX, coordinates.posY, coordinates.posZ);
    }

    @Contract("_ -> new")
    public static @NotNull Pos mutableOf(@NotNull ForgeDirection direction) {
        return mutableOf(direction.offsetX, direction.offsetY, direction.offsetZ);
    }

    @Contract("_ -> new")
    public static @NotNull Pos mutableOf(@NotNull Vec3 vec3) {
        return mutableOf(vec3.xCoord, vec3.yCoord, vec3.zCoord);
    }

    @Contract("_ -> new")
    public static @NotNull Pos mutableOf(@NotNull Triplet<? extends Number, ? extends Number, ? extends Number> triplet) {
        return mutableOf(triplet.getX().doubleValue(), triplet.getY().doubleValue(), triplet.getZ().doubleValue());
    }

    @Contract("_, _, _ -> new")
    public static @NotNull Pos mutableOf(double x, double y, double z) {
        return new Mutable(x, y, z);
    }

    @Contract("_ -> new")
    public static @NotNull Pos mutableOfFloor(@NotNull Pos pos) {
        return mutableOfFloor(pos.getX(), pos.getY(), pos.getZ());
    }
    //------------------------------------
    //endregion

    @Contract("_ -> new")
    public static @NotNull Pos mutableOfFloor(@NotNull Vec3 vec3) {
        return mutableOfFloor(vec3.xCoord, vec3.yCoord, vec3.zCoord);
    }

    @Contract("_ -> new")
    public static @NotNull Pos mutableOfFloor(@NotNull Triplet<? extends Number, ? extends Number, ? extends Number> triplet) {
        return mutableOfFloor(triplet.getX().doubleValue(), triplet.getY().doubleValue(), triplet.getZ().doubleValue());
    }

    @Contract("_, _, _ -> new")
    public static @NotNull Pos mutableOfFloor(double x, double y, double z) {
        return mutableOf(MathHelper.floor_double(x), MathHelper.floor_double(y), MathHelper.floor_double(z));
    }

    //region Immutable creation
    //------------------------------------
    @Contract("_ -> new")
    public static @NotNull Pos of(@NotNull Pos pos) {
        return of(pos.getX(), pos.getY(), pos.getZ());
    }

    @Contract("_ -> new")
    public static @NotNull Pos of(@NotNull ChunkPosition position) {
        return of(position.chunkPosX, position.chunkPosY, position.chunkPosZ);
    }

    @Contract("_ -> new")
    public static @NotNull Pos of(@NotNull ChunkCoordinates coordinates) {
        return of(coordinates.posX, coordinates.posY, coordinates.posZ);
    }
    //------------------------------------
    //endregion

    @Contract("_ -> new")
    public static @NotNull Pos of(@NotNull ForgeDirection direction) {
        return of(direction.offsetX, direction.offsetY, direction.offsetZ);
    }

    @Contract("_ -> new")
    public static @NotNull Pos of(@NotNull Vec3 vec3) {
        return of(vec3.xCoord, vec3.yCoord, vec3.zCoord);
    }

    @Contract("_ -> new")
    public static @NotNull Pos of(@NotNull Triplet<? extends Number, ? extends Number, ? extends Number> triplet) {
        return of(triplet.getX().doubleValue(), triplet.getY().doubleValue(), triplet.getZ().doubleValue());
    }

    @Contract("_, _, _ -> new")
    public static @NotNull Pos of(double x, double y, double z) {
        return new Pos(x, y, z);
    }

    @Contract("_ -> new")
    public static @NotNull Pos ofFloor(@NotNull Pos pos) {
        return ofFloor(pos.getX(), pos.getY(), pos.getZ());
    }

    @Contract("_ -> new")
    public static @NotNull Pos ofFloor(@NotNull Vec3 vec3) {
        return ofFloor(vec3.xCoord, vec3.yCoord, vec3.zCoord);
    }
    //------------------------------------
    //endregion

    @Contract("_ -> new")
    public static @NotNull Pos ofFloor(@NotNull Triplet<? extends Number, ? extends Number, ? extends Number> triplet) {
        return ofFloor(triplet.getX().doubleValue(), triplet.getY().doubleValue(), triplet.getZ().doubleValue());
    }

    @Contract("_, _, _ -> new")
    public static @NotNull Pos ofFloor(double x, double y, double z) {
        return of(MathHelper.floor_double(x), MathHelper.floor_double(y), MathHelper.floor_double(z));
    }

    //region Plus/minus one by axis
    //------------------------------------
    @Contract(" -> new")
    public @NotNull Pos plusX() {
        return new Pos(getX() + 1, getY(), getZ());
    }

    @Contract(" -> new")
    public @NotNull Pos plusY() {
        return new Pos(getX(), getY() + 1, getZ());
    }

    @Contract(" -> new")
    public @NotNull Pos plusZ() {
        return new Pos(getX(), getY(), getZ() + 1);
    }

    @Contract(" -> new")
    public @NotNull Pos minusX() {
        return new Pos(getX() - 1, getY(), getZ());
    }

    @Contract(" -> new")
    public @NotNull Pos minusY() {
        return new Pos(getX(), getY() - 1, getZ());
    }

    @Contract(" -> new")
    public @NotNull Pos minusZ() {
        return new Pos(getX(), getY(), getZ() - 1);
    }

    //region Plus/minus distance by axis
    //------------------------------------
    public @NotNull Pos plusX(double distance) {
        if (distance == 0) return this;
        return new Pos(getX() + distance, getY(), getZ());
    }
    //------------------------------------
    //endregion

    public @NotNull Pos plusY(double distance) {
        if (distance == 0) return this;
        return new Pos(getX(), getY() + distance, getZ());
    }

    public @NotNull Pos plusZ(double distance) {
        if (distance == 0) return this;
        return new Pos(getX(), getY(), getZ() + distance);
    }

    public @NotNull Pos minusX(double distance) {
        if (distance == 0) return this;
        return new Pos(getX() - distance, getY(), getZ());
    }

    public @NotNull Pos minusY(double distance) {
        if (distance == 0) return this;
        return new Pos(getX(), getY() - distance, getZ());
    }

    public @NotNull Pos minusZ(double distance) {
        if (distance == 0) return this;
        return new Pos(getX(), getY(), getZ() - distance);
    }

    //region Multiple/Division by axis
    //------------------------------------
    public @NotNull Pos mulX(double distance) {
        if (distance == 1) return this;
        return new Pos(getX() * distance, getY(), getZ());
    }

    public @NotNull Pos mulY(double distance) {
        if (distance == 1) return this;
        return new Pos(getX(), getY() * distance, getZ());
    }

    public @NotNull Pos mulZ(double distance) {
        if (distance == 1) return this;
        return new Pos(getX(), getY(), getZ() * distance);
    }

    public @NotNull Pos divX(double distance) {
        if (distance == 1) return this;
        return new Pos(getX() / distance, getY(), getZ());
    }
    //------------------------------------
    //endregion

    public @NotNull Pos divY(double distance) {
        if (distance == 1) return this;
        return new Pos(getX(), getY() / distance, getZ());
    }

    public @NotNull Pos divZ(double distance) {
        if (distance == 1) return this;
        return new Pos(getX(), getY(), getZ() / distance);
    }

    //region Addition
    //------------------------------------
    public @NotNull Pos add(@NotNull ChunkPosition position) {
        return add(position.chunkPosX, position.chunkPosY, position.chunkPosZ);
    }

    public @NotNull Pos add(@NotNull Vec3 vec) {
        return add(vec.xCoord, vec.yCoord, vec.zCoord);
    }

    public @NotNull Pos add(@NotNull ChunkCoordinates coordinates) {
        return add(coordinates.posX, coordinates.posY, coordinates.posZ);
    }

    public @NotNull Pos add(@NotNull ForgeDirection direction) {
        return add(direction, 1);
    }

    public @NotNull Pos add(@NotNull ForgeDirection direction, double term) {
        return add(direction.offsetX * term, direction.offsetY * term, direction.offsetZ * term);
    }

    public @NotNull Pos add(@NotNull Pos pos) {
        return add(pos.getX(), pos.getY(), pos.getZ());
    }
    //------------------------------------
    //endregion

    public @NotNull Pos add(@NotNull Triplet<? extends Number, ? extends Number, ? extends Number> triplet) {
        return add(triplet.getX().doubleValue(), triplet.getY().doubleValue(), triplet.getZ().doubleValue());
    }

    public @NotNull Pos add(double term) {
        return add(term, term, term);
    }

    public @NotNull Pos add(double x, double y, double z) {
        if (x == 0 && y == 0 && z == 0) return this;
        return new Pos(getX() + x, getY() + y, getZ() + z);
    }

    //region Subtraction
    //------------------------------------
    public @NotNull Pos sub(@NotNull ChunkPosition position) {
        return sub(position.chunkPosX, position.chunkPosY, position.chunkPosZ);
    }

    public @NotNull Pos sub(@NotNull Vec3 vec) {
        return sub(vec.xCoord, vec.yCoord, vec.zCoord);
    }

    public @NotNull Pos sub(@NotNull ChunkCoordinates coordinates) {
        return sub(coordinates.posX, coordinates.posY, coordinates.posZ);
    }

    public @NotNull Pos sub(@NotNull ForgeDirection direction) {
        return sub(direction, 1);
    }

    public @NotNull Pos sub(@NotNull ForgeDirection direction, double term) {
        return sub(direction.offsetX * term, direction.offsetY * term, direction.offsetZ * term);
    }
    //------------------------------------
    //endregion

    public @NotNull Pos sub(@NotNull Pos pos) {
        return sub(pos.getX(), pos.getY(), pos.getZ());
    }

    public @NotNull Pos sub(@NotNull Triplet<? extends Number, ? extends Number, ? extends Number> triplet) {
        return sub(triplet.getX().doubleValue(), triplet.getY().doubleValue(), triplet.getZ().doubleValue());
    }

    public @NotNull Pos sub(double term) {
        return sub(term, term, term);
    }

    public @NotNull Pos sub(double x, double y, double z) {
        if (x == 0 && y == 0 && z == 0) return this;
        return new Pos(getX() - x, getY() - y, getZ() - z);
    }

    //region Multiple
    //------------------------------------
    public @NotNull Pos mul(@NotNull ChunkPosition position) {
        return mul(position.chunkPosX, position.chunkPosY, position.chunkPosZ);
    }

    public @NotNull Pos mul(@NotNull Vec3 vec) {
        return mul(vec.xCoord, vec.yCoord, vec.zCoord);
    }

    public @NotNull Pos mul(@NotNull ChunkCoordinates coordinates) {
        return mul(coordinates.posX, coordinates.posY, coordinates.posZ);
    }

    public @NotNull Pos mul(@NotNull ForgeDirection direction, double factor) {
        return mul(direction.offsetX == 0 ? 1 : direction.offsetX * factor, direction.offsetY == 0 ? 1 :
            direction.offsetY * factor, direction.offsetZ == 0 ? 1 : direction.offsetZ * factor);
    }

    public @NotNull Pos mul(@NotNull Pos pos) {
        return mul(pos.getX(), pos.getY(), pos.getZ());
    }

    public @NotNull Pos mul(@NotNull Triplet<? extends Number, ? extends Number, ? extends Number> triplet) {
        return mul(triplet.getX().doubleValue(), triplet.getY().doubleValue(), triplet.getZ().doubleValue());
    }

    public @NotNull Pos mul(double factor) {
        return mul(factor, factor, factor);
    }
    //endregion

    public @NotNull Pos mul(double x, double y, double z) {
        return x == 0 && y == 0 && z == 0 ? ORIGIN : x == 1 && y == 1 && z == 1 ? this
            : new Pos(getX() * x, getY() * y, getZ() * z);
    }

    //region Division
    //------------------------------------
    public @NotNull Pos div(@NotNull ChunkPosition position) {
        return div(position.chunkPosX, position.chunkPosY, position.chunkPosZ);
    }

    public @NotNull Pos div(@NotNull Vec3 vec) {
        return div(vec.xCoord, vec.yCoord, vec.zCoord);
    }

    public @NotNull Pos div(@NotNull ChunkCoordinates coordinates) {
        return div(coordinates.posX, coordinates.posY, coordinates.posZ);
    }

    public @NotNull Pos div(@NotNull ForgeDirection direction, double divider) {
        return div(direction.offsetX == 0 ? 1 : direction.offsetX * divider, direction.offsetY == 0 ? 1 :
            direction.offsetY * divider, direction.offsetZ == 0 ? 1 : direction.offsetZ * divider);
    }

    public @NotNull Pos div(@NotNull Pos pos) {
        return div(pos.getX(), pos.getY(), pos.getZ());
    }

    public double distanceQrt(double x, double y, double z) {
        return pow(getX() - x, 2) + pow(getY() - y, 2) + pow(getZ() - z, 2);
    }
    //------------------------------------
    //endregion

    public @NotNull Pos div(@NotNull Triplet<? extends Number, ? extends Number, ? extends Number> triplet) {
        return mul(triplet.getX().doubleValue(), triplet.getY().doubleValue(), triplet.getZ().doubleValue());
    }

    @Contract("_ -> new")
    public @NotNull Pos div(double divider) {
        return div(divider, divider, divider);
    }

    public @NotNull Pos div(double x, double y, double z) {
        return x == 1 && y == 1 && z == 1 ? this : new Pos(getX() / x, getY() / y, getZ() / z);
    }

    //region Math
    //------------------------------------
    public @NotNull Pos floor() {
        val x = getX();
        val y = getY();
        val z = getZ();
        val fX = fLX();
        val fY = fLY();
        val fZ = fLZ();
        return x == fX && y == fY && z == fZ ? this : new Pos(fX, fY, fZ);
    }

    public double length() {
        return sqrt(lengthQrt());
    }

    public double lengthQrt() {
        return pow(getX(), 2) + pow(getY(), 2) + pow(getZ(), 2);
    }

    public @NotNull Pos normalize() {
        double x = getX();
        double y = getY();
        double z = getZ();
        if (x == 0 && y == 0 & z == 0) return ORIGIN;
        val len = length();
        val nX = x / len;
        val nY = y / len;
        val nZ = z / len;
        return new Pos(nX, nY, nZ);
    }
    //------------------------------------
    //endregion

    //region Distance
    //------------------------------------
    public double distance(@NotNull ChunkPosition position) {
        return distance(position.chunkPosX, position.chunkPosY, position.chunkPosZ);
    }

    public double distance(@NotNull Vec3 vec) {
        return distance(vec.xCoord, vec.yCoord, vec.zCoord);
    }

    public double distance(@NotNull ChunkCoordinates coordinates) {
        return distance(coordinates.posX, coordinates.posY, coordinates.posZ);
    }

    public double distance(@NotNull Pos pos) {
        return distance(pos.getX(), pos.getY(), pos.getZ());
    }

    public double distance(@NotNull ForgeDirection direction) {
        return distance(direction.offsetX, direction.offsetY, direction.offsetZ);
    }

    public double distance(@NotNull Triplet<? extends Number, ? extends Number, ? extends Number> triplet) {
        return distance(triplet.getX().doubleValue(), triplet.getY().doubleValue(), triplet.getZ().doubleValue());
    }

    public double distance(double x, double y, double z) {
        return sqrt(distanceQrt(x, y, z));
    }
    //------------------------------------
    //endregion

    //------------------------------------
    //endregion

    //region Square distance
    //------------------------------------
    public double distanceQrt(@NotNull ChunkPosition position) {
        return distanceQrt(position.chunkPosX, position.chunkPosY, position.chunkPosZ);
    }

    public double distanceQrt(@NotNull Vec3 vec) {
        return distanceQrt(vec.xCoord, vec.yCoord, vec.zCoord);
    }

    public double distanceQrt(@NotNull ChunkCoordinates coordinates) {
        return distanceQrt(coordinates.posX, coordinates.posY, coordinates.posZ);
    }

    public double distanceQrt(@NotNull Triplet<? extends Number, ? extends Number, ? extends Number> triplet) {
        return distanceQrt(triplet.getX().doubleValue(), triplet.getY().doubleValue(), triplet.getZ().doubleValue());
    }

    public double distanceQrt(@NotNull ForgeDirection direction) {
        return distanceQrt(direction.offsetX, direction.offsetY, direction.offsetZ);
    }

    public double distanceQrt(@NotNull Pos pos) {
        return distanceQrt(pos.getX(), pos.getY(), pos.getZ());
    }

    //region Scalar
    //------------------------------------
    public double scalar(@NotNull ChunkCoordinates coordinates) {
        return scalar(coordinates.posX, coordinates.posY, coordinates.posZ);
    }

    public double scalar(@NotNull ChunkPosition position) {
        return scalar(position.chunkPosX, position.chunkPosY, position.chunkPosZ);
    }

    public double scalar(@NotNull Vec3 vec) {
        return scalar(vec.xCoord, vec.yCoord, vec.zCoord);
    }

    public double scalar(@NotNull ForgeDirection direction) {
        return scalar(direction.offsetX, direction.offsetY, direction.offsetZ);
    }

    public double scalar(@NotNull Triplet<? extends Number, ? extends Number, ? extends Number> triplet) {
        return scalar(triplet.getX().doubleValue(), triplet.getY().doubleValue(), triplet.getZ().doubleValue());
    }

    public double scalar(@NotNull Pos pos) {
        return scalar(pos.getX(), pos.getY(), pos.getZ());
    }

    public double scalar(double x, double y, double z) {
        return getX() * x + getY() * y + getZ() + z;
    }

    //region Cross
    //------------------------------------
    public @NotNull Pos cross(@NotNull ChunkCoordinates coordinates) {
        return cross(coordinates.posX, coordinates.posY, coordinates.posZ);
    }

    public @NotNull Pos cross(@NotNull ChunkPosition position) {
        return cross(position.chunkPosX, position.chunkPosY, position.chunkPosZ);
    }
    //------------------------------------
    //endregion

    public @NotNull Pos cross(@NotNull Vec3 vec) {
        return cross(vec.xCoord, vec.yCoord, vec.zCoord);
    }

    public @NotNull Pos cross(@NotNull ForgeDirection direction) {
        return cross(direction.offsetX, direction.offsetY, direction.offsetZ);
    }

    public @NotNull Pos cross(@NotNull Triplet<? extends Number, ? extends Number, ? extends Number> triplet) {
        return cross(triplet.getX().doubleValue(), triplet.getY().doubleValue(), triplet.getZ().doubleValue());
    }

    public @NotNull Pos cross(@NotNull Pos pos) {
        return cross(pos.getX(), pos.getY(), pos.getZ());
    }

    public @NotNull Pos cross(double x, double y, double z) {
        double ownX = getX();
        double ownY = getY();
        double ownZ = getZ();
        val cX = ownY * z - ownZ * y;
        val cY = ownZ * x - ownX * z;
        val cZ = ownX * y - ownY * x;
        return new Pos(cX, cY, cZ);
    }

    //region x, y, z conversions
    //------------------------------------
    // TODO: Move this to numeric Unit/Pair/Triplet
    public float fX() {
        return getX().floatValue();
    }

    public float fY() {
        return getY().floatValue();
    }

    public float fZ() {
        return getZ().floatValue();
    }

    public int iX() {
        return getX().intValue();
    }

    public int iY() {
        return getY().intValue();
    }

    public int iZ() {
        return getZ().intValue();
    }

    public byte bX() {
        return getX().byteValue();
    }
    //------------------------------------
    //endregion

    public byte bY() {
        return getY().byteValue();
    }

    public byte bZ() {
        return getZ().byteValue();
    }

    public short sX() {
        return getX().shortValue();
    }

    public short sY() {
        return getY().shortValue();
    }

    public short sZ() {
        return getZ().shortValue();
    }

    public long lX() {
        return getX().longValue();
    }

    public long lY() {
        return getY().longValue();
    }

    public long lZ() {
        return getZ().longValue();
    }

    //region x, y, z floored conversions
    //------------------------------------
    public int fIX() {
        return MathHelper.floor_double(getX());
    }

    public int fIY() {
        return MathHelper.floor_double(getY());
    }
    //------------------------------------
    //endregion

    public int fIZ() {
        return MathHelper.floor_double(getZ());
    }

    public short fSX() {
        return (short) MathHelper.floor_double(getX());
    }

    public short fSY() {
        return (short) MathHelper.floor_double(getY());
    }

    public short fSZ() {
        return (short) MathHelper.floor_double(getZ());
    }

    public byte fBX() {
        return (byte) MathHelper.floor_double(getX());
    }

    public byte fBY() {
        return (byte) MathHelper.floor_double(getY());
    }

    public byte fBZ() {
        return (byte) MathHelper.floor_double(getZ());
    }

    public long fLX() {
        return MathHelper.floor_double_long(getX());
    }

    public long fLY() {
        return MathHelper.floor_double_long(getY());
    }

    public long fLZ() {
        return MathHelper.floor_double_long(getZ());
    }
    //------------------------------------
    //endregion

    //region Type conversions
    //------------------------------------
    @Contract(" -> new")
    public @NotNull ChunkPosition asChunkPosition() {
        return new ChunkPosition(iX(), iY(), iZ());
    }

    @Contract(" -> new")
    public @NotNull Vec3 asVec3() {
        return Vec3.createVectorHelper(getX(), getY(), getZ());
    }

    @Contract(" -> new")
    public @NotNull ChunkCoordinates asChunkCoordinates() {
        return new ChunkCoordinates(iX(), iY(), iZ());
    }

    public @NotNull ForgeDirection asForgeDirection() {
        for (val direction : ForgeDirection.values())
            if (getX() == direction.offsetX && getY() == direction.offsetY && getZ() == direction.offsetZ)
                return direction;
        return ForgeDirection.UNKNOWN;
    }

    @Contract(" -> new")
    public double @NotNull [] asArray() {
        return new double[]{getX(), getY(), getZ()};
    }

    @Contract(" -> new")
    public int @NotNull [] asIntArray() {
        return new int[]{iX(), iY(), iZ()};
    }

    @Contract(" -> new")
    public float @NotNull [] asFloatArray() {
        return new float[]{fX(), fY(), fZ()};
    }
    //------------------------------------
    //endregion

    @Contract(" -> new")
    public byte @NotNull [] asByteArray() {
        return new byte[]{bX(), bY(), bZ()};
    }

    @Contract(" -> new")
    public long @NotNull [] asLongArray() {
        return new long[]{lX(), lY(), lZ()};
    }

    @Contract(" -> new")
    public short @NotNull [] asShortArray() {
        return new short[]{sX(), sY(), sZ()};
    }

    //region Type floored conversions
    //------------------------------------
    @Contract(" -> new")
    public @NotNull ChunkPosition asFlooredChunkPosition() {
        return new ChunkPosition(fIX(), fIY(), fIZ());
    }

    @Contract(" -> new")
    public @NotNull Vec3 asFlooredVec3() {
        return Vec3.createVectorHelper(fIX(), fIY(), fIZ());
    }

    @Contract(" -> new")
    public @NotNull ChunkCoordinates asFlooredChunkCoordinates() {
        return new ChunkCoordinates(fIX(), fIY(), fIZ());
    }

    public @NotNull ForgeDirection asFlooredForgeDirection() {
        for (val direction : ForgeDirection.values())
            if (fIX() == direction.offsetX && fIY() == direction.offsetY && fIZ() == direction.offsetZ)
                return direction;
        return ForgeDirection.UNKNOWN;
    }
    //------------------------------------
    //endregion

    @Contract(" -> new")
    public double[] asFlooredArray() {
        return new double[]{fLX(), fLY(), fLZ()};
    }

    @Contract(" -> new")
    public int[] asFlooredIntArray() {
        return new int[]{fIX(), fIY(), fIZ()};
    }

    @Contract(" -> new")
    public float[] asFlooredFloatArray() {
        return new float[]{fIX(), fIY(), fIZ()};
    }

    @Contract(" -> new")
    public byte[] asFlooredByteArray() {
        return new byte[]{(byte) fIX(), (byte) fIY(), (byte) fIZ()};
    }

    @Contract(" -> new")
    public long[] asFlooredLongArray() {
        return new long[]{fLX(), fLY(), fLZ()};
    }

    @Contract(" -> new")
    public short[] asFlooredShortArray() {
        return new short[]{(short) fIX(), (short) fIY(), (short) fIZ()};
    }

    //region Relative AABB creation
    //------------------------------------
    @Contract("_ -> new")
    public @NotNull AxisAlignedBB createAABB(@NotNull ChunkPosition position) {
        return createAABB(position.chunkPosX, position.chunkPosY, position.chunkPosZ);
    }
    //------------------------------------
    //endregion

    @Contract("_ -> new")
    public @NotNull AxisAlignedBB createAABB(@NotNull Vec3 vec) {
        return createAABB(vec.xCoord, vec.yCoord, vec.zCoord);
    }

    @Contract("_ -> new")
    public @NotNull AxisAlignedBB createAABB(@NotNull ChunkCoordinates coordinates) {
        return createAABB(coordinates.posX, coordinates.posY, coordinates.posZ);
    }

    @Contract("_ -> new")
    public @NotNull AxisAlignedBB createAABB(@NotNull ForgeDirection direction) {
        return createAABB(direction.offsetX, direction.offsetY, direction.offsetZ);
    }

    @Contract("_ -> new")
    public @NotNull AxisAlignedBB createAABB(@NotNull Pos pos) {
        return createAABB(pos.getX(), pos.getY(), pos.getZ());
    }

    @Contract("_ -> new")
    public @NotNull AxisAlignedBB createAABB(
        @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> triplet) {
        return createAABB(triplet.getX().doubleValue(), triplet.getY().doubleValue(), triplet.getZ().doubleValue());
    }

    @Contract("_, _, _ -> new")
    public @NotNull AxisAlignedBB createAABB(double x, double y, double z) {
        // Unboxing
        double ownX = getX();
        double ownY = getY();
        double ownZ = getZ();
        return AxisAlignedBB.getBoundingBox(min(ownX, ownX + x), min(ownY, ownY + y), min(ownZ, ownZ + z),
            max(ownX, ownX + x), max(ownY, ownY + y), max(ownZ, ownZ + z));
    }

    //region Absolute AABB creation
    //------------------------------------
    @Contract("_ -> new")
    public @NotNull AxisAlignedBB createAABBAbsolute(@NotNull ChunkPosition position) {
        return createAABB(position.chunkPosX, position.chunkPosY, position.chunkPosZ);
    }

    @Contract("_ -> new")
    public @NotNull AxisAlignedBB createAABBAbsolute(@NotNull Vec3 vec) {
        return createAABB(vec.xCoord, vec.yCoord, vec.zCoord);
    }
    //------------------------------------
    //endregion

    @Contract("_ -> new")
    public @NotNull AxisAlignedBB createAABBAbsolute(@NotNull ChunkCoordinates coordinates) {
        return createAABB(coordinates.posX, coordinates.posY, coordinates.posZ);
    }

    @Contract("_ -> new")
    public @NotNull AxisAlignedBB createAABBAbsolute(@NotNull ForgeDirection direction) {
        return createAABB(direction.offsetX, direction.offsetY, direction.offsetZ);
    }

    @Contract("_ -> new")
    public @NotNull AxisAlignedBB createAABBAbsolute(@NotNull Pos pos) {
        return createAABB(pos.getX(), pos.getY(), pos.getZ());
    }

    @Contract("_ -> new")
    public @NotNull AxisAlignedBB createAABBAbsolute(
        @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> triplet) {
        return createAABBAbsolute(triplet.getX().doubleValue(), triplet.getY().doubleValue(),
            triplet.getZ().doubleValue());
    }

    @Contract("_, _, _ -> new")
    public @NotNull AxisAlignedBB createAABBAbsolute(double x, double y, double z) {
        return AxisAlignedBB.getBoundingBox(min(getX(), x), min(getY(), y), min(getZ(), z), max(getX(), x),
            max(getY(), y), max(getZ(), z));
    }

    //region Box iterates
    //------------------------------------
    @Contract("_ -> new")
    public @NotNull Iterable<Mutable> iterateBox(@NotNull ChunkPosition position) {
        return iterateBox(position.chunkPosX, position.chunkPosY, position.chunkPosZ);
    }

    @Contract("_ -> new")
    public @NotNull Iterable<Mutable> iterateBox(@NotNull ChunkCoordinates coordinates) {
        return iterateBox(coordinates.posX, coordinates.posY, coordinates.posZ);
    }

    @Override
    public String toString() {
        return "Pos(x = " + getX() + ", y = " + getY() + ", z = " + getZ() + ")";
    }
    //------------------------------------
    //endregion

    @Contract("_ -> new")
    public @NotNull Iterable<Mutable> iterateBox(@NotNull Vec3 vec) {
        return iterateBox((int) vec.xCoord, (int) vec.yCoord, (int) vec.zCoord);
    }

    @Contract("_ -> new")
    public @NotNull Iterable<Mutable> iterateBox(@NotNull ForgeDirection direction) {
        return iterateBox(direction.offsetX, direction.offsetY, direction.offsetZ);
    }

    @Contract("_ -> new")
    public @NotNull Iterable<Mutable> iterateBox(
        @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> triplet) {
        return iterateBox(triplet.getX().intValue(), triplet.getY().intValue(), triplet.getZ().intValue());
    }

    @Contract("_ -> new")
    public @NotNull Iterable<Mutable> iterateBox(@NotNull Pos pos) {
        return iterateBox(pos.iX(), pos.iY(), pos.iZ());
    }

    @Contract("_, _, _ -> new")
    public @NotNull Iterable<Mutable> iterateBox(int oppositeCornerX, int oppositeCornerY, int oppositeCornerZ) {
        val minX = min(iX(), oppositeCornerX);
        val minY = min(iY(), oppositeCornerY);
        val minZ = min(iZ(), oppositeCornerZ);
        val maxX = max(iX(), oppositeCornerX);
        val maxY = max(iY(), oppositeCornerY);
        val maxZ = max(iZ(), oppositeCornerZ);
        val width = maxX - minX;
        val height = maxY - minY;
        val length = maxZ - minZ;
        val size = width * height * length;
        return () -> new AbstractIterator<>() {
            private final @NotNull Mutable temp = new Mutable();
            private int index;

            @Override
            protected Mutable computeNext() {
                if (index == size) return endOfData();
                val x = index % width;
                val y = (index / width) % height;
                val z = index / width / height;
                index++;
                return temp.set(minX + x, minY + y, minZ + z);
            }
        };
    }

    //region Box streams
    //------------------------------------
    @Contract("_ -> new")
    public @NotNull Stream<Mutable> streamBox(@NotNull ChunkPosition position) {
        return streamBox(position.chunkPosX, position.chunkPosY, position.chunkPosZ);
    }

    @Contract("_ -> new")
    public @NotNull Stream<Mutable> streamBox(@NotNull ChunkCoordinates coordinates) {
        return streamBox(coordinates.posX, coordinates.posY, coordinates.posZ);
    }

    @Contract("_ -> new")
    public @NotNull Stream<Mutable> streamBox(@NotNull Vec3 vec) {
        return streamBox((int) vec.xCoord, (int) vec.yCoord, (int) vec.zCoord);
    }

    @Contract("_ -> new")
    public @NotNull Stream<Mutable> streamBox(@NotNull ForgeDirection direction) {
        return streamBox(direction.offsetX, direction.offsetY, direction.offsetZ);
    }

    @Contract("_ -> new")
    public @NotNull Stream<Mutable> streamBox(
        @NotNull Triplet<? extends Number, ? extends Number, ? extends Number> triplet) {
        return streamBox(triplet.getX().intValue(), triplet.getY().intValue(), triplet.getZ().intValue());
    }

    @Contract("_ -> new")
    public @NotNull Stream<Mutable> streamBox(@NotNull Pos pos) {
        return streamBox(pos.iX(), pos.iY(), pos.iZ());
    }

    @Contract("_, _, _ -> new")
    public @NotNull Stream<Mutable> streamBox(int oppositeCornerX, int oppositeCornerY, int oppositeCornerZ) {
        return StreamSupport.stream(iterateBox(oppositeCornerX, oppositeCornerY, oppositeCornerZ).spliterator(), false);
    }
    //------------------------------------
    //endregion

    @Contract("_, _ -> new")
    public @NotNull Stream<Pos> streamBox(@NotNull Pos oppositeCorner, double step) {
        val builder = Stream.<Pos>builder();
        val stopX = Math.max(getX(), oppositeCorner.getX()) + 1;
        val stopY = Math.max(getY(), oppositeCorner.getY()) + 1;
        val stopZ = Math.max(getZ(), oppositeCorner.getZ()) + 1;
        for (var x = Math.min(getX(), oppositeCorner.getX()); x < stopX; x += step)
            for (var y = Math.min(getY(), oppositeCorner.getY()); y < stopY; y += step)
                for (var z = Math.min(getZ(), oppositeCorner.getZ()); z < stopZ; z += step)
                    builder.accept(new Pos(x, y, z));
        return builder.build();
    }

    //region Object utility
    //------------------------------------
    @Contract(" -> new")
    public @NotNull Stream<Pos> stream() {
        return Stream.of(this);
    }

    public void forEachBox(@NotNull Pos oppositeCorner, @NotNull Consumer<Pos> action) {
        val temp = new Mutable();
        val stopX = Math.max(iX(), oppositeCorner.iX()) + 1;
        val stopY = Math.max(iY(), oppositeCorner.iY()) + 1;
        val stopZ = Math.max(iZ(), oppositeCorner.iZ()) + 1;
        for (var x = Math.min(iX(), oppositeCorner.iX()); x < stopX; x++)
            for (var y = Math.min(iY(), oppositeCorner.iY()); y < stopY; y++)
                for (var z = Math.min(iZ(), oppositeCorner.iZ()); z < stopZ; z++) {
                    temp.set(x, y, z);
                    action.accept(temp);
                }
    }

    public boolean isInsideOfBox(@NotNull AxisAlignedBB aabb) {
        return getX() > aabb.minX && getX() < aabb.maxX && getY() > aabb.minY && getY() < aabb.maxY
            && getZ() > aabb.minZ && getZ() < aabb.maxZ;
    }

    @Contract(" -> new")
    public @NotNull Mutable mutableCopy() {
        return new Mutable(getX(), getY(), getZ());
    }

    @Contract(" -> this")
    public @NotNull Pos toImmutable() {
        return immutable ? this : new Pos(getX(), getY(), getZ());
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
        } else if (o instanceof Triplet<?, ?, ?> triplet && triplet.getX() instanceof Number numberX &&
            triplet.getY() instanceof Number numberY && triplet.getZ() instanceof Number numberZ) {
            x = numberX.doubleValue();
            y = numberY.doubleValue();
            z = numberZ.doubleValue();
        } else return 0;
        return getY() == y ? (getZ() == z ? Double.compare(getX(), x) : Double.compare(getZ(), z)) :
            Double.compare(getY(), y);
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (o == null) return false;
        else if (o == this) return true;
        else if (o instanceof Pos pos)
            return getX().equals(pos.getX()) && getY().equals(pos.getY()) && getZ().equals(pos.getZ());
        else if (o instanceof ChunkCoordinates coordinates)
            return iX() == coordinates.posX && iY() == coordinates.posY && iZ() == coordinates.posZ;
        else if (o instanceof ChunkPosition position)
            return iX() == position.chunkPosX && iY() == position.chunkPosY && iZ() == position.chunkPosZ;
        else if (o instanceof Vec3 vec)
            return getX() == vec.xCoord && getY() == vec.yCoord && getZ() == vec.zCoord;
        else if (o instanceof ForgeDirection direction)
            return iX() == direction.offsetX && iY() == direction.offsetY && iZ() == direction.offsetZ;
        else return super.equals(o);
    }

    //region Immutable ban
    //------------------------------------
    @Override
    public void setX(@Nullable Double x) {
        if (immutable) throw new IllegalArgumentException("This Pos is immutable");
        else super.setX(x);
    }

    @Override
    public void setY(@Nullable Double y) {
        if (immutable) throw new IllegalArgumentException("This Pos is immutable");
        else super.setY(y);
    }

    @Override
    public void setZ(@Nullable Double z) {
        if (immutable) throw new IllegalArgumentException("This Pos is immutable");
        else super.setZ(z);
    }
    //------------------------------------
    //endregion

    public static class Mutable extends Pos {
        protected Mutable() {
            super();
        }

        protected Mutable(double x, double y, double z) {
            super(x, y, z);
            immutable = false;
        }

        @Contract("_, _ -> this")
        public @NotNull Mutable set(double x, double y) {
            set((Double) x, (Double) y);
            return this;
        }

        @Contract("_, _, _ -> this")
        public @NotNull Mutable set(double x, double y, double z) {
            set((Double) x, (Double) y, (Double) z);
            return this;
        }

        @Contract("_ -> this")
        public @NotNull Mutable set(@NotNull Vec3 vec) {
            return set(vec.xCoord, vec.yCoord, vec.zCoord);
        }

        @Contract("_ -> this")
        public @NotNull Mutable set(@NotNull ForgeDirection direction) {
            return set(direction.offsetX, direction.offsetY, direction.offsetZ);
        }

        @Contract("_ -> this")
        public @NotNull Mutable set(@NotNull ChunkCoordinates coordinates) {
            return set(coordinates.posX, coordinates.posY, coordinates.posZ);
        }

        @Contract("_ -> this")
        public @NotNull Mutable set(@NotNull ChunkPosition position) {
            return set(position.chunkPosX, position.chunkPosY, position.chunkPosZ);
        }

        @Contract("_ -> this")
        public @NotNull Mutable set(@NotNull Triplet<? extends Number, ? extends Number, ? extends Number> triplet) {
            return set(triplet.getX().doubleValue(), triplet.getY().doubleValue(), triplet.getZ().doubleValue());
        }

        @Contract("_ -> this")
        public @NotNull Mutable set(@NotNull Pos pos) {
            return set(pos.getX().doubleValue(), pos.getY().doubleValue(), pos.getZ().doubleValue());
        }

        @Override
        public @NotNull Pos add(double x, double y, double z) {
            return super.add(x, y, z).toImmutable();
        }

        @Override
        public @NotNull Pos sub(double x, double y, double z) {
            return super.sub(x, y, z).toImmutable();
        }

        @Override
        public @NotNull Pos mul(double x, double y, double z) {
            return super.mul(x, y, z).toImmutable();
        }

        @Override
        public @NotNull Pos div(double x, double y, double z) {
            return super.div(x, y, z).toImmutable();
        }

        @Override
        public @NotNull Pos floor() {
            return super.floor().toImmutable();
        }

        @Override
        public @NotNull Pos normalize() {
            return super.normalize().toImmutable();
        }
    }
}
