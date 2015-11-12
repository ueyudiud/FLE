package fle.core.world.biome;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.BiomeGenBase;

public class FLEBiomeLavaSea extends FLEBiomeHellBase
{
	public FLEBiomeLavaSea(String name, int index)
	{
		super(name, index);
		theBiomeDecorator.generateLakes = true;
        spawnableMonsterList.add(new BiomeGenBase.SpawnListEntry(EntityGhast.class, 30, 4, 4));
        setTemperatureRainfall(13F, 0F);
	}
	
	@Override
	protected void genTerrainBlocks(Random rand, Block[] blocks, byte[] bytes,
			boolean isFlat, boolean isNonwaterTop, int rootHeight, int x,
			int z, int size, int height)
	{
        for (int l1 = 255; l1 >= 0; --l1)
        {
            int i2 = (z * 16 + x) * size + l1;

            if (l1 <= rand.nextInt(4))
            {
                blocks[i2] = Blocks.bedrock;
            }
            else if(l1 <= 40)
            {
            	blocks[i2] = Blocks.lava;
            }
        }
	}
}