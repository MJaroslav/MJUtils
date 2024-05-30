package io.github.mjaroslav.mjutils.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.github.mjaroslav.sharedjava.tuple.Triplet;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@NoArgsConstructor
@AllArgsConstructor
public class PositionalPacket<REQ extends IMessage> extends AbstractPacket<REQ> {
    protected double x, y, z;

    public PositionalPacket(@NotNull Triplet<? extends Number, ? extends Number, ? extends Number> pos) {
        this(pos.getX().doubleValue(), pos.getY().doubleValue(), pos.getZ().doubleValue());
    }

    @Override
    public void fromBytes(@NotNull ByteBuf buf) {
        x = buf.readDouble();
        y = buf.readDouble();
        z = buf.readDouble();
    }

    @Override
    public void toBytes(@NotNull ByteBuf buf) {
        buf.writeDouble(x);
        buf.writeDouble(y);
        buf.writeDouble(z);
    }
}
