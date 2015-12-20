package fle.core.te.argil;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import flapi.energy.IThermalTileEntity;
import flapi.te.TEBase;
import fle.FLE;
import fle.core.energy.ThermalTileHelper;
import fle.core.init.Materials;

public class TileEntityCeramicBricks extends TEBase implements IThermalTileEntity
{
	protected ThermalTileHelper tc = new ThermalTileHelper(Materials.Argil);
	
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
	public void update()
	{
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
}