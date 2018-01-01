/*
 * copyright© 2016-2017 ueyudiud
 */
package farcore.energy.thermal;

import farcore.data.V;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * The heat wave, used when on explosion, etc.
 * 
 * @author ueyudiud
 *
 */
public class HeatWave implements ITickable, IThermalObjectInWorld
{
	private World	world;
	private float	temperature;
	
	private double	coreX;
	private double	coreY;
	private double	coreZ;
	
	private boolean	isDead	= false;
	private double	total;
	private double	spread;
	private int		tick;
	
	public HeatWave(World world)
	{
		this.world = world;
	}
	
	public HeatWave(World world, float temp, BlockPos pos, double hardness, double spread)
	{
		this(world, temp, pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5, hardness, spread);
	}
	
	public HeatWave(World world, float temp, double x, double y, double z, double hardness, double spread)
	{
		this.world = world;
		this.temperature = temp;
		this.coreX = x;
		this.coreY = y;
		this.coreZ = z;
		this.total = hardness;
		this.spread = spread;
	}
	
	@Override
	public float getDetTemp(double distanceSq, float tempBase)
	{
		float sigma = (float) (this.spread * this.tick + 1F);
		return (float) ((this.temperature - tempBase) / (sigma * V.sqrt2π)) * (float) Math.exp(-distanceSq / (2 * sigma * sigma));
	}
	
	@Override
	public void update()
	{
		++this.tick;
		if (this.spread * this.tick > 100)
		{
			this.isDead = true;
		}
	}
	
	@Override
	public boolean isDead()
	{
		return this.isDead;
	}
	
	@Override
	public World world()
	{
		return this.world;
	}
	
	@Override
	public double[] position()
	{
		return new double[] { this.coreX, this.coreY, this.coreZ };
	}
	
	@Override
	public void readFromNBT(NBTBase nbt)
	{
		NBTTagCompound compound = (NBTTagCompound) nbt;
		this.coreX = compound.getDouble("x");
		this.coreY = compound.getDouble("y");
		this.coreZ = compound.getDouble("z");
		this.tick = compound.getInteger("tick");
		this.total = compound.getDouble("a");
		this.spread = compound.getDouble("s");
		this.temperature = compound.getFloat("t");
	}
	
	@Override
	public NBTBase writeFromNBT()
	{
		NBTTagCompound compound = new NBTTagCompound();
		compound.setDouble("x", this.coreX);
		compound.setDouble("y", this.coreY);
		compound.setDouble("z", this.coreZ);
		compound.setInteger("tick", this.tick);
		compound.setDouble("a", this.total);
		compound.setDouble("s", this.spread);
		compound.setFloat("t", this.temperature);
		return compound;
	}
}
