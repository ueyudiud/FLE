package fla.core.world.generate;

import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.common.DimensionManager;

//Rough class...
public class FlaWorldType extends WorldType
{
	public static FlaWorldType FARLAND;
	
	public FlaWorldType(String name) 
	{
		super(name);
	}

	public BiomeGenBase[] getBiomesForWorldType()
	{
		return new BiomeGenBase[]{BiomeGenBase.plains};
	}
	
	@Override
	public WorldChunkManager getChunkManager(World world)
	{
		return new FlaWorldChunkManager(world);
	}

	@Override
	public IChunkProvider getChunkGenerator(World world, String generatorOptions)
	{
		return new FlaChunkProvider(world, world.getSeed(), world.getWorldInfo().isMapFeaturesEnabled());
	}
}
