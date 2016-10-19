package farcore.util.runnable;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ThreadLight implements Runnable
{
	private World world;
	private BlockPos pos;

	public ThreadLight(World world, BlockPos pos)
	{
		this.world = world;
		this.pos = pos;
	}

	@Override
	public void run()
	{
		world.theProfiler.startSection("checkLight");
		world.checkLight(pos);
		world.theProfiler.endSection();
	}
}