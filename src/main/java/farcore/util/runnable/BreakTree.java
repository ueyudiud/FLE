package farcore.util.runnable;

import java.util.ArrayList;
import java.util.Arrays;

import farcore.data.EnumItem;
import farcore.data.V;
import farcore.lib.block.instance.BlockLogNatural;
import farcore.lib.item.instance.ItemTreeLog;
import farcore.util.U;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BreakTree implements Runnable
{
	private BlockLogNatural block;
	private World world;
	private BlockPos pos;
	private int cacheDrop = 0;

	public BreakTree(BlockLogNatural block, World world, BlockPos pos)
	{
		this.block = block;
		this.world = world;
		this.pos = pos;
	}

	@Override
	public void run()
	{
		boolean[][][] cacheArray;
		scanLog(world, cacheArray = new boolean[2 * V.treeScanRange + 1][256][2 * V.treeScanRange + 1], (byte) 0, (byte) 0, (byte) 0);
		if(cacheDrop > 0)
		{
			ItemStack stack = new ItemStack(EnumItem.log.item, 1, block.tree.material().id);
			ItemTreeLog.setLogSize(stack, cacheDrop);
			U.Worlds.spawnDropsInWorld(world, pos, Arrays.asList(stack));
		}
	}

	public void scanLog(World world, boolean[][][] array, byte x, byte y, byte z)
	{
		if(y >= 0 && pos.getY() + y < 256)
		{
			int offsetX = 0,
					offsetY = 0,
					offsetZ = 0;
			array[x + V.treeScanRange][y][z + V.treeScanRange] = true;
			for (offsetX = -1; offsetX <= 1; offsetX++)
				for (offsetZ = -1; offsetZ <= 1; offsetZ++)
					for (offsetY = 0; offsetY <= 2; offsetY++)
						if((offsetX != 0 || offsetY != 0 || offsetZ != 0) &&
								Math.abs(x + offsetX) <= V.treeScanRange &&
								Math.abs(z + offsetZ) <= V.treeScanRange &&
								pos.getY() + y + offsetY < 256)
							if(!array[x + offsetX + V.treeScanRange][y + offsetY][z + offsetZ + V.treeScanRange] &&
									block.isLog(world, pos.add(x + offsetX, y + offsetY, z + offsetZ)))
								scanLog(world, array, (byte) (x + offsetX), (byte) (y + offsetY), (byte) (z + offsetZ));
			++cacheDrop;
			BlockPos pos1 = pos.add(x, y, z);
			world.setBlockState(pos1, Blocks.AIR.getDefaultState(), 2);

			beginLeavesDecay(world, pos1.up());
			beginLeavesDecay(world, pos1.down());
			beginLeavesDecay(world, pos1.south());
			beginLeavesDecay(world, pos1.north());
			beginLeavesDecay(world, pos1.east());
			beginLeavesDecay(world, pos1.west());
			U.Worlds.spawnDropsInWorld(world, pos1, block.tree.getLogOtherDrop(world, pos1, new ArrayList()));
		}
	}

	protected void beginLeavesDecay(World world, BlockPos pos)
	{
		IBlockState state;
		(state = world.getBlockState(pos)).getBlock().beginLeavesDecay(state, world, pos);
	}
}