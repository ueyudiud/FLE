package farcore.lib.recipe;

import net.minecraft.block.Block;
import net.minecraft.world.World;

public interface IBlockConditionMacher
{
	boolean match(World world, int x, int y, int z, Block block, int meta);
}