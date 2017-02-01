/*
 * copyright© 2016-2017 ueyudiud
 */

package farcore.lib.capability;

import java.util.Arrays;
import java.util.List;

import com.google.common.collect.ImmutableList;

import nebula.common.util.Direction;
import nebula.common.util.L;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

/**
 * A fluid handler helper.
 * @author ueyudiud
 */
public interface IFluidHandler
{
	boolean canFill(Direction direction, FluidStack stack);
	
	boolean canDrain(Direction direction, FluidStack stack);
	
	int fill(Direction direction, FluidStack resource, boolean process);
	
	default FluidStack drain(Direction direction, FluidStack required, boolean process)
	{
		return canDrain(direction, required) ? drain(direction, required.amount, process) : null;
	}
	
	FluidStack drain(Direction direction, int maxAmount, boolean process);
	
	SidedFluidIOProperty getProperty(Direction direction);
	
	default boolean shouldProviedeFluidIOFrom(Direction direction) { return true; }
	
	interface SidedFluidIOProperty
	{
		int getCapacity();
		
		List<FluidStack> getStacks();
		
		default boolean canFill() { return true; }
		
		default boolean canDrain() { return true; }
		
		boolean canFill(FluidStack stack);
		
		boolean canDrain(FluidStack stack);
	}
	
	class SidedFluidIOTankPropertyWrapper implements SidedFluidIOProperty
	{
		FluidTank tank;
		
		public SidedFluidIOTankPropertyWrapper(FluidTank tank)
		{
			this.tank = tank;
		}
		
		@Override
		public int getCapacity()
		{
			return tank.getCapacity();
		}
		
		@Override
		public List<FluidStack> getStacks()
		{
			return tank.getFluid() == null ? ImmutableList.of() : Arrays.asList(tank.getFluid());
		}
		
		@Override
		public boolean canFill()
		{
			return tank.canFill();
		}
		
		@Override
		public boolean canDrain()
		{
			return tank.canDrain();
		}
		
		@Override
		public boolean canFill(FluidStack stack)
		{
			return tank.canFillFluidType(stack);
		}
		
		@Override
		public boolean canDrain(FluidStack stack)
		{
			return tank.canDrainFluidType(stack);
		}
	}
	
	class SidedFluidIOPropertyWrapper implements IFluidTankProperties
	{
		SidedFluidIOProperty property;
		FluidStack stack = null;
		
		public SidedFluidIOPropertyWrapper(SidedFluidIOProperty property)
		{
			this.property = property;
		}
		
		public SidedFluidIOPropertyWrapper(SidedFluidIOProperty property, int id)
		{
			this.property = property;
			if(id >= 0)
			{
				this.stack = property.getStacks().get(id);
			}
		}
		
		public SidedFluidIOPropertyWrapper(SidedFluidIOProperty property, FluidStack stack)
		{
			this.property = property;
			this.stack = stack.copy();
		}
		
		@Override
		public FluidStack getContents()
		{
			return stack;
		}
		
		@Override
		public int getCapacity()
		{
			return property.getCapacity();
		}
		
		@Override
		public boolean canFill()
		{
			return property.canFill();
		}
		
		@Override
		public boolean canDrain()
		{
			return property.canDrain();
		}
		
		@Override
		public boolean canFillFluidType(FluidStack fluidStack)
		{
			return property.canFill(fluidStack);
		}
		
		@Override
		public boolean canDrainFluidType(FluidStack fluidStack)
		{
			return property.canDrain(fluidStack);
		}
	}
	
	class FluidHandlerWrapper implements net.minecraftforge.fluids.capability.IFluidHandler
	{
		Direction direction;
		IFluidHandler handler;
		IFluidTankProperties[] properties;
		
		public FluidHandlerWrapper(TileEntity tile, Direction direction)
		{
			this.handler = (IFluidHandler) tile;
			this.direction = direction;
		}
		
		@Override
		public IFluidTankProperties[] getTankProperties()
		{
			if(properties != null) return properties;
			SidedFluidIOProperty property = handler.getProperty(direction);
			List<FluidStack> list = property.getStacks();
			return properties = (list.isEmpty() ? new IFluidTankProperties[]{ new SidedFluidIOPropertyWrapper(property) } :
				L.transform(L.cast(list, FluidStack.class), IFluidTankProperties.class,
						stack -> new SidedFluidIOPropertyWrapper(property, stack)));
		}
		
		@Override
		public int fill(FluidStack resource, boolean doFill)
		{
			return handler.fill(direction, resource, doFill);
		}
		
		@Override
		public FluidStack drain(FluidStack resource, boolean doDrain)
		{
			return handler.drain(direction, resource, doDrain);
		}
		
		@Override
		public FluidStack drain(int maxDrain, boolean doDrain)
		{
			return handler.drain(direction, maxDrain, doDrain);
		}
	}
}