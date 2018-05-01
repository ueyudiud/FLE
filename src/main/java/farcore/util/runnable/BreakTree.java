/*
 * copyright© 2016-2018 ueyudiud
 */
package farcore.util.runnable;

import java.util.ArrayList;

import farcore.blocks.flora.BlockLogNatural;
import farcore.data.EnumItem;
import farcore.data.V;
import farcore.items.ItemTreeLog;
import nebula.common.util.W;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BreakTree implements Runnable
{
	private BlockLogNatural	block;
	private World			world;
	private BlockPos		pos;
	private int				cacheDrop	= 0;
	
	public BreakTree(BlockLogNatural block, World world, BlockPos pos)
	{
		this.block = block;
		this.world = world;
		this.pos = pos;
	}
	
	@Override
	public void run()
	{
		scanLog(this.world, new boolean[2 * V.treeScanRange + 1][256][2 * V.treeScanRange + 1], (byte) 0, (byte) 0, (byte) 0);
		if (this.cacheDrop > 0)
		{
			ItemStack stack = new ItemStack(EnumItem.log.item, 1, this.block.tree.material.id);
			ItemTreeLog.setLogSize(stack, this.cacheDrop);
			W.spawnDropInWorld(this.world, this.pos, stack);
		}
	}
	
	public void scanLog(World world, boolean[][][] array, byte x, byte y, byte z)
	{
		if (y >= 0 && this.pos.getY() + y < 256)
		{
			int offsetX = 0, offsetY = 0, offsetZ = 0;
			array[x + V.treeScanRange][y][z + V.treeScanRange] = true;
			for (offsetX = -1; offsetX <= 1; offsetX++)
			{
				for (offsetZ = -1; offsetZ <= 1; offsetZ++)
				{
					for (offsetY = 0; offsetY <= 2; offsetY++)
						if ((offsetX != 0 || offsetY != 0 || offsetZ != 0) && Math.abs(x + offsetX) <= V.treeScanRange && Math.abs(z + offsetZ) <= V.treeScanRange && this.pos.getY() + y + offsetY < 256)
							if (!array[x + offsetX + V.treeScanRange][y + offsetY][z + offsetZ + V.treeScanRange] && this.block.isLog(world, this.pos.add(x + offsetX, y + offsetY, z + offsetZ)))
							{
								scanLog(world, array, (byte) (x + offsetX), (byte) (y + offsetY), (byte) (z + offsetZ));
							}
				}
			}
			++this.cacheDrop;
			BlockPos pos1 = this.pos.add(x, y, z);
			world.setBlockState(pos1, Blocks.AIR.getDefaultState(), 2);
			// Some update causes out of sych.
			beginLeavesDecay(world, pos1.up());
			beginLeavesDecay(world, pos1.down());
			beginLeavesDecay(world, pos1.south());
			beginLeavesDecay(world, pos1.north());
			beginLeavesDecay(world, pos1.east());
			beginLeavesDecay(world, pos1.west());
			W.spawnDropsInWorld(world, pos1, this.block.tree.getLogOtherDrop(world, pos1, new ArrayList()));
		}
	}
	
	protected void beginLeavesDecay(World world, BlockPos pos)
	{
		IBlockState state;
		(state = world.getBlockState(pos)).getBlock().beginLeavesDecay(state, world, pos);
	}
}
