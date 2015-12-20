package flapi.recipe.stack;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidContainerRegistry.FluidContainerData;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;
import flapi.collection.abs.AbstractStack;

public class FluidContainerStack extends ItemAbstractStack
{
	boolean isDrain;
	FluidStack fluid;

	public FluidContainerStack()
	{
		isDrain = false;
	}
	public FluidContainerStack(Fluid fluid)
	{
		this(fluid, 1);
	}
	public FluidContainerStack(Fluid fluid, int amount)
	{
		this(new FluidStack(fluid, amount));
	}
	public FluidContainerStack(FluidStack stack)
	{
		this.fluid = stack;
		this.isDrain = true;
	}

	@Override
	public boolean equal(ItemStack arg)
	{
		if(arg == null) return false;
		if(!isDrain)
		{
			return arg.getItem() instanceof IFluidContainerItem || FluidContainerRegistry.fillFluidContainer(fluid, arg) != null;
		}
		FluidStack c = null;
		if(arg.getItem() instanceof IFluidContainerItem)
		{
			c = ((IFluidContainerItem) arg.getItem()).getFluid(arg);
		}
		else if(FluidContainerRegistry.isFilledContainer(arg))
		{
			c = FluidContainerRegistry.getFluidForFilledItem(arg);
		}
		return c != null && c.containsFluid(fluid);
	}

	@Override
	public boolean equal(AbstractStack<ItemStack> arg)
	{
		return arg instanceof FluidContainerStack ? FluidStack.areFluidStackTagsEqual(fluid, ((FluidContainerStack) arg).fluid) : false;
	}

	@Override
	public boolean contain(AbstractStack<ItemStack> arg)
	{
		return arg instanceof FluidContainerStack ? fluid == null ? ((FluidContainerStack) arg).fluid == null : fluid.containsFluid(((FluidContainerStack) arg).fluid) : false;
	}

	@Override
	public ItemStack[] toList()
	{
		List<ItemStack> list = new ArrayList();
		for(FluidContainerData data : FluidContainerRegistry.getRegisteredFluidContainerData())
		{
			if(isDrain && data.emptyContainer != null)
			{
				list.add(data.emptyContainer);
			}
			else if(isDrain)
			{
				if(data.fluid != null && data.fluid.containsFluid(fluid))
					list.add(data.filledContainer);
			}
		}
		return list.toArray(new ItemStack[list.size()]);
	}
	
	public ItemStack fillOrDrainFluid(ItemStack stack)
	{
		if(isDrain)
		{
			return FluidContainerRegistry.drainFluidContainer(stack);
		}
		else
		{
			return FluidContainerRegistry.fillFluidContainer(this.fluid, stack);
		}
	}
}