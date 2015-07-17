package fla.core.tileentity;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import fla.api.block.IFacing;
import fla.api.world.BlockPos;

public abstract class TileEntityBaseWithFacing extends TileEntityBase implements IFacing
{
	public TileEntityBaseWithFacing setFacing(ForgeDirection dir)
	{
		try
		{
			TileEntityBaseWithFacing tile = this.getClass().newInstance();
			tile.dir = dir;
			return tile;
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return this;
		}
	}
	
	protected ForgeDirection dir = ForgeDirection.UNKNOWN;

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		dir = ForgeDirection.values()[nbt.getInteger("BlockDirection")];
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setInteger("BlockDirection", dir.ordinal());
	}
	
	@Override
	public ForgeDirection getBlockDirection(BlockPos pos) 
	{
		return dir;
	}

	@Override
	public abstract boolean canSetDirection(World world, BlockPos pos);

	@Override
	public abstract boolean canSetDirectionWith(World world, BlockPos pos, double xPos,
			double yPos, double zPos, ItemStack itemstack);

	@Override
	public abstract ForgeDirection setDirectionWith(World world, BlockPos pos,
			double xPos, double yPos, double zPos, ItemStack itemstack);

	@Override
	public boolean hasTopOrDownState(World world, BlockPos pos) 
	{
		return false;
	}

}
