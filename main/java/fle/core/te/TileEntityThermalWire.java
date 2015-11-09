package fle.core.te;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fle.FLE;
import fle.api.FleValue;
import fle.api.cover.IThermalCover;
import fle.api.energy.IThermalTileEntity;
import fle.api.material.MaterialAbstract;
import fle.api.material.MaterialAlloy;
import fle.api.te.TEBase;
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
	public void updateEntity()
	{
		super.updateEntity();
		if(tc == null)
		{
			if(worldObj.isRemote)
			{
				sendToServer(new FleSyncAskTileMetaPacket((byte) 3, getBlockPos()));
				return;
			}
			return;
		}
		updateCovers();
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
		if(tc == null) return FLE.fle.getThermalNet().getEnvironmentTemperature(getBlockPos());
		if(should(COVER)) return tc.getTempreture() + FLE.fle.getThermalNet().getEnvironmentTemperature(getBlockPos());
		if(getCover(dir) instanceof IThermalCover)
		{
			enable(COVER);
			int ret = ((IThermalCover) getCover(dir)).getTemperature(this, dir);
			disable(COVER);
			return ret;
		}
		return tc.getTempreture() + FLE.fle.getThermalNet().getEnvironmentTemperature(getBlockPos());
	}

	@Override
	public double getThermalConductivity(ForgeDirection dir)
	{
		if(tc == null) return 0;
		if(should(COVER)) return tc.getThermalConductivity();
		if(getCover(dir) instanceof IThermalCover)
		{
			enable(COVER);
			double ret = ((IThermalCover) getCover(dir)).getThermalConductivity(this, dir);
			disable(COVER);
			return ret;
		}
		return tc.getThermalConductivity();
	}

	@Override
	public double getThermalEnergyCurrect(ForgeDirection dir)
	{
		if(tc == null) return 0;
		if(should(COVER)) return tc.getHeat();
		if(getCover(dir) instanceof IThermalCover)
		{
			enable(COVER);
			double ret = ((IThermalCover) getCover(dir)).getThermalEnergyCurrect(this, dir);
			disable(COVER);
			return ret;
		}
		return tc.getHeat();
	}

	@Override
	public void onHeatReceive(ForgeDirection dir, double heatValue)
	{
		if(tc == null) return;
		if(should(COVER))
		{
			tc.reseaveHeat(heatValue);
			return;
		}
		if(getCover(dir) instanceof IThermalCover)
		{
			enable(COVER);
			((IThermalCover) getCover(dir)).onHeatReceive(this, dir, heatValue);
			disable(COVER);
		}
		else
		{
			tc.reseaveHeat(heatValue);
		}
	}

	@Override
	public void onHeatEmit(ForgeDirection dir, double heatValue)
	{
		if(tc == null) return;
		if(should(COVER))
		{
			tc.emitHeat(heatValue);
			return;
		}
		if(getCover(dir) instanceof IThermalCover)
		{
			enable(COVER);
			((IThermalCover) getCover(dir)).onHeatEmit(this, dir, heatValue);
			disable(COVER);
		}
		else
		{
			tc.emitHeat(heatValue);
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
	public Object onEmmit(byte aType)
	{
		switch(aType)
		{
		case 3 : return MaterialAbstract.getMaterialRegistry().serial(material);
		case 4 : return tc.getHeat();
		}
		return super.onEmmit(aType);
	}
	
	@Override
	public void onReseave(byte type, Object contain)
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
			super.onReseave(type, contain);
		}
	}
}