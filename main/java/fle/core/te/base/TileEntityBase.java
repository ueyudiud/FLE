package fle.core.te.base;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import fle.api.block.IFacingBlock;
import fle.api.te.ITEInWorld;
import fle.api.world.BlockPos;

public class TileEntityBase extends TileEntity implements ITEInWorld, IFacingBlock
{
	private ForgeDirection dir;
	
	public void setDirction(ForgeDirection aDirection)
	{
		dir = aDirection;
	}
	
	public TileEntityBase() 
	{
		
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		dir = ForgeDirection.values()[nbt.getByte("BlockFacing")];
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setByte("BlockFacing", (byte) (dir == null ? ForgeDirection.UNKNOWN.ordinal() : dir.ordinal()));
	}
	
	@Override
	public TileEntity getTileEntity() 
	{
		return this;
	}

	@Override
	public int getLightValue() 
	{
		return (int) (16 * worldObj.getLightBrightness(xCoord, yCoord, zCoord));
	}

	@Override
	public boolean isRedStoneEmmit() 
	{
		return worldObj.getBlockPowerInput(xCoord, yCoord, zCoord) != 0;
	}

	@Override
	public World getWorldObj() 
	{
		return worldObj;
	}

	@Override
	public BlockPos getBlockPos() 
	{
		return new BlockPos(worldObj, xCoord, yCoord, zCoord);
	}

	
	@Override
	public ForgeDirection getDirction(BlockPos pos) 
	{
		return dir;
	}
	

	@Override
	public boolean canSetDirection(BlockPos pos, ItemStack tool, float xPos,
			float yPos, float zPos) 
	{
		return false;
	}
	

	@Override
	public void setDirection(World world, BlockPos pos, ItemStack tool,
			float xPos, float yPos, float zPos) 
	{
		
	}
}