/*
 * copyright© 2016-2018 ueyudiud
 */
package farcore.lib.tile.abstracts;

import java.io.IOException;

import javax.annotation.Nullable;

import nebula.common.data.Misc;
import nebula.common.network.PacketBufferExt;
import nebula.common.tile.INetworkedSyncTile;
import nebula.common.tile.TESynchronization;
import nebula.common.util.Direction;
import nebula.common.util.L;
import nebula.common.util.NBTs;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
public abstract class TEScreenLineChart extends TESynchronization implements INetworkedSyncTile
{
	public class ChartOption
	{
		boolean	enable;
		int		width;
		int		tickDelay;
		int		maxValue;
		int		minValue;
	}
	
	private final int WIDTH, DURATION, MIN_VALUE, MAX_VALUE;
	
	protected static final int Enabled = 1;
	
	protected int[]			lineHeight;
	protected TimeMarker	marker;
	protected int			maxDisplayValue, minDisplayValue;
	
	public TEScreenLineChart(int width, int duration, int min, int max)
	{
		this.WIDTH = width;
		this.DURATION = duration;
		this.MIN_VALUE = min;
		this.MAX_VALUE = max;
		enable(Enabled);
		resetChart();
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setInteger("width", this.lineHeight.length);
		nbt.setInteger("duration", this.marker.duration);
		nbt.setInteger("max", this.maxDisplayValue);
		nbt.setInteger("min", this.minDisplayValue);
		return nbt;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		ChartOption option = new ChartOption();
		option.width = NBTs.getIntOrDefault(nbt, "width", this.WIDTH);
		option.tickDelay = NBTs.getIntOrDefault(nbt, "duration", this.DURATION);
		option.maxValue = NBTs.getIntOrDefault(nbt, "max", this.MAX_VALUE);
		option.minValue = NBTs.getIntOrDefault(nbt, "min", this.MIN_VALUE);
	}
	
	protected void resetChart()
	{
		enable(Enabled);
		this.lineHeight = new int[this.WIDTH];
		this.marker = new TimeMarker(this.DURATION, this::updateChart);
		this.maxDisplayValue = this.MAX_VALUE;
		this.minDisplayValue = this.MIN_VALUE;
	}
	
	protected void resetChart(@Nullable ChartOption option)
	{
		if (option == null)
		{
			resetChart();
			return;
		}
		set(Enabled, option.enable);
		if (option.width != this.lineHeight.length) this.lineHeight = new int[option.width];
		if (option.tickDelay != this.marker.duration) this.marker = new TimeMarker(option.tickDelay, this::updateChart);
		this.maxDisplayValue = option.maxValue;
		this.minDisplayValue = option.minValue;
	}
	
	@Override
	public void readNetworkData(int type, PacketBufferExt buf) throws IOException
	{
		switch (type)
		{
		case 0:
			buf.readFixedIntArray(this.lineHeight);
			break;
		default:
			break;
		}
	}
	
	@Override
	public void writeNetworkData(int type, PacketBufferExt buf) throws IOException
	{
		switch (type)
		{
		case 0:
			buf.writeFixedIntArray(this.lineHeight);
			break;
		default:
			break;
		}
	}
	
	protected void updateChart()
	{
		if (is(Enabled))
		{
			System.arraycopy(this.lineHeight, 0, this.lineHeight, 1, this.lineHeight.length - 1);
			this.lineHeight[0] = getChartValue();
		}
	}
	
	@Override
	public Direction getRotation()
	{
		return getBlockState().getValue(Misc.PROP_DIRECTION_HORIZONTALS);
	}
	
	protected abstract int getChartValue();
	
	public int[] getLineHeight()
	{
		return this.lineHeight;
	}
	
	public float rescaleValue(float value)
	{
		return (L.range(this.maxDisplayValue, this.minDisplayValue, value) - this.minDisplayValue) / (this.maxDisplayValue - this.minDisplayValue);
	}
	
	@SideOnly(Side.CLIENT)
	public float getDelayTick(float partialTicks)
	{
		return (this.marker.getDelay() + partialTicks) / this.marker.duration;
	}
}
