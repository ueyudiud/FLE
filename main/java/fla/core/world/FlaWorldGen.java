package fla.core.world;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenerator;
import cpw.mods.fml.common.IWorldGenerator;
import fla.api.world.BlockPos;
import fla.core.Fla;
import fla.core.FlaBlocks;

public class FlaWorldGen implements IWorldGenerator
{
	public void generate(Random random, int chunkX, int chunkZ, World world,
			IChunkProvider chunkGenerator, IChunkProvider chunkProvider) 
	{
		switch(world.provider.dimensionId)
		{
		case 1 : this.generateEnd(random, chunkX * 16, chunkZ * 16, world);
		case 0 : this.generateSurface(random, chunkX * 16, chunkZ * 16, world);
		case -1 : this.generateNether(random, chunkX * 16, chunkZ * 16, world);
		}
		
	}
	
	public void generateEnd(Random random, int x, int z, World world)
	{
		
	}

	public void generateSurface(Random random, int x, int z, World world)
	{
		int a = random.nextInt(10);
		if(a == 0 || a == 1 || a == 2) 
		{
			this.genOre(random, world, x, z, 180, 50, 0.5D, 120, Blocks.stone, FlaBlocks.rock1, 0);
			this.genOre(random, world, x, z, 180, 50, 0.5D, 120, Blocks.glass, FlaBlocks.rock1, 0);
		}
		if(a == 4)
		{
			this.genOre(random, world, x, z, 180, 10, 0.3D, 70, Blocks.stone, new WorldMinableInfo(FlaBlocks.ore1, 3, 0), new WorldMinableInfo(FlaBlocks.ore1, 18, 1).setCoreGen(-1D, 0.8D), new WorldMinableInfo(FlaBlocks.ore1, 10, 2), new WorldMinableInfo(FlaBlocks.ore1, 3, 3), new WorldMinableInfo(FlaBlocks.ore1, 6, 4), new WorldMinableInfo(FlaBlocks.ore1, 2, 5));
			this.genOre(random, world, x, z, 180, 10, 0.3D, 70, Blocks.glass, new WorldMinableInfo(FlaBlocks.ore1, 3, 0), new WorldMinableInfo(FlaBlocks.ore1, 18, 1).setCoreGen(-1D, 0.8D), new WorldMinableInfo(FlaBlocks.ore1, 10, 2), new WorldMinableInfo(FlaBlocks.ore1, 3, 3), new WorldMinableInfo(FlaBlocks.ore1, 6, 4), new WorldMinableInfo(FlaBlocks.ore1, 2, 5));
		}
		if(a == 5)
		{
			this.genOre(random, world, x, z, 60, 10, 0.3D, 70, Blocks.stone, new WorldMinableInfo(FlaBlocks.ore1, 12, 6), new WorldMinableInfo(FlaBlocks.ore1, 9, 4), new WorldMinableInfo(FlaBlocks.ore1, 7, 7), new WorldMinableInfo(FlaBlocks.ore1, 5, 8), new WorldMinableInfo(FlaBlocks.ore1, 1, 9), new WorldMinableInfo(FlaBlocks.ore1, 8, 10));
			this.genOre(random, world, x, z, 60, 10, 0.3D, 70, Blocks.glass, new WorldMinableInfo(FlaBlocks.ore1, 12, 6), new WorldMinableInfo(FlaBlocks.ore1, 9, 4), new WorldMinableInfo(FlaBlocks.ore1, 7, 7), new WorldMinableInfo(FlaBlocks.ore1, 5, 8), new WorldMinableInfo(FlaBlocks.ore1, 1, 9), new WorldMinableInfo(FlaBlocks.ore1, 8, 10));
		}
	}
	
	public void generateNether(Random random, int x, int z, World world)
	{
		
	}

