package fle.core.world.biome;

import java.util.List;
import java.util.Random;

import farcore.enums.EnumBlock;
import farcore.lib.world.biome.BiomeBase;
import fle.core.util.BlockInfo;
import fle.core.util.ClimateManager;
import fle.core.world.climate.Climate;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class BiomeFleBase extends BiomeBase
{
	public BiomeFleBase(int id)
	{
		super(id);
	}
	public BiomeFleBase(int id, boolean register)
	{
		super(id, register);
	}
		
	@Override
	public void decorate(World world, Random rand, int x, int z)
	{
		super.decorate(world, rand, x, z);
		ClimateManager.getGenClimateAt(world, x, z).decorate(world, rand, x, z, this);
	}
	
	@Override
	public void genTerrain(World world, Random rand, Block[] blocks, byte[] metas, int x, int z, double layer)
	{
        boolean flag = true;
        int r = 0;
        int l = (int)(layer / 3.0D + 3.0D + rand.nextDouble() * 0.25D);
        int i1 = x & 15;
        int j1 = z & 15;
        int k1 = blocks.length / 256;
        int k = 0;
        Climate climate = ClimateManager.getGenClimateAt(world, x, z);
        BlockInfo[] cacheReplace = null;

        for (int l1 = 255; l1 >= 0; --l1)
        {
            int i2 = (j1 * 16 + i1) * k1 + l1;
            if(l1 == 0)
            {
            	blocks[i2] = Blocks.bedrock;
            }
            else if(l1 < 10)
            {
            	blocks[i2] = EnumBlock.lava.block();
            	metas[i2] = 15;
            }
            else if(l1 < 14 + rand.nextInt(2))
            {
            	blocks[i2] = Blocks.air;
            	metas[i2] = 0;
            }
            else
            {
            	Block block2 = blocks[i2];
            	if(block2 != null && block2.getMaterial() != Material.air)
            	{
            		if(block2 == Blocks.stone)
            		{
            			if(cacheReplace == null && (r & 0x4) == 0)
            			{
            				if((r & 0x1) == 0)
            				{
            					if(l < 0)
            					{
            						blocks[i2] = Blocks.stone;
            						metas[i2] = (byte) 0;
            					}
            					else
            					{
            						cacheReplace = climate.genTopBlock(x, z, rand, l, (r & 0x2) != 0, (r & 0x1) == 0, layer);
            						blocks[i2] = cacheReplace[0].block;
            						metas[i2] = (byte) cacheReplace[0].meta;
            						k = 1;
            						r |= 0x1;
            					}
            				}
            				else if(l1 > 112)
            				{
            					if(l < 0)
            					{
            						blocks[i2] = Blocks.stone;
            						metas[i2] = (byte) 0;
            					}
            					else
            					{
            						cacheReplace = climate.genTopBlock(x, z, rand, l, (r & 0x2) != 0, (r & 0x1) == 0, layer);
            						blocks[i2] = cacheReplace[0].block;
            						metas[i2] = (byte) cacheReplace[0].meta;
            						k = l - 1;
            						r |= 0x1;
            					}
            				}
            				else
            				{
            					k = -2;
            				}
            			}
            			else if(k < cacheReplace.length)
            			{
            				blocks[i2] = cacheReplace[k].block;
            				metas[i2] = (byte) cacheReplace[k].meta;
            				++k;
            				if(k == cacheReplace.length)
            				{
            					cacheReplace = null;
            					k = 0;
            				}
            			}
            			r |= 0x4;
            		}
            		else if(block2 == EnumBlock.water.block())
            		{
            			if((r & 0x2) == 0)
            			{
            				if(getTemperature(world, x, l1, z) < 0.15F)
            				{
            					blocks[i2] = EnumBlock.ice.block();
                				metas[i2] = (byte) 0;
            				}
            				r |= 0x2;
            			}
            		}
            	}
            	else
            	{
            		r &= (~0x4);
            	}
			}
        }
	}
}