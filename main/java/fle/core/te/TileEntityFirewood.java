package fle.core.te;

import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import fle.FLE;
import fle.api.energy.IThermalTileEntity;
import fle.api.world.BlockPos;
import fle.core.init.IB;
import fle.core.init.Materials;
import fle.core.net.FlePackets.CoderNBTUpdate;
import fle.core.te.base.TEBase;

public class TileEntityFirewood extends TEBase implements IThermalTileEntity
{
	private final double sh = Materials.HardWood.getPropertyInfo().getSpecificHeat();
	private final double hc = Materials.HardWood.getPropertyInfo().getThermalConductivity();
	
	private boolean isCoal;
	private int woodContain;
	private int coalLevel;
	private double heatCurrect;
	private byte burnState = 0;//0 for level.1 ; 1 for level.2 ; 2 for level.3.
	
	public TileEntityFirewood()
	{
		
	}
	
	public TileEntityFirewood(boolean aCoal)
	{
		isCoal = aCoal;
		woodContain = aCoal ? 4000 : 5000;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		woodContain = nbt.getInteger("WoodContain");
		coalLevel = nbt.getShort("CoalLevel");
		heatCurrect = nbt.getDouble("HeatCurrect");
		burnState = nbt.getByte("BurnState");
		isCoal = nbt.getBoolean("IsCoal");
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setInteger("WoodContain", woodContain);
		nbt.setShort("CoalLevel", (short) coalLevel);
		nbt.setDouble("HeatCurrect", heatCurrect);
		nbt.setByte("BurnState", burnState);
		nbt.setBoolean("IsCoal", isCoal);
	}
	
	@Override
	public void updateEntity()
	{
		boolean flag = false;
		if(burnState == 0 && checkingFire())
		{
			burnState = 1;
			flag = true;
		}
		if(burnState > 0)
		{
			if(coalLevel < 10000)
			{
				++coalLevel;
			}
			else if(!isCoal)
			{
				worldObj.setBlock(xCoord, yCoord, zCoord, IB.charcoal, 0, 2);
				woodContain = 4000;
				coalLevel = 20000;
				if(!canBBurning())
					burnState = 0;
				flag = true;
			}
			if(!canABurning()) burnState = 0;
			else if(canBBurning()) burnState = 2;
		}
		if(burnState == 2)
		{
			--woodContain;
			heatCurrect += isCoal ? 6.0F : 5.0F;
			FLE.fle.getThermalNet().emmitHeat(getBlockPos());
			if(!canBBurning()) burnState = 1;
			if((getBlockPos().toPos(ForgeDirection.UP).getBlock().isFlammable(worldObj, xCoord, yCoord + 1, zCoord, ForgeDirection.UP) || getBlockPos().toPos(ForgeDirection.UP).isReplacable()))
			{
				if(worldObj.getTileEntity(xCoord, yCoord + 1, zCoord) != null)
				{
					worldObj.removeTileEntity(xCoord, yCoord + 1, zCoord);
				}
				worldObj.setBlock(xCoord, yCoord + 1, zCoord, Blocks.fire);
			}
		}
		if(woodContain <= 0)
		{
			worldObj.setBlock(xCoord, yCoord, zCoord, IB.ash);
			worldObj.removeTileEntity(xCoord, yCoord, zCoord);
			return;
		}
		if(flag)
		{
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		}
		if(!worldObj.isRemote)
		{
			FLE.fle.getNetworkHandler().sendTo(new CoderNBTUpdate(this));
		}
	}
	
	public boolean canABurning()
	{
		int blockCount = 0;
		BlockPos tPos = getBlockPos();
		for(int i = -1; i <= 1; ++i)
			for(int j = -1; j <= 1; ++j)
				for(int k = -1; k <= 1; ++k)
				{
					if(!tPos.toPos(i, j, k).isAir())
					{
						++blockCount;
					}
				}
		if(isCatchRain()) return false;
		return blockCount != 27;
	}
	
	public boolean canBBurning()
	{
		int blockCount = 0;
		for(int i = 0; i < ForgeDirection.VALID_DIRECTIONS.length; ++i)
		{
			ForgeDirection dir = ForgeDirection.VALID_DIRECTIONS[i];
			if(!getBlockPos().toPos(dir).isAir())
			{
				++blockCount;
			}
		}
		return blockCount != 6 && burnState > 0;
	}
	
	public boolean checkingFire()
	{
		for(int i = 0; i < ForgeDirection.VALID_DIRECTIONS.length; ++i)
		{
			ForgeDirection dir = ForgeDirection.VALID_DIRECTIONS[i];
			if(getBlockPos().toPos(dir).getBlock() == Blocks.fire || getBlockPos().toPos(dir).getBlock().isBurning(worldObj, xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord + dir.offsetZ))
			{
				return true;
			}
		}
		return false;
	}
	
	@Override
	public int getTemperature(ForgeDirection dir)
	{
		return (int) (FLE.fle.getThermalNet().getEnvironmentTemperature(getBlockPos()) + heatCurrect / sh);
	}

	@Override
	public double getThermalConductivity(ForgeDirection dir)
	{
		return hc;
	}

	@Override
	public double getThermalEnergyCurrect(ForgeDirection dir)
	{
		return heatCurrect;
	}

	@Override
	public void onHeatReceive(ForgeDirection dir, double heatValue)
	{
		heatCurrect += heatValue;
	}

	@Override
	public void onHeatEmmit(ForgeDirection dir, double heatValue)
	{
		heatCurrect -= heatValue;
	}


	public void setBurning()
	{
		burnState = 1;
	}
	
	public boolean isBurning()
	{
		return burnState == 2;
	}

	public int getCharcoalContain()
	{
		return (int) Math.floor(woodContain / 1250);
	}

	public boolean isSmoking()
	{
		return burnState > 0;
	}
}