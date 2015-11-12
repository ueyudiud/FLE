package fle.core.te;

import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import fle.FLE;
import fle.api.energy.IThermalTileEntity;
import fle.api.material.IAtoms.EnumCountLevel;
import fle.api.material.Matter;
import fle.api.te.TEBase;
import fle.api.world.BlockPos;
import fle.core.energy.ThermalTileHelper;
import fle.core.init.Config;
import fle.core.init.IB;
import fle.core.init.Materials;
import fle.core.net.FleTEPacket;

public class TileEntityFirewood extends TEBase implements IThermalTileEntity
{
	private final int charcoalPower = Config.getInteger("pCharcoal", 800000);
	private final int firewoodPower = Config.getInteger("pFirewood", 500000);
	
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
		woodContain = aCoal ? 5000 : 6000;
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
		super.updateEntity();
		FLE.fle.getThermalNet().emmitHeat(getBlockPos());
		boolean flag = false;
		if(burnState == 0 && checkingFire())
		{
			setBurning();
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
				woodContain = 5000;
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
			heatCurrect.reseaveHeat((isCoal ? charcoalPower : firewoodPower) * FLE.fle.getAirConditionProvider().getAirLevel(getBlockPos()).getIonContain(EnumCountLevel.Matter, Matter.mO2));
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
			markNBTUpdate();
			markForUpdate();
		}
		heatCurrect.update();
	}
	
	public boolean canABurning()
	{
		int blockCount = 0;
		BlockPos tPos = getBlockPos();
		for(int i = -1; i <= 1; ++i)
			for(int j = -1; j <= 1; ++j)
				for(int k = -1; k <= 1; ++k)
				{
					if(!tPos.toPos(i, j, k).isAir() && tPos.toPos(i, j, k).getBlock().getMaterial() != Material.fire)
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
			if(!getBlockPos().toPos(dir).isAir() && getBlockPos().toPos(dir).getBlock().getMaterial() != Material.fire)
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
		if(!worldObj.isRemote)
		{
			burnState = 1;

			sendToNearBy(new FleTEPacket(this, (byte) 0), 128.0F);
		}
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

	@Override
	public double getPreHeatEmit()
	{
		return heatCurrect.getPreHeatEmit();
	}
	
	@Override
	public Object onEmmit(byte aType)
	{
		return aType == 0 ? burnState : null;
	}
	
	@Override
	public void onReseave(byte type, Object contain)
	{
		if(type == 0)
			burnState = (Byte) contain;
	}
}