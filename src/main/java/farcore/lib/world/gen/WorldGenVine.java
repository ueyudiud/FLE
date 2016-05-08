package farcore.lib.world.gen;

import java.util.Random;

import farcore.enums.Direction;
import farcore.util.U;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.oredict.OreDictionary;

public class WorldGenVine extends WorldGenerator
{
	private Block bush;
	private Block vine;
	private int meta;
	private int growOffset;
	
	public WorldGenVine(Block bush, Block vine, int meta, int growOffset)
	{
		this.bush = bush;
		this.vine = vine;
		this.meta = meta;
		this.growOffset = growOffset;
	}

	@Override
	public boolean generate(World world, Random random, int x, int y, int z)
	{
		int i, j, k, k1;

        for (int loop = 0; loop < 3; ++loop)
        {
        	i = x + random.nextInt(4) - random.nextInt(4);
            j = z + random.nextInt(4) - random.nextInt(4);
            k = world.getTopSolidOrLiquidBlock(i, j);
            if(world.getBlock(i, y, j).isReplaceable(world, i, y, j) &&
            		bush.canBlockStay(world, i, y, j) && 
            		!U.Worlds.isBlockNearby(world, i, k, j, bush, meta, true))
            {
            	world.setBlock(i, y, j, bush, meta, 2);
            	int length = 4 + random.nextInt(8);
            	k1 = k;
            	Direction direction = U.Lang.randomSelect(Direction.directions_2D, random);
            	{
            		for(; y < 256 && y > 0 && length > 0; --length, k1 += growOffset)
            		{
            			if(world.getBlock(i + direction.x, k1, j + direction.z)
            					.isReplaceable(world, i + direction.x, k1, j + direction.z) &&
            					vine.canBlockStay(world, i + direction.x, k1, j + direction.z))
            			{
            				world.setBlock(i + direction.x, k1, j + direction.z, vine, meta, 2);
            			}
            		}
            	}
            }
        }
		return true;
	}
}