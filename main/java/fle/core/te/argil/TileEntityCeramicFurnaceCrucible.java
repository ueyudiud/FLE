package fle.core.te.argil;

import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fle.FLE;
import fle.api.energy.IThermalTileEntity;
import fle.api.material.IAtoms;
import fle.api.net.INetEventListener;
import fle.api.te.IMatterContainer;
import fle.api.te.TEIT;
import fle.api.util.IChemCondition;
import fle.core.energy.ThermalTileHelper;
import fle.core.gui.InventoryCeramicFurnaceCrucible;
import fle.core.init.Materials;

public class TileEntityCeramicFurnaceCrucible extends TEIT<InventoryCeramicFurnaceCrucible> implements IThermalTileEntity, IChemCondition, IMatterContainer
{
	protected ThermalTileHelper tc = new ThermalTileHelper(Materials.Argil);

	public TileEntityCeramicFurnaceCrucible()
	{
		super(new InventoryCeramicFurnaceCrucible());
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		tc.writeToNBT(nbt);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		tc.readFromNBT(nbt);
	}
	
	@Override
	public void updateEntity()
	{
		inv.updateEntity(this);
		FLE.fle.getThermalNet().emmitHeat(getBlockPos());
		tc.update();
	}

	@Override
	public int getTemperature(ForgeDirection dir)
	{
		return tc.getTempreture() + FLE.fle.getThermalNet().getEnvironmentTemperature(getBlockPos());
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
	
	@Override
	public double getPreHeatEmit()
	{
		return tc.getPreHeatEmit();
	}

	@Override
	public EnumPH getPHLevel()
	{
		return EnumPH.Water;
	}

	@Override
	public EnumOxide getOxideLevel()
	{
		return EnumOxide.C;
	}

	@Override
	public int getTemperature()
	{
		return getTemperature(ForgeDirection.UNKNOWN);
	}
	
	@SideOnly(Side.CLIENT)
	public Map<IAtoms, Integer> getContainerMap()
	{
		return inv.matterMap;
	}

	@Override
	public Map<IAtoms, Integer> getMatterContain()
	{
		return inv.matterMap;
	}

	@Override
	public void setMatterContain(Map<IAtoms, Integer> map)
	{
		inv.matterMap = map;
	}

	public void onOutput()
	{
		inv.outputStack(this);
	}

	public void drain()
	{
		inv.drain(inv.getCapacity(), true);
		inv.syncTank(this);
	}
}