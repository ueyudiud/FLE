package farcore.lib.world.gen;

import java.util.Random;

import nebula.common.world.ICoord;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public abstract class FarWorldGenerator extends WorldGenerator implements ICoord
{
	protected World world;
	protected BlockPos pos;
	protected Random rand;

	@Override
	public final boolean generate(World worldIn, Random rand, BlockPos position)
	{
		world = worldIn;
		pos = position;
		this.rand = rand;
		pos = moveGeneratePosition();
		return pos == null ? false : generate();
	}

	protected BlockPos moveGeneratePosition()
	{
		return pos;
	}
	
	protected abstract boolean generate();
	
	@Override
	public World world()
	{
		return world;
	}
	
	@Override
	public BlockPos pos()
	{
		return pos;
	}
}