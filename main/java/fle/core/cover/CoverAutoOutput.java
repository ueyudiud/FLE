package fle.core.cover;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import fle.api.cover.CoverTile;
import fle.api.cover.IItemIOCover;
import fle.api.te.TEBase;
import fle.api.world.BlockPos;
import fle.core.cover.tile.CoverTileAutoOutput;

public class CoverAutoOutput extends CoverBase
{
	public final int loop;
	public final int maxStackLimit;
	
	public CoverAutoOutput(String unlocalized, int tick, int sizeLimit)
	{
		super(unlocalized);
		loop = tick;
		maxStackLimit = sizeLimit;
	}
	
	@Override
	public CoverTile createCoverTile(ForgeDirection dir, TEBase tile)
	{
		return new CoverTileAutoOutput(dir, tile, this);
	}
}