package fle.core.te.creative;

import cpw.mods.fml.relauncher.SideOnly;
import net.minecraftforge.common.util.ForgeDirection;
import fle.FLE;
import fle.api.energy.IThermalTileEntity;
import fle.api.te.TEBase;
import fle.core.energy.ThermalTileHelper;
import fle.core.net.FleTEPacket;

public class TileEntityThermal extends TEBase implements IThermalTileEntity
{
	ThermalTileHelper th = new ThermalTileHelper(1.0F, 1.0F);
	
	public TileEntityThermal()
	{
		
	}
	
	@Override
	public void updateEntity()
	{
		super.updateEntity();
		th.update();
		if(isRedStoneEmmit())
		{
			th.emitHeat(th.getHeat());
		}
		else
		{
			sendToNearBy(new FleTEPacket(this, (byte) 2), 32.0F);
		}
	}

	@Override
	public int getTemperature(ForgeDirection dir)
	{
		return th.getTempreture() + FLE.fle.getThermalNet().getEnvironmentTemperature(getBlockPos());
	}

	@Override
	public double getThermalConductivity(ForgeDirection dir)
	{
		return th.getThermalConductivity();
	}

	@Override
	public double getThermalEnergyCurrect(ForgeDirection dir)
	{
		return th.getHeat();
	}

	@Override
	public void onHeatReceive(ForgeDirection dir, double heatValue)
	{
		th.reseaveHeat(heatValue);
	}

	@Override
	public void onHeatEmit(ForgeDirection dir, double heatValue)
	{
		th.emitHeat(heatValue);
	}

	@Override
	public double getPreHeatEmit()
	{
		return th.getPreHeatEmit();
	}
	
	@Override
	public Object onEmit(byte aType)
	{
		return aType == 2 ? th.getHeat() : super.onEmit(aType);
	}
	
	@Override
	public void onReceive(byte type, Object contain)
	{
		if(type == 2)
		{
			th.syncHeat((Double) contain);
		}
		super.onReceive(type, contain);
	}
}