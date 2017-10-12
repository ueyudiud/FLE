/*
 * copyright© 2016-2017 ueyudiud
 */
package fle.core.tree;

import java.util.Random;

import farcore.lib.tree.ITree;
import farcore.lib.tree.TreeInfo;
import nebula.common.util.L;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class TreeGenJungle extends TreeGenHuge
{
	private boolean vineGen = false;
	
	public TreeGenJungle(ITree tree, float generateCoreLeavesChance)
	{
		super(tree, generateCoreLeavesChance);
	}
	
	public TreeGenJungle enableVineGen()
	{
		this.vineGen = true;
		return this;
	}
	
	@Override
	public boolean generateTreeAt(World world, int x, int y, int z, Random random, TreeInfo info)
	{
		int l = this.baseHeight + L.nextInt(this.randHeight, random);
		
		if (!matchHugeTreeGrow(world, random, x, y, z, l))
			return false;
		else
		{
			growLeaves(world, x, z, y + l, 2, random, info);
			
			for (int i1 = y + l - 2 - random.nextInt(4); i1 > y + l / 2; i1 -= 2 + random.nextInt(4))
			{
				float f = random.nextFloat() * (float)Math.PI * 2.0F;
				int j1 = x + (int)(0.5F + MathHelper.cos(f) * 4.0F);
				int k1 = z + (int)(0.5F + MathHelper.sin(f) * 4.0F);
				int l1;
				
				for (l1 = 0; l1 < 5; ++l1)
				{
					j1 = x + (int)(1.5F + MathHelper.cos(f) * l1);
					k1 = z + (int)(1.5F + MathHelper.sin(f) * l1);
					generateLog(world, j1, i1 - 3 + l1 / 2, k1, 1);
				}
				
				l1 = 1 + random.nextInt(2);
				int i2 = i1;
				
				for (int j2 = i1 - l1; j2 <= i2; ++j2)
				{
					int k2 = j2 - i2;
					generateCloudlyLeaves(world, j1, j2, k1, 1 - k2, 0, random, info, (byte) 1);
				}
			}
			
			for (int l2 = 0; l2 < l; ++l2)
			{
				if (isLogReplaceable(world, x, y + l2, z))
				{
					generateLog(world, x, y + l2, z, 1);
				}
				
				if (l2 > 0 && this.vineGen)
				{
					if (random.nextInt(3) > 0 && isAirOrVine(world, x - 1, y + l2, z))
					{
						generateVine(world, x - 1, y + l2, z, EnumFacing.EAST);
					}
					
					if (random.nextInt(3) > 0 && isAirOrVine(world, x, y + l2, z - 1))
					{
						generateVine(world, x, y + l2, z - 1, EnumFacing.SOUTH);
					}
				}
				
				if (l2 < l - 1)
				{
					if (isLogReplaceable(world, x + 1, y + l2, z))
					{
						generateLog(world, x + 1, y + l2, z, 1);
						if (l2 > 0 && this.vineGen)
						{
							if (random.nextInt(3) > 0 && isAirOrVine(world, x + 2, y + l2, z))
							{
								generateVine(world, x + 2, y + l2, z, EnumFacing.WEST);
							}
							
							if (random.nextInt(3) > 0 && isAirOrVine(world, x + 1, y + l2, z - 1))
							{
								generateVine(world, x + 1, y + l2, z - 1, EnumFacing.SOUTH);
							}
						}
					}
					
					if (isLogReplaceable(world, x + 1, y + l2, z + 1))
					{
						generateLog(world, x + 1, y + l2, z + 1, 1);
						if (l2 > 0 && this.vineGen)
						{
							if (random.nextInt(3) > 0 && isAirOrVine(world, x + 2, y + l2, z + 1))
							{
								generateVine(world, x + 2, y + l2, z + 1, EnumFacing.WEST);
							}
							
							if (random.nextInt(3) > 0 && isAirOrVine(world, x + 1, y + l2, z + 2))
							{
								generateVine(world, x + 1, y + l2, z + 2, EnumFacing.NORTH);
							}
						}
					}
					
					if (isLogReplaceable(world, x, y + l2, z + 1))
					{
						generateLog(world, x, y + l2, z + 1, 1);
						if (l2 > 0 && this.vineGen)
						{
							if (random.nextInt(3) > 0 && isAirOrVine(world, x - 1, y + l2, z + 1))
							{
								generateVine(world, x - 1, y + l2, z + 1, EnumFacing.EAST);
							}
							
							if (random.nextInt(3) > 0 && isAirOrVine(world, x, y + l2, z + 2))
							{
								generateVine(world, x, y + l2, z + 2, EnumFacing.NORTH);
							}
						}
					}
				}
			}
			return true;
		}
	}
	
	private void growLeaves(World world, int x, int z, int y, int size, Random rand, TreeInfo info)
	{
		for (int i1 = y - 2; i1 <= y; ++i1)
		{
			int j1 = i1 - y;
			generateCloudlyLeaves(world, x, i1, z, size - j1, 0, rand, info, (byte) 2);
		}
	}
}