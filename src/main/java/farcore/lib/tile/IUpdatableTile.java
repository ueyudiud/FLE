package farcore.lib.tile;

import net.minecraft.block.Block;

public interface IUpdatableTile
{
	void causeUpdate(int x, int y, int z, Block block, int meta, boolean tileUpdate);
}