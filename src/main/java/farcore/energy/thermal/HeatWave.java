package farcore.energy.thermal;

import farcore.data.V;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class HeatWave implements ITickable, IThermalObjectInWorld
{
	private World world;
	private float temperature;
	
	private double coreX;
	private double coreY;
	private double coreZ;

	private boolean isDead = false;
	private double total;
	private double spread;
	private int tick;

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
		temperature = temp;
		coreX = x;
		coreY = y;
		coreZ = z;
		total = hardness;
		this.spread = spread;
	}

	@Override
	public float getDetTemp(double distanceSq, float tempBase)
	{
		float sigma = (float) (spread * tick + 1F);
		return (float) ((temperature - tempBase) / (sigma * V.sqrt2π)) *
				(float) Math.exp(- distanceSq / (2 * sigma * sigma));
	}

	@Override
	public void update()
	{
		++tick;
		if(spread * tick > 100)
		{
			isDead = true;
		}
	}
	
	@Override
	public boolean isDead()
	{
		return isDead;
	}
	
	@Override
	public World world()
	{
		return world;
	}

	@Override
	public double[] position()
	{
		return new double[]{coreX, coreY, coreZ};
	}

	@Override
	public void readFromNBT(NBTBase nbt)
	{
		NBTTagCompound compound = (NBTTagCompound) nbt;
		coreX = compound.getDouble("x");
		coreY = compound.getDouble("y");
		coreZ = compound.getDouble("z");
		tick = compound.getInteger("tick");
		total = compound.getDouble("a");
		spread = compound.getDouble("s");
		temperature = compound.getFloat("t");
	}
	
	@Override
	public NBTBase writeFromNBT()
	{
		NBTTagCompound compound = new NBTTagCompound();
		compound.setDouble("x", coreX);
		compound.setDouble("y", coreY);
		compound.setDouble("z", coreZ);
		compound.setInteger("tick", tick);
		compound.setDouble("a", total);
		compound.setDouble("s", spread);
		compound.setFloat("t", temperature);
		return compound;
	}
}