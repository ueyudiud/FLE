/*
 * copyright 2016-2018 ueyudiud
 */
package farcore.lib.crop;

import java.util.Set;

import com.google.common.collect.Sets;

import farcore.energy.thermal.ThermalNet;
import farcore.lib.bio.BioData;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

/**
 * The simulated crop access, use to predicate could crop plant at specific
 * position.
 * 
 * @author ueyudiud
 */
public class CropAccessSimulated implements ICropAccess
{
	World			world;
	BlockPos		pos;
	ICropSpecie		crop;
	BioData			dna;
	boolean			isWild;
	Set<String>		traits;
	
	public CropAccessSimulated(World world, BlockPos pos, ICropSpecie crop, BioData dna)
	{
		this(world, pos, crop, dna, false);
	}
	
	public CropAccessSimulated(World world, BlockPos pos, ICropSpecie crop, BioData dna, boolean isWild, String...traits)
	{
		this.world = world;
		this.pos = pos;
		this.crop = crop;
		this.dna = dna;
		this.isWild = isWild;
		this.traits = Sets.newHashSet(traits);
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
	public ICropSpecie getSpecie()
	{
		return this.crop;
	}
	
	@Override
	public CropInfo info()
	{
		CropInfo info = new CropInfo();
		info.data = this.dna;
		return info;
	}
	
	@Override
	public BioData getData()
	{
		return this.dna;
	}
	
	@Override
	public Biome biome()
	{
		return this.world.getBiome(this.pos);
	}
	
	@Override
	public boolean isWild()
	{
		return this.isWild;
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
	public int getWaterLevel()
	{
		return 0;
	}
	
	@Override
	public float temp()
	{
		// No use near-by temperature for it change so quickly.
		return ThermalNet.getEnvironmentTemperature(this.world, this.pos);
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
	public void pollinate(boolean self, BioData gm)
	{
		
	}
}
