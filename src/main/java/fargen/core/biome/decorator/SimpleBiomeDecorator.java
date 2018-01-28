/*
 * copyright© 2016-2017 ueyudiud
 */
package fargen.core.biome.decorator;

import java.util.Random;

import farcore.data.M;
import farcore.data.MP;
import farcore.lib.world.IWorldPropProvider;
import farcore.lib.world.WorldPropHandler;
import fargen.core.worldgen.generator.WorldGenStoneChip;
import nebula.base.Stack;
import nebula.base.function.Selector;
import nebula.base.function.WeightedRandomSelector;
import nebula.common.world.gen.WorldGenRandPlant;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate;
import net.minecraftforge.event.terraingen.TerrainGen;

/**
 * @author ueyudiud
 */
public class SimpleBiomeDecorator extends BiomeDecorator
{
	private static final WorldGenStoneChip GEN_STONE_CHIP = new WorldGenStoneChip();
	
	private static final Selector<WorldGenerator> GRASS_GENERATOR;
	
	static
	{
		Selector<IBlockState> selector = new WeightedRandomSelector<>(
				new Stack<>(M.short_grass.getProperty(MP.property_plant).block().getDefaultState(), 1440),
				new Stack<>(M.foxtail.getProperty(MP.property_plant).block().getDefaultState(), 340),
				new Stack<>(M.goosefoots.getProperty(MP.property_plant).block().getDefaultState(), 240),
				new Stack<>(M.knotgrass.getProperty(MP.property_plant).block().getDefaultState(), 500),
				new Stack<>(M.pennycress.getProperty(MP.property_plant).block().getDefaultState(), 210),
				new Stack<>(M.inula.getProperty(MP.property_plant).block().getDefaultState(), 200),
				new Stack<>(M.poa_bluegrass.getProperty(MP.property_plant).block().getDefaultState(), 420),
				new Stack<>(M.bristlegrass.getProperty(MP.property_plant).block().getDefaultState(), 120));
		GRASS_GENERATOR = Selector.single(new WorldGenRandPlant(132, selector));
	}
	
	public int						grassPerChunk		= 1;
	public boolean					enableStoneChipGen	= true;
	public Selector<WorldGenerator>	grassGenerator		= GRASS_GENERATOR;
	
	@Override
	public void decorate(final World world, final Random rand, final BlockPos pos, final IChunkGenerator generator)
	{
		int pass, xOff, yOff, zOff, x, y, z;
		
		IWorldPropProvider provider = WorldPropHandler.getWorldProperty(world);
		float temp = provider.getTemperature(world, pos);
		
		if (TerrainGen.decorate(world, rand, pos, Decorate.EventType.GRASS))
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
		if (this.enableStoneChipGen) GEN_STONE_CHIP.generate(world, rand, pos, generator);
	}
}
