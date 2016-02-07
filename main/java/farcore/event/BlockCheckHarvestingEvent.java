package farcore.event;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;

public class BlockCheckHarvestingEvent extends BlockEvent
{
	public boolean succeed;
	public final boolean defaultSucceed;
	
	public BlockCheckHarvestingEvent(int x, int y, int z, World world, Block block, int meta, boolean can)
	{
		super(x, y, z, world, block, meta);
		this.succeed = defaultSucceed = can;
	}
}