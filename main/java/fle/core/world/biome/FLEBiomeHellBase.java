package fle.core.world.biome;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import fle.core.world.dim.FLEBiomeDecoratorBase;
import fle.core.world.dim.FLEBiomeDecoratorHell;

public abstract class FLEBiomeHellBase extends FLEBiome
{
	public FLEBiomeHellBase(String name, int index)
	{
		super(name, index);
        spawnableMonsterList.clear();
        spawnableCreatureList.clear();
        spawnableWaterCreatureList.clear();
        spawnableCaveCreatureList.clear();
        spawnableMonsterList.add(new BiomeGenBase.SpawnListEntry(EntityPigZombie.class, 100, 4, 4));
        setDisableRain().setTemperatureRainfall(8.0F, 0.0F);
	}

	public FLEBiomeHellBase(String name, int index, boolean flag)
	{
		super(name, index, flag);
        spawnableMonsterList.clear();
        spawnableCreatureList.clear();
        spawnableWaterCreatureList.clear();
        spawnableCaveCreatureList.clear();
        spawnableMonsterList.add(new BiomeGenBase.SpawnListEntry(EntityPigZombie.class, 100, 4, 4));
        setDisableRain().setTemperatureRainfall(8.0F, 0.0F);
	}
	
	@Override
	protected void genTerrainBlocks(World aWorld, Random rand, Block[] blocks, byte[] bytes,
			boolean isFlat, boolean isNonwaterTop, int rootHeight, int x,
			int z, int size, int height)
	{
		;
	}
	
	@Override
	public FLEBiomeDecoratorBase createBiomeDecorator()
	{
		return new FLEBiomeDecoratorHell();
	}
}