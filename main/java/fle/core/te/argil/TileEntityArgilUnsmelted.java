package fle.core.te.argil;

import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import fle.FLE;
import fle.api.FleValue;
import fle.api.energy.IThermalTileEntity;
import fle.api.world.BlockPos;
import fle.core.init.IB;
import fle.core.init.Materials;
import fle.core.te.base.TEBase;

public class TileEntityArgilUnsmelted extends TEBase implements IThermalTileEntity
{
	private final double sh = Materials.Argil.getPropertyInfo().getSpecificHeat();
	private final double hc = Materials.Argil.getPropertyInfo().getThermalConductivity();
	
	private double heatCurrect;
	private int smeltedTick;

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		
		smeltedTick = nbt.getInteger("SmeltedTick");
		heatCurrect = nbt.getDouble("Heat");
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		
		nbt.setInteger("SmeltedTick", smeltedTick);
		nbt.setDouble("Heat", heatCurrect);
	}
	
	@Override
	public void updateEntity() 
	{
		bakeClay();
		FLE.fle.getThermalNet().emmitHeat(getBlockPos());
		if(smeltedTick > 40000)
		{
			worldObj.removeTileEntity(xCoord, yCoord, zCoord);
			invalidate();
			int meta = IB.argil_smelted.getDamageValue(worldObj, xCoord, yCoord, zCoord);
			worldObj.setBlock(xCoord, yCoord, zCoord, IB.argil_smelted);
			worldObj.setTileEntity(xCoord, yCoord, zCoord, IB.argil_smelted.createTileEntity(worldObj, meta));
		}
	}
	
	private void bakeClay()
	{
		for(int i = 0; i < ForgeDirection.VALID_DIRECTIONS.length; ++i)
		{
			if(getBlockPos().toPos(ForgeDirection.VALID_DIRECTIONS[i]).isAir()) return;
		}
		if(heatCurrect > 50)
		{
			double pregress = (heatCurrect - 50D) / 4;
			heatCurrect -= pregress;
			smeltedTick += (int) pregress;
		}
	}
	
	@Override
	public int getTemperature(ForgeDirection dir)
	{
		return (int) (heatCurrect / sh + FleValue.WATER_FREEZE_POINT);
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
}