	public boolean genOre(Random rand, World world, int x, int z, int maxY, int minY, double chance, int size, Block base, Block block, int meta)
	{
		int i = 0;
		int j = 0;
		if(rand.nextGaussian() < chance)
		{
			int X = x + rand.nextInt(16);
			int Y = minY + rand.nextInt(maxY - minY);
			int Z = z + rand.nextInt(16);
			int s = (int) Math.round(Math.sqrt(size) * rand.nextGaussian() + size / 2);
			if(Y >= world.getTopSolidOrLiquidBlock(X, Z))
			{
				return false;
			}
			if(base.isReplaceableOreGen(world, X, Y, Z, base))
			{
				new net.minecraft.world.gen.feature.WorldGenMinable(block, meta, size, base).generate(world, rand, X, Y, Z);
			}
			else
			{
				return false;
			}
		}
		return true;
	}
	public boolean genOre(Random rand, World world, int x, int z, int maxY, int minY, double chance, int size, Block base, WorldMinableInfo...info)
	{
		int i = 0;
		int j = 0;
		if(rand.nextGaussian() < chance)
		{
			int X = x + rand.nextInt(16);
			int Y = minY + rand.nextInt(maxY - minY);
			int Z = z + rand.nextInt(16);
			int s = (int) Math.round(Math.sqrt(size) * rand.nextGaussian() + size / 2);
			if(Y >= world.getTopSolidOrLiquidBlock(X, Z))
			{
				return false;
			}
			if(base.isReplaceableOreGen(world, X, Y, Z, base))
			{
				new WorldGenMinable(base, size, info).generate(world, rand, X, Y, Z);
			}
			else
			{
				return false;
			}
		}
		return true;
	}

	public boolean genOre(Random rand, World world, int x, int z, int maxY, int minY, int count, int size, Block base, WorldMinableInfo...info)
	{
		int i = 0;
		int j = 0;
		int count1 = count;
		while(i < count1 && j < 10)
		{
			int X = x + rand.nextInt(16);
			int Y = minY + rand.nextInt(maxY - minY);
			int Z = z + rand.nextInt(16);
			int s = (int) Math.round(Math.sqrt(size) * rand.nextGaussian() + size / 2);
			if(Y >= world.getTopSolidOrLiquidBlock(X, Z))
			{
				++j;
				continue;
			}
			if(world.getBlock(X, Y, Z).isReplaceableOreGen(world, X, Y, Z, base))
			{
				new WorldGenMinable(base, size, info).generate(world, rand, X, Y, Z);
			}
			else
			{
				++j;
				continue;
			}
			++i;
		}
		return true;
	}
	
	private static class WorldMinableInfo
	{
		public Block basicBlock;
		public int genWeight;
		public int blockMeta;
		public int flaMeta;//Far Land Age: the meta get in FWM.

		public WorldMinableInfo(Block basicBlock, int genWeight)
		{
			this(basicBlock, genWeight, 0);
		}
		public WorldMinableInfo(Block basicBlock, int genWeight, int blockMeta)
		{
			this.basicBlock = basicBlock;
			this.genWeight = genWeight;
			this.flaMeta = blockMeta;
			this.blockMeta = 0;
		}
		
		double distanceMax = 1.0D;
		double distanceMin = 0.0D;
		
		
		public WorldMinableInfo setCoreGen(double min, double max)
		{
			this.distanceMax = max;
			this.distanceMin = min;
			return this;
		}
		
		public double genWithCoreDistance(double distance)
		{
			return distance > distanceMax ? 0D :
				distance < distanceMin ? 0D : 1D;
		}
	}
	
	public static class WorldGenMinable extends WorldGenerator
	{
	    private final Block blockBaseOn;
	    /** The number of blocks to generate. */
	    private int numberOfBlocks;
	    private WorldMinableInfo[] infos;

	    public WorldGenMinable(Block block, int num, int meta)
	    {
	        this(Blocks.stone, num, new WorldMinableInfo(block, 10, meta));
	    }

	    public WorldGenMinable(Block target, int number, WorldMinableInfo...infos)
	    {
	    	this.blockBaseOn = target;
	    	this.numberOfBlocks = number;
	    	this.infos = infos;
	    }

