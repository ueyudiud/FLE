/*
 * copyright© 2016-2017 ueyudiud
 */
package farcore.lib.world.gen;

import java.util.Random;

import nebula.common.world.ICoord;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenerator;

public abstract class FarWorldGenerator extends WorldGenerator implements ICoord
{
	protected World				world;
	protected BlockPos			pos;
	protected Random			rand;
	protected IChunkGenerator	chunkGenerator;
	
	@Override
	public boolean generate(World worldIn, Random rand, BlockPos position)
	{
		return generate(worldIn, rand, position, worldIn.getWorldType().getChunkGenerator(worldIn, null));
	}
	
	public final boolean generate(World worldIn, Random rand, BlockPos position, IChunkGenerator chunkGenerator)
	{
		this.world = worldIn;
		this.pos = position;
		this.rand = rand;
		this.pos = moveGeneratePosition();
		this.chunkGenerator = chunkGenerator;
		return this.pos == null ? false : generate();
	}
	
	protected BlockPos moveGeneratePosition()
	{
		return this.pos;
	}
	
	protected abstract boolean generate();
	
	@Override
	public World world()
	{
		return this.world;
	}
	
	@Override
	public BlockPos pos()
	{
		return this.pos;
	}
}
