package fle.core.te;

import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import fle.FLE;
import fle.api.energy.IThermalTileEntity;
import fle.api.enums.EnumAtoms;
import fle.api.material.Matter;
import fle.api.net.FlePackets.CoderNBTUpdate;
import fle.api.te.TEBase;
import fle.api.world.BlockPos;
import fle.core.energy.ThermalTileHelper;
import fle.core.init.Config;
import fle.core.init.IB;
import fle.core.init.Materials;

public class TileEntityFirewood extends TEBase implements IThermalTileEntity
{
	private final int charcoalPower = Config.getInteger("pCharcoal", 6000);
	private final int firewoodPower = Config.getInteger("pFirewood", 4000);
	
	private boolean isCoal;
	private int woodContain;
	private int coalLevel;
	private ThermalTileHelper heatCurrect;
	private byte burnState = 0;//0 for level.1 ; 1 for level.2 ; 2 for level.3.
	
	public TileEntityFirewood()
	{
		heatCurrect = new ThermalTileHelper(Materials.HardWood);
	}
	
	public TileEntityFirewood(boolean aCoal)
	{
		isCoal = aCoal;
		woodContain = aCoal ? 4000 : 5000;
		heatCurrect = new ThermalTileHelper(!aCoal ? Materials.HardWood : Materials.Charcoal);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		heatCurrect = new ThermalTileHelper(!isCoal ? Materials.HardWood : Materials.Charcoal);
		woodContain = nbt.getInteger("WoodContain");
		coalLevel = nbt.getShort("CoalLevel");
		heatCurrect.readFromNBT(nbt);
		burnState = nbt.getByte("BurnState");
		isCoal = nbt.getBoolean("IsCoal");
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setInteger("WoodContain", woodContain);
		nbt.setShort("CoalLevel", (short) coalLevel);
		heatCurrect.writeToNBT(nbt);
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
			heatCurrect.reseaveHeat((isCoal ? charcoalPower : firewoodPower) * FLE.fle.getAirConditionProvider().getAirLevel(getBlockPos()).getIconContain(EnumAtoms.O));
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
		return (int) (FLE.fle.getThermalNet().getEnvironmentTemperature(getBlockPos()) + heatCurrect.getTempreture());
	}

	@Override
	public double getThermalConductivity(ForgeDirection dir)
	{
		return heatCurrect.getThermalConductivity();
	}

	@Override
	public double getThermalEnergyCurrect(ForgeDirection dir)
	{
		return heatCurrect.getHeat();
	}

	@Override
	public void onHeatReceive(ForgeDirection dir, double heatValue)
	{
		heatCurrect.reseaveHeat(heatValue);
	}

	@Override
	public void onHeatEmit(ForgeDirection dir, double heatValue)
	{
		heatCurrect.emitHeat(heatValue);
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