	    public boolean generate(World world, Random rand, int x, int y, int z)
	    {
	        float f = rand.nextFloat() * (float)Math.PI;
	        double d0 = (double)((float)(x + 8) + MathHelper.sin(f) * (float)this.numberOfBlocks / 8.0F);
	        double d1 = (double)((float)(x + 8) - MathHelper.sin(f) * (float)this.numberOfBlocks / 8.0F);
	        double d2 = (double)((float)(z + 8) + MathHelper.cos(f) * (float)this.numberOfBlocks / 8.0F);
	        double d3 = (double)((float)(z + 8) - MathHelper.cos(f) * (float)this.numberOfBlocks / 8.0F);
	        double d4 = (double)(y + rand.nextInt(3) - 2);
	        double d5 = (double)(y + rand.nextInt(3) - 2);

	        for (int l = 0; l <= this.numberOfBlocks; ++l)
	        {
	            double d6 = d0 + (d1 - d0) * (double)l / (double)this.numberOfBlocks;
	            double d7 = d4 + (d5 - d4) * (double)l / (double)this.numberOfBlocks;
	            double d8 = d2 + (d3 - d2) * (double)l / (double)this.numberOfBlocks;
	            double d9 = rand.nextDouble() * (double)this.numberOfBlocks / 16.0D;
	            double d10 = (double)(MathHelper.sin((float)l * (float)Math.PI / (float)this.numberOfBlocks) + 1.0F) * d9 + 1.0D;
	            double d11 = (double)(MathHelper.sin((float)l * (float)Math.PI / (float)this.numberOfBlocks) + 1.0F) * d9 + 1.0D;
	            int i1 = MathHelper.floor_double(d6 - d10 / 2.0D);
	            int j1 = MathHelper.floor_double(d7 - d11 / 2.0D);
	            int k1 = MathHelper.floor_double(d8 - d10 / 2.0D);
	            int l1 = MathHelper.floor_double(d6 + d10 / 2.0D);
	            int i2 = MathHelper.floor_double(d7 + d11 / 2.0D);
	            int j2 = MathHelper.floor_double(d8 + d10 / 2.0D);

	            for (int k2 = i1; k2 <= l1; ++k2)
	            {
	                double d12 = ((double)k2 + 0.5D - d6) / (d10 / 2.0D);

	                if (d12 * d12 < 1.0D)
	                {
	                    for (int l2 = j1; l2 <= i2; ++l2)
	                    {
	                        double d13 = ((double)l2 + 0.5D - d7) / (d11 / 2.0D);

	                        if (d12 * d12 + d13 * d13 < 1.0D)
	                        {
	                            for (int i3 = k1; i3 <= j2; ++i3)
	                            {
	                                double d14 = ((double)i3 + 0.5D - d8) / (d10 / 2.0D);

	                                if (d12 * d12 + d13 * d13 + d14 * d14 < 1.0D && world.getBlock(k2, l2, i3).isReplaceableOreGen(world, k2, l2, i3, blockBaseOn))
	                                {
	                                	setRandomOreWithChance(rand, world, k2, l2, i3, d12 * d12 + d13 * d13 + d14 * d14);
	                                }
	                            }
	                        }
	                    }
	                }
	            }
	        }

	        return true;
	    }
		
		private void setRandomOreWithChance(Random rand, World world, int x, int y, int z, double coreDistance)
		{
			Block block;
			List<Integer> list = new ArrayList();
			if(infos != null)
			{
				for (int j = 0; j < infos.length; j++) 
				{
					for (int i = 0; i < infos[j].genWeight * infos[j].genWithCoreDistance(coreDistance); ++i)
					{
						list.add(j);
					}
				}
			}
			int genId = list.get(rand.nextInt(list.size()));
			world.setBlock(x, y, z, infos[genId].basicBlock, infos[genId].flaMeta, 2);
			return;
		}
	}
}
