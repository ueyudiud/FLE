package fle.core.world.biome;

import java.util.Random;

import fle.api.enums.EnumFLERock;
import fle.api.material.MaterialRock;
import fle.core.block.BlockFleRock;
import fle.core.block.BlockRock;
import fle.core.init.IB;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class FLEBiomeRiver extends FLEBiome
{
	public FLEBiomeRiver(String name, int index)
	{
		super(name, index);
		spawnableCreatureList.clear();
		topBlock = fillerBlock = Blocks.dirt;
		theBiomeDecorator.sandPerChunk = 2;
	}
	
	int height;
	
	public FLEBiomeRiver setWaterHeight(int aHeight)
	{
		height = aHeight;
		return this;
	}
	
	@Override
	protected void genTerrainBlocks(World aWorld, Random rand, Block[] blocks, byte[] bytes,
			boolean isFlat, boolean isNonwaterTop, int rootHeight, int x,
			int z, int size, int height)
	{
		int waterHeight = rootHeight + height - 1;
        boolean flag = true;
        int k = -1;
        BlockFleRock rock = BlockFleRock.a(MaterialRock.getRockFromType(EnumFLERock.getRock(rockAcidityNoise.noise(x, z), rockWeatheringNoise.noise(x, z))));
        for (int l1 = 255; l1 >= 0; --l1)
        {
            int i2 = (z * 16 + x) * size + l1;

            if (l1 <= rand.nextInt(2))
            {
                blocks[i2] = Blocks.bedrock;
            }
            else if(l1 <= 8)
            {
            	blocks[i2] = Blocks.lava;
            }
            else if(l1 <= 10 + rand.nextInt(6))
            {
            	blocks[i2] = Blocks.air;
            }
            else
            {
                Block block2 = blocks[i2];
                if (block2 != null && block2.getMaterial() != Material.air)
                {
                    if (block2 == Blocks.stone)
                    {
                        if(k == -1)
                        {
                        	if(rootHeight > 0)
                        	{
                                if (l1 >= height - 16)
                                {
                                	blocks[i2] = Blocks.dirt;
                                	k = rootHeight;
                                }
                                else
                                {
                                	k = -2;
                                	continue;
                                }
                        	}
                        }
                        else if(k > 0)
                        {
                        	--k;
                        	if(k > 0)
                        	{
                        		blocks[i2] = Blocks.dirt;
                        	}
                        	else if(k == 0)
                        	{
                        		blocks[i2] = Blocks.gravel;
                        		k = -2;
                        	}
                        }
                        else
                        {
                        	blocks[i2] = rock;
                        	continue;
                        }
                    }
                }
                else if(l1 <= waterHeight)
                {
                	if(flag)
                	{
                    	if(k > 0) --k;
                      	if(getFloatTemperature(x, l1, z) < 0.15F) blocks[i2] = Blocks.ice;
                      	else blocks[i2] = Blocks.water;
                       	flag = false;
                	}
                	else
                	{
                		blocks[i2] = Blocks.water;
                	}
                }
                else
                {
                    k = -1;
                }
            }
        }
	}
}