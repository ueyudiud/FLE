package farcore.lib.tree.instance;

import java.util.Random;

import farcore.lib.tree.TreeBase;
import farcore.lib.tree.TreeGenAbstract;
import farcore.lib.tree.TreeInfo;
import farcore.util.U;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TreeGenSimple extends TreeGenAbstract
{
	/** The minimum height of a generated tree. */
	private short minTreeHeight;
	/** The rand height of a generated tree. */
	private short randTreeHeight;
	/** If leaves need connect from up to down. */
	private final boolean leavesConnect;
	
	private short minLeavesHeight;
	private short logCheckWidth = 1;
	private short maxLeavesHeight;
	private float leavesWidth;
	private short checkRange;
	
	public TreeGenSimple(TreeBase tree, float generateCoreLeavesChance, boolean leavesConnect)
	{
		super(tree, generateCoreLeavesChance);
		this.leavesConnect = leavesConnect;
	}

	public TreeGenSimple setTreeLogShape(int minHeiht, int randHeight)
	{
		minTreeHeight = (short) minHeiht;
		randTreeHeight = (short) randHeight;
		return this;
	}
	public TreeGenSimple setTreeLeavesShape(int minLeavesHeight,
			int maxLeavesHeight, int logCheckWidth, float leavesWidth)
	{
		this.minLeavesHeight = (short) minLeavesHeight;
		this.maxLeavesHeight = (short) maxLeavesHeight;
		this.logCheckWidth = (short) logCheckWidth;
		this.leavesWidth = leavesWidth;
		checkRange = (short) (leavesWidth + 0.5);
		return this;
	}

	@Override
	public boolean generateTreeAt(World world, int x, int y, int z, Random random, TreeInfo info)
	{
		int r = randTreeHeight == 0 ? 0 : random.nextInt(randTreeHeight);
		int l = minTreeHeight + r;
		boolean flag = true;

		if (y >= 1 && y + l + 1 <= 256)
		{
			byte b0 = (byte) checkRange;
			int k1;
			Block block;
			
			if (!checkLogGrow(world, x, y, z, logCheckWidth, l + 1, logCheckWidth, false))
				return false;
			else
			{
				BlockPos pos;
				IBlockState state = world.getBlockState(pos = new BlockPos(x, y - 1, z));
				
				if (state.getBlock().canSustainPlant(state, world, pos, EnumFacing.UP, (BlockSapling) Blocks.SAPLING) &&
						y < 256 - l - 1)
				{
					state.getBlock().onPlantGrow(state, world, pos, pos.up());
					int l1;
					int l2;
					int k;
					int ch;
					float dis;
					int km = Math.max(l + 1, maxLeavesHeight + r);
					boolean[][] cache = new boolean[b0 * 2 + 1][b0 * 2 + 1];
					for(k = 0; k < l; ++k)
					{
						generateLog(world, x, y + k, z, 1);
						if(k >= minLeavesHeight)
						{
							cache[b0][b0] = true;
							for(int i1 = -b0; i1 <= b0; ++i1)
							{
								for(int j1 = -b0; j1 <= b0; ++j1)
								{
									if((i1 | j1) == 0)
									{
										continue;
									}
									dis = (float) Math.sqrt(i1 * i1 + j1 * j1) + (random.nextFloat() - random.nextFloat()) * 0.2F;
									if(dis > leavesWidth)
									{
										continue;
									}
									if(!cache[i1 + b0][j1 + b0] &&
											((leavesConnect && k - minLeavesHeight >= 2) ||
													dis < leavesWidth / 1.3F ||
													U.L.nextInt((int) ((3 + minLeavesHeight - k) * dis / 4F), random) == 0))
									{
										if(leavesConnect)
										{
											if(i1 < b0 && cache[i1 + b0 + 1][j1 + b0])
											{
												cache[i1 + b0][j1 + b0] = true;
											}
											else if(i1 + b0 - 1 >= 0 && cache[i1 + b0 - 1][j1 + b0])
											{
												cache[i1 + b0][j1 + b0] = true;
											}
											else if(j1 < b0 && cache[i1 + b0][j1 + b0 + 1])
											{
												cache[i1 + b0][j1 + b0] = true;
											}
											else if(j1 + b0 - 1 >= 0 && cache[i1 + b0][j1 + b0 - 1])
											{
												cache[i1 + b0][j1 + b0] = true;
											}
										}
										else
										{
											cache[i1 + b0][j1 + b0] = true;
										}
									}
									if(cache[i1 + b0][j1 + b0])
									{
										generateTreeLeaves(world, x + i1, y + k, z + j1, 0, random, info);
									}
								}
							}
						}
					}
					for(; k < km; ++k)
					{
						if(k == km - 1)
						{
							if(isLeavesReplaceable(world, x, y + k, z))
							{
								generateTreeLeaves(world, x, y + k, z, 0, random, info);
							}
							if(isLeavesReplaceable(world, x + 1, y + k, z))
							{
								generateTreeLeaves(world, x + 1, y + k, z, 0, random, info);
							}
							if(isLeavesReplaceable(world, x - 1, y + k, z))
							{
								generateTreeLeaves(world, x - 1, y + k, z, 0, random, info);
							}
							if(isLeavesReplaceable(world, x, y + k, z + 1))
							{
								generateTreeLeaves(world, x, y + k, z + 1, 0, random, info);
							}
							if(isLeavesReplaceable(world, x, y + k, z - 1))
							{
								generateTreeLeaves(world, x, y + k, z - 1, 0, random, info);
							}
						}
						else
						{
							cache[b0][b0] = true;
							for(int i1 = -b0; i1 <= b0; ++i1)
							{
								for(int j1 = -b0; j1 <= b0; ++j1)
								{
									dis = (float) Math.sqrt(i1 * i1 + j1 * j1);
									if(dis > checkRange)
									{
										continue;
									}
									if((i1 | j1) != 0 && cache[i1 + b0][j1 + b0] && !(maxLeavesHeight - k >= 1 ||
											(ch = (int) dis) < checkRange / 2 ||
											((ch += k - maxLeavesHeight) < 0 ||
													ch < 5 && random.nextInt(ch) == 0)))
									{
										int c = 4;
										if(i1 < b0 && cache[i1 + b0 + 1][j1 + b0])
										{
											--c;
										}
										else if(i1 + b0 - 1 >= 0 && cache[i1 + b0 - 1][j1 + b0])
										{
											--c;
										}
										else if(j1 < b0 && cache[i1 + b0][j1 + b0 + 1])
										{
											--c;
										}
										else if(j1 + b0 - 1 >= 0 && cache[i1 + b0][j1 + b0 - 1])
										{
											--c;
										}
										if(leavesConnect ? c > 2 : (c > 2 || (c == 2 && random.nextBoolean())))
										{
											cache[i1 + b0][j1 + b0] = false;
										}
									}
									if(cache[i1 + b0][j1 + b0])
									{
										generateTreeLeaves(world, x + i1, y + k, z + j1, 0, random, info);
									}
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