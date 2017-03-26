/*
 * copyright© 2016-2017 ueyudiud
 */

package fargen.core.biome.decorator;

import java.util.Random;

import nebula.common.base.Selector;
import net.minecraft.block.BlockTallGrass.EnumType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenTallGrass;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.event.terraingen.TerrainGen;

/**
 * @author ueyudiud
 */
public class SimpleBiomeDecorator extends BiomeDecorator
{
	private static final Selector<WorldGenerator> GRASS_GENERATOR = Selector.single(new WorldGenTallGrass(EnumType.GRASS));
	
	public int grassPerChunk = 1;
	public Selector<WorldGenerator> grassGenerator = GRASS_GENERATOR;
	
	@Override
	public void decorate(final World world, final Random rand, final BlockPos pos)
	{
		int pass, xOff, yOff, zOff, x, y, z;
		
		if(TerrainGen.decorate(world, rand, pos, DecorateBiomeEvent.Decorate.EventType.GRASS))
		{
			for (pass = 0; pass < this.grassPerChunk; ++pass)
			{
				xOff = rand.nextInt(16) + 8;
				zOff = rand.nextInt(16) + 8;
				y = world.getHeight(pos.getX() + xOff, pos.getZ() + zOff) * 2;
				
				if (y > 0)
				{
					this.grassGenerator.next(rand).generate(world, rand, pos.add(xOff, rand.nextInt(y), zOff));
				}
			}
		}
	}
}