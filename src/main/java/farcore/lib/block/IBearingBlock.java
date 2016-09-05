package farcore.lib.block;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public interface IBearingBlock
{
	boolean canBearingAt(IBlockAccess world, BlockPos pos, BlockPos target);
	
	void bearEntityWalkOn(World world, BlockPos pos, BlockPos target, Entity entity);
}