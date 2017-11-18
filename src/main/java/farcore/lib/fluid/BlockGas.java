/*
 * copyright© 2016-2017 ueyudiud
 */
package farcore.lib.fluid;

import java.util.Random;

import nebula.common.block.BlockStreamFluid;
import nebula.common.fluid.FluidBase;
import nebula.common.fluid.FluidBlockEvent.FluidTouchBlockEvent;
import nebula.common.util.Direction;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.fml.common.eventhandler.Event.Result;

/**
 * @author ueyudiud
 */
public class BlockGas extends BlockStreamFluid
{
	public BlockGas(FluidBase fluid, Material material)
	{
		super(fluid, material);
		this.displacements.clear();
	}
	
	@Override
	public boolean canCollideCheck(IBlockState state, boolean fullHit)
	{
		return false;//You can not collide gas, use tool instead :D
	}
	
	@Override
	public IBlockState getExtendedState(IBlockState oldState, IBlockAccess worldIn, BlockPos pos)
	{
		IExtendedBlockState state = (IExtendedBlockState) oldState;
		state = state.withProperty(FLOW_DIRECTION, -1000.0F);
		state = state.withProperty(LEVEL_CORNERS[0], 1.0F);
		state = state.withProperty(LEVEL_CORNERS[1], 1.0F);
		state = state.withProperty(LEVEL_CORNERS[2], 1.0F);
		state = state.withProperty(LEVEL_CORNERS[3], 1.0F);
		return state;
	}
	
	@Override
	public int getMaxRenderHeightMeta()
	{
		return 1;
	}
	
	@Override
	public float getFluidHeightForRender(IBlockAccess world, BlockPos pos)
	{
		return 1.0F;
	}
	
	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		int level = state.getValue(getLevelProperty()) + 1;
		int prelevel = level;
		byte repleaced = 0;
		for (EnumFacing facing : EnumFacing.VALUES)
		{
			level = displaceIfPossible(worldIn, pos.offset(facing), pos, level);
			if (level == 0) return;
			if (level > 0)
				repleaced |= 1 << facing.ordinal();
			else
				level = -level;
		}
		if (repleaced == 0)
		{
			if (prelevel != level)
			{
				setFluidLevel(worldIn, pos, level, true);
			}
			return;
		}
		int[] neighbourLevels = new int[6];
		for (Direction direction : Direction.DIRECTIONS_3D)
		{
			int i = getFluidLevel(worldIn, direction.offset(pos));
			neighbourLevels[direction.ordinal()] = i >= level ? -1 : i;
		}
		
		int count = 1;
		int total = level;
		for (int i : neighbourLevels)
		{
			if (i >= 0)
			{
				++count;
				total += i;
			}
		}
		
		if (count == 1)
		{
			if (prelevel != level)
			{
				setFluidLevel(worldIn, pos, level, true);
			}
			return;
		}
		
		int each = total / count;
		int rem = total % count;
		for (Direction direction : Direction.DIRECTIONS_3D)
		{
			if (neighbourLevels[direction.ordinal()] >= 0)
			{
				int n = each;
				if (rem == count || rem > 1 && rand.nextInt(count - rem) != 0)
				{
					++n;
					--rem;
				}
				if (n != neighbourLevels[direction.ordinal()])
				{
					setFluidLevel(worldIn, direction.offset(pos), n, true);
				}
				--count;
			}
		}
		
		if (rem > 0)
		{
			++each;
		}
		if (each != prelevel)
		{
			setFluidLevel(worldIn, pos, each, true);
		}
	}
	
	@Override
	public int displaceIfPossible(World world, BlockPos pos, BlockPos source, int level)
	{
		if (!world.isAreaLoaded(pos, 2)) return -level;
		if (world.isAirBlock(pos)) return level;
		IBlockState state = world.getBlockState(pos);
		if (state.getBlock() == this) return level;
		FluidTouchBlockEvent event = new FluidTouchBlockEvent(world, source, pos, state, this, level);
		MinecraftForge.EVENT_BUS.post(event);
		if (event.getResult() == Result.ALLOW)
		{
			world.setBlockState(pos, event.getEndingTargetState(), 3);
			return event.amount;
		}
		
		Block block = state.getBlock();
		
		Boolean flag = this.displacements.get(block);
		if (flag != null)
		{
			if (flag)
			{
				block.dropBlockAsItem(world, pos, state, 0);
				world.setBlockToAir(pos);
				return level;
			}
			return -level;
		}
		
		//		Material material = state.getMaterial();
		//		if (material.blocksMovement() || material == Material.PORTAL) return -level;
		//
		//		int density = getDensity(world, pos);
		//		if (density == Integer.MAX_VALUE)
		//		{
		//			block.dropBlockAsItem(world, pos, state, 0);
		//			world.setBlockToAir(pos);
		//			return level;
		//		}
		
		// if (this.density > density)
		// return true;
		// else
		return -level;
	}
}