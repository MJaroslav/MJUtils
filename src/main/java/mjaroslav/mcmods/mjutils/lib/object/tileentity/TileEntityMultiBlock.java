package mjaroslav.mcmods.mjutils.lib.object.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

/**
 * WIP
 *
 * @author MJaroslav
 */
public abstract class TileEntityMultiBlock extends TileEntity {
    public static final String CAN_RENDER_NAME = "can_render";
    public static final String IS_READY_NAME = "is_ready";
    public boolean canRender = false;
    public boolean isReady = false;

    @Override
    public void updateEntity() {
        if (canForm() && !this.isReady)
            form();
    }

    @Override
    public boolean canUpdate() {
        return true;
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        writeToNBTS(nbt);
    }

    public void writeToNBTS(NBTTagCompound nbt) {
        nbt.setBoolean(CAN_RENDER_NAME, this.canRender);
        nbt.setBoolean(IS_READY_NAME, this.isReady);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        readFromNBTS(nbt);
    }

    public void readFromNBTS(NBTTagCompound nbt) {
        this.canRender = nbt.getBoolean(CAN_RENDER_NAME);
        this.isReady = nbt.getBoolean(IS_READY_NAME);
    }

    ;

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound syncData = new NBTTagCompound();
        this.writeToNBTS(syncData);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, syncData);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        this.readFromNBTS(pkt.func_148857_g());
    }

    public abstract boolean canRender();

    public abstract boolean canForm();

    public abstract void form();
}
