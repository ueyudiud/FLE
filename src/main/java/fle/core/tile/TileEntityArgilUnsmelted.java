package fle.core.tile;

import farcore.energy.thermal.ThermalHelper;
import farcore.enums.Direction;
import fle.api.tile.TileEntityThermalable;
import fle.load.BlockItems;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityArgilUnsmelted extends TileEntityThermalable
{
	private float progress;
	
	public TileEntityArgilUnsmelted()
	{
		super(new ThermalHelper(1.2E4F, 3.5E-2F));
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setFloat("progress", progress);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		progress = nbt.getFloat("progress");
	}
	
	@Override
	protected void updateServer2()
	{
		float temp = getTemperature(Direction.U);
		if(temp > 500)
		{
			float amt = (temp - 500) * 27.8F;
			helper.emit(amt);
			progress += amt;
			if(progress > 10000000)
			{
				smeltedArgil();
			}
		}
	}
	
	private void smeltedArgil()
	{
		switch (getBlockMetadata())
		{
		case 0:
			worldObj.setBlock(xCoord, yCoord, zCoord, BlockItems.argil, 0, 3);
			break;
		default:
			break;
		}
		TileEntity tile = worldObj.getTileEntity(xCoord, yCoord, zCoord);
		if(tile instanceof TileEntityThermalable)
		{
			((TileEntityThermalable) tile).setTemp(helper.temperature());
		}
	}
}