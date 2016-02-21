package flapi.solid;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import flapi.world.BlockPos;

public interface ISolidBlock
{
	boolean canFill(BlockPos aPos, Block aBock);
	boolean canDrain(BlockPos aPos, Block aBlock);
	SolidStack getSolidStackContain(BlockPos aPos, Block aBlock);
	int fill(World aWorld, BlockPos aPos, int maxFill, boolean doFill);
	SolidStack drain(World aWorld, BlockPos aPos, int maxDrain, boolean doDrain);
}
