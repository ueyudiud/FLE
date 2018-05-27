/*
 * copyright 2016-2018 ueyudiud
 */
package farcore.energy.thermal.instance;

import farcore.energy.thermal.IThermalHandler;
import farcore.lib.material.Mat;
import nebula.common.util.Direction;
import nebula.common.world.ICoord;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author ueyudiud
 */
abstract class ThermalHandlerAbstract implements IThermalHandler
{
	protected final ICoord coordinate;
	public Mat material;
	public long energy;
	
	public ThermalHandlerAbstract(ICoord coord)
	{
		this.coordinate = coord;
	}
	
	@Override
	public World world()
	{
		return this.coordinate.world();
	}
	
	@Override
	public BlockPos pos()
	{
		return this.coordinate.pos();
	}
	
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setLong("energy", this.energy);
	}
	
	public void readFromNBT(NBTTagCompound nbt)
	{
		this.energy = nbt.getLong("energy");
	}
	
	@Override
	public double getThermalConductivity(Direction direction)
	{
		return this.material.thermalConductivity;
	}
	
	@Override
	public void onHeatChange(Direction direction, long value)
	{
		this.energy += value;
	}
}
