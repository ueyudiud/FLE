package fle.energy.block;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flapi.energy.IThermalTileEntity;
import flapi.material.MaterialAbstract;
import flapi.te.TEBase;
import fle.FLE;
import fle.core.energy.ThermalTileHelper;
import fle.core.net.FleSyncAskTileMetaPacket;
import fle.core.net.FleTEPacket;

public class TileEntityThermalWire extends TEBase implements IThermalTileEntity
{
	private ThreadLocal<MaterialAbstract> mT = new ThreadLocal<MaterialAbstract>();
	
	MaterialAbstract material;
	ThermalTileHelper tc;
	
	public TileEntityThermalWire()
	{
	}
	
	public void init(MaterialAbstract material)
	{
		mT.set(material);
	}
	
	@Override
	protected void onPostinit(NBTTagCompound nbt)
	{
		super.onPostinit(nbt);
		MaterialAbstract m1 = mT.get();
		if(m1 != null)
		{
			material = m1;
			tc = new ThermalTileHelper(material);
			if(nbt != null)
				tc.readFromNBT(nbt);
			sendToNearBy(new FleTEPacket(this, (byte) 3), 64.0F);
		}
	}
	
	@Override
	public void update()
	{
		if(tc == null)
		{
			if(worldObj.isRemote)
			{
				sendToServer(new FleSyncAskTileMetaPacket((byte) 3, getBlockPos()));
				return;
			}
			return;
		}
		FLE.fle.getThermalNet().emmitHeat(getBlockPos());
		tc.update();
		sendToNearBy(new FleTEPacket(this, (byte) 4), 16.0F);
		markRenderForUpdate();
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		material = MaterialAbstract.getMaterialRegistry().get(nbt.getString("Material"));
		if(material != null)
		{
			mT.set(material);
		}
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		if(material != null)
		{
			nbt.setString("Material", MaterialAbstract.getMaterialRegistry().name(material));
			tc.writeToNBT(nbt);
		}
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
		try
		{
			tc.reseaveHeat(heatValue);
		}
		catch(Throwable e)
		{
			;
		}
	}

	@Override
	public void onHeatEmit(ForgeDirection dir, double heatValue)
	{
		try
		{
			tc.emitHeat(heatValue);
		}
		catch(Throwable e)
		{
			;
		}
	}

	@Override
	public double getPreHeatEmit()
	{
		return tc == null ? 0 : tc.getPreHeatEmit();
	}
	
	public MaterialAbstract getMaterial()
	{
		return material;
	}
	
	@SideOnly(Side.CLIENT)
	public int getColor()
	{
		return material == null ? 0xFFFFFF : material.getPropertyInfo() != null ? material.getPropertyInfo().getColors()[0] : 0xFFFFFF;
	}
	
	@Override
	public Object onEmit(byte aType)
	{
		switch(aType)
		{
		case 3 : return MaterialAbstract.getMaterialRegistry().serial(material);
		case 4 : return tc.getHeat();
		}
		return super.onEmit(aType);
	}
	
	@Override
	public void onReceive(byte type, Object contain)
	{
		switch(type)
		{
		case 3 : 
			material = MaterialAbstract.getMaterialRegistry().get((Integer) contain);
			if(material != null)
				tc = new ThermalTileHelper(material);
		break;
		case 4 : if(tc != null) tc.syncHeat((Double) contain);
		break;
		default : 
			super.onReceive(type, contain);
		}
	}
}