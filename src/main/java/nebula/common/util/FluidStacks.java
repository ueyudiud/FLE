/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.common.util;

import javax.annotation.Nullable;

import nebula.common.block.ISmartFluidBlock;
import nebula.common.data.Misc;
import nebula.common.fluid.FluidBlockEvent;
import nebula.common.fluid.FluidBlockEvent.FluidDrainToWorldEvent;
import nebula.common.fluid.FluidBlockEvent.FluidFillFromWorldEvent;
import nebula.common.fluid.FluidStackExt;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;

/**
 * @author ueyudiud
 */
public final class FluidStacks
{
	private FluidStacks() {}
	
	public static @Nullable Fluid getFluid(@Nullable FluidStack stack)
	{
		return stack == null ? null : stack.getFluid();
	}
	
	public static int getAmount(@Nullable FluidStack stack)
	{
		return stack == null ? 0 : stack.amount;
	}
	
	public static boolean isGaseous(FluidStack stack)
	{
		return stack.getFluid().isGaseous(stack);
	}
	
	public static int getTemperature(FluidStack stack, int def)
	{
		return stack instanceof FluidStackExt ?
				((FluidStackExt) stack).getTemperature() :
					stack != null ? stack.getFluid().getTemperature(stack) : def;
	}
	
	public static int getColor(FluidStack stack)
	{
		return stack.getFluid().getColor(stack);
	}
	
	public static int getViscosity(FluidStack stack)
	{
		return stack.getFluid().getViscosity(stack);
	}
	
	public static FluidStack fillFluidFromWorld(World world, @Nullable RayTraceResult result, int maxFill, @Nullable Fluid requiredFluid, boolean doFill)
	{
		if(result == null || result.typeOfHit != RayTraceResult.Type.BLOCK) return null;
		return fillFluidFromWorld(world, result.getBlockPos(), maxFill, requiredFluid, doFill);
	}
	
	public static FluidStack fillFluidFromWorld(World world, BlockPos pos, int maxFill, @Nullable Fluid requiredFluid, boolean doFill)
	{
		if(maxFill == 0) return null;
		IBlockState state = world.getBlockState(pos);
		if(doFill)
		{
			FluidFillFromWorldEvent event = new FluidFillFromWorldEvent(world, pos, requiredFluid, maxFill);
			if(MinecraftForge.EVENT_BUS.post(event)) return null;
			
			if(event.getRet() != null)
			{
				return event.getRet().copy();
			}
		}
		if(state.getBlock() instanceof ISmartFluidBlock)
		{
			ISmartFluidBlock block = (ISmartFluidBlock) state.getBlock();
			if(requiredFluid == null || block.getFluid() == requiredFluid)
			{
				return ((ISmartFluidBlock) state.getBlock()).drain(world, pos, maxFill, doFill);
			}
		}
		else if(state.getBlock() instanceof IFluidBlock)
		{
			IFluidBlock block = (IFluidBlock) state.getBlock();
			if((requiredFluid == null || block.getFluid() == requiredFluid) && block.canDrain(world, pos))
			{
				return block.drain(world, pos, doFill);
			}
		}
		else if(state.getBlock() instanceof BlockLiquid && state.getValue(BlockLiquid.LEVEL) == 0)
		{
			if((requiredFluid == null || requiredFluid == FluidRegistry.WATER) && state.getMaterial() == Material.WATER)
			{
				if(doFill)
				{
					world.setBlockToAir(pos);
				}
				return new FluidStack(FluidRegistry.WATER, 1000);
			}
			else if((requiredFluid == null || requiredFluid == FluidRegistry.LAVA) && state.getMaterial() == Material.LAVA)
			{
				if(doFill)
				{
					world.setBlockToAir(pos);
				}
				return new FluidStack(FluidRegistry.LAVA, 1000);
			}
		}
		return null;
	}
	
	public static int drainFluidToWorld(World world, @Nullable RayTraceResult result, @Nullable FluidStack stack, boolean doDrain)
	{
		if(result == null || result.typeOfHit != RayTraceResult.Type.BLOCK) return 0;
		int amount = drainFluidToWorld(world, result.getBlockPos(), stack, doDrain);//For smart fluid block.
		if(amount > 0) return amount;
		return drainFluidToWorld(world, result.getBlockPos().offset(result.sideHit), stack, doDrain);
	}
	
	public static int drainFluidToWorld(World world, BlockPos pos, @Nullable FluidStack stack, boolean doDrain)
	{
		if(stack == null || !stack.getFluid().canBePlacedInWorld()) return 0;
		Block blockRaw = stack.getFluid().getBlock();
		if(doDrain)
		{
			FluidBlockEvent.FluidDrainToWorldEvent event = new FluidDrainToWorldEvent(world, pos, blockRaw, stack);
			if(MinecraftForge.EVENT_BUS.post(event)) return 0;
			
			if(event.getUsed() > 0)
			{
				return event.getUsed();
			}
		}
		if(blockRaw instanceof ISmartFluidBlock)
		{
			ISmartFluidBlock block = (ISmartFluidBlock) blockRaw;
			return block.fill(world, pos, stack, doDrain);
		}
		else if(blockRaw instanceof BlockFluidBase)
		{
			if(stack.amount >= Misc.BUCKET_CAPACITY && Worlds.isAirOrReplacable(world, pos))
			{
				if(doDrain)
				{
					world.setBlockState(pos, blockRaw.getDefaultState());
				}
				return Misc.BUCKET_CAPACITY;
			}
		}
		else if(blockRaw instanceof BlockLiquid)//Use for vanilla water and lava.
		{
			if(stack.amount >= Misc.BUCKET_CAPACITY && Worlds.isAirOrReplacable(world, pos))
			{
				if(doDrain)
				{
					world.setBlockState(pos, blockRaw.getDefaultState());
				}
				return Misc.BUCKET_CAPACITY;
			}
		}
		return 0;//Unknown fluid type will prevent to drain to world.
	}
	
	public static boolean areFluidStacksEqual(FluidStack stack1, FluidStack stack2)
	{
		return stack1 == null || stack2 == null ? stack1 == stack2 :
			stack1.isFluidStackIdentical(stack2);
	}
	
	public static FluidStack copy(FluidStack fluid)
	{
		return fluid == null ? null : fluid.copy();
	}
	
	public static FluidStack sizeOf(FluidStack stack, int amount)
	{
		FluidStack stack2;
		(stack2 = stack.copy()).amount = amount;
		return stack2;
	}
}