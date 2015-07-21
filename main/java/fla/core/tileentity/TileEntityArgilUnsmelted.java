package fla.core.tileentity;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import fla.api.energy.heat.IHeatTileEntity;
import fla.api.util.InfoBuilder;
import fla.api.world.BlockPos;
import fla.core.FlaBlocks;

public class TileEntityArgilUnsmelted extends TileEntityBase implements IHeatTileEntity
{
	private int smeltedTick;

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		
		smeltedTick = nbt.getInteger("SmeltedTick");
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		
		nbt.setInteger("SmeltedTick", smeltedTick);
	}
	
	@Override
	public void updateEntity() 
	{
		if(smeltedTick > 10000)
		{
			worldObj.removeTileEntity(xCoord, yCoord, zCoord);
			invalidate();
			worldObj.setBlock(xCoord, yCoord, zCoord, FlaBlocks.argil_smelted);
			worldObj.setTileEntity(xCoord, yCoord, zCoord, FlaBlocks.argil_smelted.createNewTileEntity(worldObj, worldObj.getBlockMetadata(xCoord, yCoord, zCoord)));
		}
	}
	
	@Override
	public InfoBuilder<BlockPos> getInformation() 
	{
		return new ArgilInfo();
	}

	@Override
	public boolean canEmmit(ForgeDirection d, int pkg) 
	{
		return false;
	}

	@Override
	public boolean canReseave(ForgeDirection d, int pkg) 
	{
		return true;
	}

	@Override
	public int getHeatCorrect(ForgeDirection d) 
	{
		return smeltedTick;
	}

	@Override
	public void catchHeat(ForgeDirection d, int pkg) 
	{
		if(pkg > 50) ++smeltedTick;
	}
	
	public class ArgilInfo implements InfoBuilder<BlockPos>
	{
		@Override
		public List<String> getInfo(BlockPos t) 
		{
			StringBuilder sb = new StringBuilder();
			List<String> ret = new ArrayList();
			ret.add(sb.append("Progress :").append(TileEntityArgilUnsmelted.this.smeltedTick).toString());
			return ret;
		}	
	}
}