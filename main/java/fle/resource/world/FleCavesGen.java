package fle.resource.world;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.MapGenCaves;
import fle.core.world.biome.FLEBiome;

public class FleCavesGen extends MapGenCaves
{
	public FleCavesGen()
	{
		
	}
	
	//Exception biomes to make sure we generate like vanilla
    protected boolean isExceptionBiome(BiomeGenBase biome)
    {
        if (biome == FLEBiome.mushroomIsland) return true;
        if (biome == FLEBiome.beach) return true;
        if (biome == FLEBiome.desert) return true;
        return false;
    }

    //Determine if the block at the specified location is the top block for the biome, we take into account
    //Vanilla bugs to make sure that we generate the map the same way vanilla does.
    protected boolean isTopBlock(Block[] data, int index, int x, int y, int z, int chunkX, int chunkZ)
    {
        BiomeGenBase biome = worldObj.getBiomeGenForCoords(x + chunkX * 16, z + chunkZ * 16);
        return (isExceptionBiome(biome) ? data[index] == Blocks.grass : data[index] == biome.topBlock);
    }
	
	@Override
	protected void digBlock(Block[] data, int index, int x, int y, int z,
			int chunkX, int chunkZ, boolean foundTop)
	{
        BiomeGenBase biome = worldObj.getBiomeGenForCoords(x + chunkX * 16, z + chunkZ * 16);
        Block top    = (isExceptionBiome(biome) ? Blocks.grass : biome.topBlock);
        Block filler = (isExceptionBiome(biome) ? Blocks.dirt  : biome.fillerBlock);
        Block block  = data[index];

        if ((block != null && block.isReplaceableOreGen(worldObj, x, y, z, Blocks.stone)) || block == Blocks.gravel || block == filler || block == top)
        {
            if (y < 9)
            {
                data[index] = Blocks.lava;
            }
            else
            {
                data[index] = null;

                if (foundTop && data[index - 1] == filler)
                {
                    data[index - 1] = top;
                }
            }
        }
	}
}