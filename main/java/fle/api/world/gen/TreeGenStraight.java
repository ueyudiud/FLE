package fle.api.world.gen;

import java.util.Random;

import farcore.lib.world.gen.tree.TreeGenAbstract;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class TreeGenStraight extends TreeGenAbstract
{
	private final int minTreeHeight;
	private final int randTreeHeight;
	private final int minLeaveHeight;
	private final int maxLeaveHeight;
	private final float leaveLength;
	private final boolean leaveConnect;
	
	private final byte checkRange;
	
	public TreeGenStraight(int minTH, int randTH, int minLH, int maxLH, float leaveLength, boolean leaveConnect)
	{
		this.minTreeHeight = minTH;
		this.randTreeHeight = randTH;
		this.minLeaveHeight = minLH;
		this.maxLeaveHeight = maxLH;
		this.leaveLength = leaveLength;
		this.leaveConnect = leaveConnect;
		checkRange = (byte) (leaveLength / Math.sqrt(2));
	}
	
	@Override
	public boolean generate(World world, Random random, int x, int y, int z, boolean isNatural)
	{
		int r = randTreeHeight == 0 ? 0 : random.nextInt(randTreeHeight);
		int l = minTreeHeight + r;
        boolean flag = true;
        
		if (y >= 1 && y + l + 1 <= 256)
		{
            byte b0 = checkRange;
            int k1;
            Block block;

            for (int i1 = y; i1 <= y + 1 + l; ++i1)
            {
                if (i1 == y)
                {
                    b0 = 0;
                }

                for (int j1 = x - b0; j1 <= x + b0 && flag; ++j1)
                {
                    for (k1 = z - b0; k1 <= z + b0 && flag; ++k1)
                    {
                    	if((j1 - x) * (j1 - x) + (k1 - z) * (k1 - z) > checkRange) continue;
                        if (i1 >= 0 && i1 < 256)
                        {
                        	block = world.getBlock(j1, i1, k1);

                            if (isReplaceable(world, j1, i1, k1))
                            {
                                flag = false;
                            }
                        }
                    }
                }
            }

            if (!flag)
            {
                return false;
            }
            else
            {
                Block block2 = world.getBlock(x, y - 1, z);

                boolean isSoil = block2.canSustainPlant(world, x, y - 1, z, ForgeDirection.UP, (BlockSapling) Blocks.sapling);
                if (isSoil && y < 256 - l - 1)
                {
                    block2.onPlantGrow(world, x, y - 1, z, x, y, z);
                    int l1;
                    int l2;
                    int k;
                    int ch;
                    float dis;
                    b0 = checkRange;
                    int km = Math.max(l + 1, maxLeaveHeight + r);
                    boolean[][] cache = new boolean[b0 * 2 + 1][b0 * 2 + 1];
                    for(k = 0; k < l; ++k)
                    {
                    	setBlockAndNotifyAdequately(world, x, y + k, z, log, 0);
                    	if(k >= minLeaveHeight)
                    	{
                    		cache[b0][b0] = true;
                        	for(int i1 = -b0; i1 <= b0; ++i1)
                        		for(int j1 = -b0; j1 <= b0; ++j1)
                        		{
                        			if((i1 | j1) == 0) continue;
                        			dis = (float) Math.sqrt(i1 * i1 + j1 * j1);
                        			if(dis > leaveLength) continue;
                        			if(!cache[i1 + b0][j1 + b0] && 
                        					((leaveConnect && k - minLeaveHeight >= 2) || 
                        							dis < leaveLength / 2 ||
                        					random.nextInt(3 + minLeaveHeight - k) == 0))
                        			{
                        				if(leaveConnect)
                        				{
                        					if(i1 < b0 && cache[i1 + b0 + 1][j1 + b0])
                        						cache[i1 + b0][j1 + b0] = true;
                        					else if(i1 + b0 - 1 >= 0 && cache[i1 + b0 - 1][j1 + b0])
                        						cache[i1 + b0][j1 + b0] = true;
                        					else if(j1 < b0 && cache[i1 + b0][j1 + b0 + 1])
                        						cache[i1 + b0][j1 + b0] = true;
                        					else if(j1 + b0 - 1 >= 0 && cache[i1 + b0][j1 + b0 - 1])
                        						cache[i1 + b0][j1 + b0] = true;
                        				}
                        				else
                        				{
                            				cache[i1 + b0][j1 + b0] = true;
                        				}
                        			}
                        			if(cache[i1 + b0][j1 + b0])
                        			{
                        				setBlockAndNotifyAdequately(world, x + i1, y + k, z + j1, leaves, 0);
                        			}
                        		}
                    	}
                    }
                    for(; k < km; ++k)
                    {
                    	if(k == km - 1)
                    	{
                    		if(isReplaceable(world, x, y + k, z))
                    			setBlockAndNotifyAdequately(world, x, y + k, z, leaves, 0);
                    		if(isReplaceable(world, x + 1, y + k, z))
                    			setBlockAndNotifyAdequately(world, x + 1, y + k, z, leaves, 0);
                    		if(isReplaceable(world, x - 1, y + k, z))
                    			setBlockAndNotifyAdequately(world, x - 1, y + k, z, leaves, 0);
                    		if(isReplaceable(world, x, y + k, z + 1))
                    			setBlockAndNotifyAdequately(world, x, y + k, z + 1, leaves, 0);
                    		if(isReplaceable(world, x, y + k, z - 1))
                    			setBlockAndNotifyAdequately(world, x, y + k, z - 1, leaves, 0);
                    	}
                    	else
                    	{
                    		cache[b0][b0] = true;
                        	for(int i1 = -b0; i1 <= b0; ++i1)
                        		for(int j1 = -b0; j1 <= b0; ++j1)
                        		{
                        			dis = (float) Math.sqrt(i1 * i1 + j1 * j1);
                        			if(dis > checkRange) continue;
                        			if((i1 | j1) != 0 && cache[i1 + b0][j1 + b0] && !(maxLeaveHeight - k >= 1 || 
                        					(ch = (int) dis) < checkRange / 2 || 
                        					((ch += k - maxLeaveHeight) < 0 ||
                        							ch < 5 && random.nextInt(ch) == 0)))
                        			{
                        				int c = 4;
                        				if(i1 < b0 && cache[i1 + b0 + 1][j1 + b0]) --c;
                    					else if(i1 + b0 - 1 >= 0 && cache[i1 + b0 - 1][j1 + b0]) --c;
                    					else if(j1 < b0 && cache[i1 + b0][j1 + b0 + 1]) --c;
                    					else if(j1 + b0 - 1 >= 0 && cache[i1 + b0][j1 + b0 - 1]) --c;
                        				if(leaveConnect ? c > 2 : (c > 2 || (c == 2 && random.nextBoolean())))
                    						cache[i1 + b0 + 1][j1 + b0] = false;
                        			}
                        			if(cache[i1 + b0][j1 + b0])
                        			{
                        				setBlockAndNotifyAdequately(world, x + i1, y + k, z + j1, leaves, 0);
                        			}
                        		}
                    	}
                    }
                    return true;
                }
            }
		}
		return false;
	}
}