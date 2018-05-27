/*
 * copyright 2016-2018 ueyudiud
 */
package farcore.lib.tree;

import java.util.Random;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import farcore.lib.bio.BioData;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@ParametersAreNonnullByDefault
public interface ITreeGenerator
{
	default boolean generateTreeAt(World world, BlockPos pos, Random random, @Nullable BioData info)
	{
		return generateTreeAt(world, pos.getX(), pos.getY(), pos.getZ(), random, info);
	}
	
	boolean generateTreeAt(World world, int x, int y, int z, Random random, @Nullable BioData info);
}
