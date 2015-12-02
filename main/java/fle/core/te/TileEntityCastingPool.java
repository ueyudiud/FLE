package fle.core.te;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fle.FLE;
import fle.api.FleValue;
import fle.api.energy.IThermalTileEntity;
import fle.api.material.MatterDictionary;
import fle.api.net.INetEventListener;
import fle.api.te.TEIT;
import fle.core.energy.ThermalTileHelper;
import fle.core.init.Materials;
import fle.core.inventory.InventoryCastingPool;

public class TileEntityCastingPool extends TEIT<InventoryCastingPool> implements IThermalTileEntity, INetEventListener
{
	public ThermalTileHelper tc = new ThermalTileHelper(Materials.Stone);
	
	public TileEntityCastingPool()
	{
		super(new InventoryCastingPool());
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		tc.readFromNBT(nbt);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		tc.writeToNBT(nbt);
	}

	@Override
	public void updateInventory()
	{
		getTileInventory().updateEntity(this);
		FLE.fle.getThermalNet().emmitHeat(getBlockPos());
		tc.update();
	}
	
	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid)
	{
		return super.canFill(from, fluid) && from == ForgeDirection.UP;
	}
	
	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid)
	{
		return super.canDrain(from, fluid) && from != ForgeDirection.UP && from != ForgeDirection.DOWN;
	}

	@Override
	public int getTemperature(ForgeDirection dir)
	{
		return FLE.fle.getThermalNet().getEnvironmentTemperature(getBlockPos()) + tc.getTempreture();
	}

	@Override
	public double getThermalConductivity(ForgeDirection dir)
	{
		return tc.getThermalConductivity();
	}

	@Override
	public double getThermalEnergyCurrect(ForgeDirection dir)
	{
		return tc.getHeat();
	}

	@Override
	public void onHeatReceive(ForgeDirection dir, double heatValue)
	{
		tc.reseaveHeat(heatValue);
	}

	@Override
	public void onHeatEmit(ForgeDirection dir, double heatValue)
	{
		tc.emitHeat(heatValue);
	}
	
	@SideOnly(Side.CLIENT)
	public double getProgress()
	{
		if(getTileInventory().getFluid() == null) return 0D;
		return (double) getTileInventory().buf / (double) ((getTileInventory().getFluid().getFluid().getTemperature(getTileInventory().getFluid()) - FleValue.WATER_FREEZE_POINT));
	}
	
	@Override
	public Object onEmit(byte aType)
	{
		switch(aType)
		{
		case 3 : return getTileInventory().buf;
		case 4 : return tc.getHeat();
		}
		return null;
	}

	@Override
	public void onReceive(byte type, Object contain)
	{
		if(type == 3)
		{
			getTileInventory().buf = (Integer) contain;
		}
		else if(type == 4)
		{
			tc.syncHeat((Double) contain);
		}
	}

	@Override
	public double getPreHeatEmit()
	{
		return tc.getPreHeatEmit();
	}
}