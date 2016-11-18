package farcore.lib.crop;

import java.util.Random;

import farcore.energy.thermal.ThermalNet;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class CropAccessSimulated implements ICropAccess
{
	World world;
	BlockPos pos;
	ICrop crop;
	String dna;
	
	public CropAccessSimulated(World world, BlockPos pos, ICrop crop, String dna)
	{
		this.world = world;
		this.pos = pos;
		this.crop = crop;
		this.dna = dna;
	}
	
	@Override
	public World world()
	{
		return world;
	}

	@Override
	public BlockPos pos()
	{
		return pos;
	}

	@Override
	public String getDNA()
	{
		return dna;
	}

	@Override
	public ICrop crop()
	{
		return crop;
	}

	@Override
	public CropInfo info()
	{
		return new CropInfo();
	}

	@Override
	public Biome biome()
	{
		return world.getBiomeGenForCoords(pos);
	}

	@Override
	public boolean isWild()
	{
		return false;
	}

	@Override
	public int stage()
	{
		return 0;
	}

	@Override
	public Random rng()
	{
		return new Random();
	}

	@Override
	public int getWaterLevel()
	{
		return 0;
	}
	
	@Override
	public float temp()
	{
		return ThermalNet.getEnviormentTemperature(world, pos);//No use near-by temperature for it change so quickly.
	}

	@Override
	public void grow(int growth)
	{
		
	}

	@Override
	public void setStage(int stage)
	{

	}

	@Override
	public int useWater(int amount)
	{
		return 0;
	}

	@Override
	public void killCrop()
	{
	}
}