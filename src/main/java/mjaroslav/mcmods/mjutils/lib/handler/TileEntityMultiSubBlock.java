package mjaroslav.mcmods.mjutils.lib.handler;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
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
public class TileEntityMultiSubBlock extends TileEntity {
    public static final String MASTER_X_NAME = "master_x";
    public static final String MASTER_Y_NAME = "master_y";
    public static final String MASTER_Z_NAME = "master_z";
    public static final String HAS_MASTER_NAME = "has_master";
    public static final String REPLACED_BLOCK_NAME = "replaced_block";
    public int masterX = this.xCoord;
    public int masterY = this.yCoord;
    public int masterZ = this.zCoord;
    public boolean hasMaster = false;
    public ItemStack replacedBlock = new ItemStack(Blocks.air, 1, 0);

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        writeToNBTS(nbt);
    }

    public void writeToNBTS(NBTTagCompound nbt) {
        nbt.setBoolean(HAS_MASTER_NAME, this.hasMaster);
        nbt.setInteger(MASTER_X_NAME, this.masterX);
        nbt.setInteger(MASTER_Y_NAME, this.masterY);
        nbt.setInteger(MASTER_Z_NAME, this.masterZ);
        NBTTagCompound item = new NBTTagCompound();
        this.replacedBlock.writeToNBT(item);
        nbt.setTag(REPLACED_BLOCK_NAME, item);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        readFromNBTS(nbt);
    }

    public void readFromNBTS(NBTTagCompound nbt) {
        this.hasMaster = nbt.getBoolean(HAS_MASTER_NAME);
        this.masterX = nbt.getInteger(MASTER_X_NAME);
        this.masterY = nbt.getInteger(MASTER_Y_NAME);
        this.masterZ = nbt.getInteger(MASTER_Z_NAME);
        this.replacedBlock = ItemStack.loadItemStackFromNBT(nbt.getCompoundTag(REPLACED_BLOCK_NAME));
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

    public void replaceBlock() {
        if (!this.worldObj.isRemote && this.replacedBlock != null && this.replacedBlock.getItem() != null
                && Block.getBlockFromItem(this.replacedBlock.getItem()) != null) {
            Block block = Block.getBlockFromItem(this.replacedBlock.getItem());
            int meta = this.replacedBlock.getItemDamage();
            this.worldObj.setBlockToAir(xCoord, yCoord, zCoord);
            this.worldObj.setBlock(xCoord, yCoord, zCoord, block, meta, 3);
            this.worldObj.removeTileEntity(xCoord, yCoord, zCoord);
        }
    }
}
