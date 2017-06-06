/*
 * copyright© 2016-2017 ueyudiud
 */

package fargen.core.biome.decorator;

import java.util.Random;

import farcore.data.M;
import farcore.data.MP;
import nebula.base.Stack;
import nebula.base.WeightedRandomSelector;
import nebula.base.function.Selector;
import nebula.common.world.gen.WorldGenRandPlant;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate;
import net.minecraftforge.event.terraingen.TerrainGen;

/**
 * @author ueyudiud
 */
public class SimpleBiomeDecorator extends BiomeDecorator
{
	private static final Selector<WorldGenerator> GRASS_GENERATOR;
	
	static
	{
		Selector<IBlockState> selector = new WeightedRandomSelector<>(
				new Stack<>(Blocks.TALLGRASS.getDefaultState().withProperty(BlockTallGrass.TYPE, BlockTallGrass.EnumType.GRASS), 880),
				new Stack<>(M.bristlegrass.getProperty(MP.property_plant).block().getDefaultState(), 120));
		GRASS_GENERATOR = Selector.single(new WorldGenRandPlant(132, selector));
	}
	
	public int grassPerChunk = 1;
	public Selector<WorldGenerator> grassGenerator = GRASS_GENERATOR;
	
	@Override
	public void decorate(final World world, final Random rand, final BlockPos pos)
	{
		int pass, xOff, yOff, zOff, x, y, z;
		
		if(TerrainGen.decorate(world, rand, pos, Decorate.EventType.GRASS))
		{
			for (pass = 0; pass < this.grassPerChunk; ++pass)
			{
				xOff = rand.nextInt(16) + 8;
				zOff = rand.nextInt(16) + 8;
				y = 128 + (world.getHeight(pos.getX() + xOff, pos.getZ() + zOff) - 128) * 2;
				
				if (y > 0)
				{
					this.grassGenerator.next(rand).generate(world, rand, pos.add(xOff, rand.nextInt(y), zOff));
				}
			}
		}
	}
}