package fle.core.world;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockQuartz;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenGlowStone1;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.util.ForgeDirection;

public class FleCrystalGen extends WorldGenerator
{
	private static final int[][] NEARBY = {{2, 3, 4, 5}, {2, 3, 4, 5}, {0, 1, 4, 5}, {0, 1, 4, 5}, {0, 1, 2, 3}, {0, 1, 2, 3}, {6, 6, 6, 6}};
	private static final int[] FACING = {2, 2, 3, 3, 4, 4, 0};
	
	public boolean generate(World aWorld, Random aRand, int x, int y, int z)
    {
		int X = x + aRand.nextInt(16) - aRand.nextInt(16);
		int Z = z + aRand.nextInt(16) - aRand.nextInt(16);
        while (!aWorld.isAirBlock(X, y, Z) || aWorld.getBlock(X, y + 1, Z) != Blocks.netherrack)
        {
            --y;
            if(y <= 0) return false;
        }
        for(int loop = 0; loop < 12; ++loop)
        {
        	int level = aRand.nextInt(64) + 32;
        	if(loop != 0)
        	{
        		X = x + aRand.nextInt(8) - aRand.nextInt(8);
        		Z = z + aRand.nextInt(8) - aRand.nextInt(8);
        	}
        	int f = -1;
        	for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
        	{
        		if(!aWorld.isAirBlock(X, y, Z) || aWorld.getBlock(X, y + 1, Z) != Blocks.netherrack) continue;
        		if(f != -1) f = 6;
        		else f = dir.ordinal();
        	}
        	if(f == -1) continue;
            aWorld.setBlock(x, y, z, Blocks.quartz_block, 0, 2);
            int[] facing = selectFacing(aRand, f);
            for(int i : facing)
            {
            	ForgeDirection d = ForgeDirection.VALID_DIRECTIONS[i];
            	level -= generate(aWorld, X - d.offsetX, y - d.offsetY, Z - d.offsetZ, aRand, level, i);
            	if(level <= 0) break;
            }
        }
        return true;
    }
	
	protected int generate(World aWorld, int x, int y, int z, Random rand, int level, int facing)
	{
		if(level <= 0) return 0; 
		int l = 0;
		switch(facing)
		{
		case 0 : --y;
		break;
		case 1 : ++y;
		break;
		case 2 : --x;
		break;
		case 3 : ++x;
		break;
		case 4 : --z;
		break;
		case 5 : ++z;
		break;
		}
		if(aWorld.getBlock(x, y, z).isAir(aWorld, x, y, z))
		{
			aWorld.setBlock(x, y, z, Blocks.quartz_block, FACING[facing], 2);
			l += rand.nextInt(8) + 8;
			int[] branch = selectFacing(rand, facing);
			for(int i : branch)
			{
				if(l >= level) return level;
				l += generate(aWorld, x, y, z, rand, level / branch.length, ForgeDirection.OPPOSITES[i]);
			}
			return l;
		}
		return 0;
	}

	protected int[] selectFacing(Random aRand, int from)
	{
		if(from == 6)
		{
			int face = 0;
			face = aRand.nextInt(0x30) & aRand.nextInt(0x30) & aRand.nextInt(0x30);
			int[] a = new int[(face >> 5) + ((face >> 4) & 1) + ((face >> 3) & 1) + ((face >> 2) & 1) + ((face >> 1) & 1) + (face & 1)];
			int b = 0;
			for(int i = 0; i < 6; ++i)
				if((face & (1 << i)) != 0)
					a[b++] = i;
			return a;
			
		}
		if(aRand.nextInt(3) != 0)
		{
			return new int[]{ForgeDirection.OPPOSITES[from]};
		}
		else
		{
			return new int[]{ForgeDirection.OPPOSITES[from], NEARBY[from][aRand.nextInt(4)]};
		}
	}
	
	@Deprecated
	public static enum CrystalType
	{
		
	}
}