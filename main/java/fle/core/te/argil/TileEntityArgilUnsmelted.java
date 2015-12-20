package fle.core.te.argil;

import static fle.core.handler.FuelHandler.g;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import flapi.energy.IThermalTileEntity;
import flapi.te.TEBase;
import fle.FLE;
import fle.core.energy.ThermalTileHelper;
import fle.core.init.IB;
import fle.core.init.Materials;

public class TileEntityArgilUnsmelted extends TEBase implements IThermalTileEntity
{
	private ThermalTileHelper heatCurrect = new ThermalTileHelper(Materials.Argil);
	private double smeltedTick;
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		
		smeltedTick = nbt.getDouble("SmeltedTick");
		heatCurrect.readFromNBT(nbt);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		
		nbt.setDouble("SmeltedTick", smeltedTick);
		heatCurrect.writeToNBT(nbt);
	}
	
	@Override
	public void update() 
	{
		bakeClay();
		FLE.fle.getThermalNet().emmitHeat(getBlockPos());
		if(smeltedTick > g(10))
		{
			worldObj.removeTileEntity(xCoord, yCoord, zCoord);
			invalidate();
			int meta = IB.argil_smelted.getDamageValue(worldObj, xCoord, yCoord, zCoord);
			worldObj.setBlock(xCoord, yCoord, zCoord, IB.argil_smelted);
			worldObj.setTileEntity(xCoord, yCoord, zCoord, IB.argil_smelted.createTileEntity(worldObj, meta));
		}
		heatCurrect.update();
	}
	
	private void bakeClay()
	{
		for(int i = 0; i < ForgeDirection.VALID_DIRECTIONS.length; ++i)
		{
			if(getBlockPos().toPos(ForgeDirection.VALID_DIRECTIONS[i]).isAir()) return;
		}
		if(getTemperature(ForgeDirection.UNKNOWN) > 750)
		{
			double pregress = (getTemperature(ForgeDirection.UNKNOWN) - 750) * 10D;
			heatCurrect.emitHeat(pregress);
			smeltedTick += pregress / 100D;
		}
	}
	
	@Override
	public int getTemperature(ForgeDirection dir)
	{
		return heatCurrect.getTempreture() + FLE.fle.getThermalNet().getEnvironmentTemperature(getBlockPos());
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

	@Override
	public double getPreHeatEmit()
	{
		return heatCurrect.getPreHeatEmit();
	}

	public double getProgress()
	{
		return smeltedTick / g(10);
	}
}