/*
 * copyright© 2016-2018 ueyudiud
 */
package farcore.lib.capability;

import javax.annotation.Nullable;

import nebula.base.A;
import nebula.common.fluid.FluidTankN;
import nebula.common.util.Direction;
import nebula.common.util.FluidStacks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

/**
 * A fluid handler helper.
 * 
 * @author ueyudiud
 */
public interface IFluidHandlerHelper
{
	boolean canFill(Direction direction, FluidStack stack);
	
	boolean canDrain(Direction direction, FluidStack stack);
	
	int fill(Direction direction, FluidStack resource, boolean process);
	
	default FluidStack drain(Direction direction, FluidStack required, boolean process)
	{
		return canDrain(direction, required) ? drain(direction, required.amount, process) : null;
	}
	
	FluidStack drain(Direction direction, int maxAmount, boolean process);
	
	@Nullable
	SidedFluidIOProperty getProperty(Direction direction);
	
	default boolean shouldProviedeFluidIOFrom(Direction direction)
	{
		return true;
	}
	
	interface SidedFluidIOProperty
	{
		int getCapacity();
		
		FluidStack[] getStacks();
		
		default boolean canFill()
		{
			return true;
		}
		
		default boolean canDrain()
		{
			return true;
		}
		
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
		public FluidStack[] getStacks()
		{
			return new FluidStack[] { tank.getFluid() };
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
	
	class SidedFluidIOTankNPropertyWrapper implements SidedFluidIOProperty
	{
		FluidTankN	tank;
		Direction	direction;
		
		public SidedFluidIOTankNPropertyWrapper(FluidTankN tank)
		{
			this(tank, Direction.Q);
		}
		
		public SidedFluidIOTankNPropertyWrapper(FluidTankN tank, Direction direction)
		{
			this.tank = tank;
			this.direction = direction;
		}
		
		@Override
		public int getCapacity()
		{
			return tank.getCapacity();
		}
		
		@Override
		public FluidStack[] getStacks()
		{
			return new FluidStack[] { tank.getFluid() };
		}
		
		@Override
		public boolean canFill()
		{
			return tank.canInsertFluid(direction, null) && tank.isFull();
		}
		
		@Override
		public boolean canDrain()
		{
			return tank.canExtractFluid(direction) && tank.hasFluid();
		}
		
		@Override
		public boolean canFill(FluidStack stack)
		{
			return canFill();
		}
		
		@Override
		public boolean canDrain(FluidStack stack)
		{
			return tank.canExtractFluidWithType(direction, stack) && tank.hasFluid();
		}
	}
	
	class SidedFluidIOPropertyWrapper implements IFluidTankProperties
	{
		SidedFluidIOProperty	property;
		FluidStack				stack	= null;
		
		public SidedFluidIOPropertyWrapper(SidedFluidIOProperty property)
		{
			this.property = property;
		}
		
		public SidedFluidIOPropertyWrapper(SidedFluidIOProperty property, int id)
		{
			this.property = property;
			if (id >= 0)
			{
				this.stack = FluidStacks.copy(property.getStacks()[id]);
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
		Direction				direction;
		IFluidHandlerHelper			handler;
		IFluidTankProperties[]	properties;
		
		public FluidHandlerWrapper(TileEntity tile, EnumFacing direction)
		{
			this(tile, Direction.of(direction));
		}
		
		public FluidHandlerWrapper(TileEntity tile, Direction direction)
		{
			this.handler = (IFluidHandlerHelper) tile;
			this.direction = direction;
		}
		
		@Override
		public IFluidTankProperties[] getTankProperties()
		{
			if (properties != null) return properties;
			SidedFluidIOProperty property = handler.getProperty(direction);
			FluidStack[] list = property.getStacks();
			return properties = (list.length == 0 ? new IFluidTankProperties[] { new SidedFluidIOPropertyWrapper(property) } : A.transform(list, IFluidTankProperties.class, stack -> new SidedFluidIOPropertyWrapper(property, stack)));
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
