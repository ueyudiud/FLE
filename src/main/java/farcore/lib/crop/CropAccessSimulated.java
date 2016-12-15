package farcore.lib.crop;

import java.util.Random;

import farcore.energy.thermal.ThermalNet;
import farcore.lib.bio.GeneticMaterial;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class CropAccessSimulated implements ICropAccess
{
	World world;
	BlockPos pos;
	ICrop crop;
	GeneticMaterial dna;
	
	public CropAccessSimulated(World world, BlockPos pos, ICrop crop, GeneticMaterial dna)
	{
		this.world = world;
		this.pos = pos;
		this.crop = crop;
		this.dna = dna;
	}
	
	@Override
	public World world()
	{
		return this.world;
	}
	
	@Override
	public BlockPos pos()
	{
		return this.pos;
	}
	
	@Override
	public ICrop crop()
	{
		return this.crop;
	}
	
	@Override
	public CropInfo info()
	{
		CropInfo info = new CropInfo();
		info.geneticMaterial = this.dna;
		return info;
	}
	
	@Override
	public GeneticMaterial getGeneticMaterial()
	{
		return this.dna;
	}
	
	@Override
	public Biome biome()
	{
		return this.world.getBiomeGenForCoords(this.pos);
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
	public float stageBuffer()
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
		return ThermalNet.getEnviormentTemperature(this.world, this.pos);//No use near-by temperature for it change so quickly.
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
	
	@Override
	public void pollinate(GeneticMaterial gm)
	{
		
	}
}