package farcore.energy.thermal;

import farcore.util.Unit;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

/**
 * Thermal tile helper.<br>
 * @author ueyudiud
 *
 */
public class ThermalHelper
{
	public final float specificHeat;
	public final float thermalConductivity;
	public final float maxHeat;
	private float postHeatDelta;
	private float heatDelta;
	private float heat = -1;
	
	public ThermalHelper(float specificHeat, float thermalConductivity)
	{
		this.specificHeat = specificHeat;
		this.thermalConductivity = thermalConductivity;
		maxHeat = Unit.TP * specificHeat;
	}

	public void writeToNBT(NBTTagCompound nbt)
	{
		writeToNBT(nbt, "thermal");
	}
	public void writeToNBT(NBTTagCompound nbt, String tag)
	{
		nbt.setFloat(tag, heat);
	}
	
	public void readFromNBT(NBTTagCompound nbt)
	{
		readFromNBT(nbt, "thermal");
	}
	public void readFromNBT(NBTTagCompound nbt, String tag)
	{
		heat = nbt.getFloat(tag);
	}
	
	public void init(TileEntity tile)
	{
		if(heat < 0)
		{
			heat = specificHeat * ThermalNet.getEnviormentTemp(tile.getWorldObj(), tile.xCoord, tile.yCoord, tile.zCoord);
		}
	}
	
	public void update(TileEntity tile)
	{
		postHeatDelta = heatDelta;
		heatDelta = 0;
	}
	
	public float temperature()
	{
		return heat / specificHeat;
	}
	
	public void emit(float heat)
	{
		this.heat -= heat;
		this.heatDelta -= heat;
		if(heat < 0) heat = 0;
	}
	
	public void receive(float heat)
	{
		this.heat += heat;
		this.heatDelta += heat;
		if(heat > maxHeat) heat = maxHeat;
	}
	
	public void setTemperature(float temp)
	{
		this.heat = temp * specificHeat;
	}
	
	public float getDeltaHeat()
	{
		return postHeatDelta;
	}
}